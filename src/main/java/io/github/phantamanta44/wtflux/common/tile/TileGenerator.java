package io.github.phantamanta44.wtflux.common.tile;

import cofh.api.fluid.IFluidContainerItem;
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.phantamanta44.wtflux.util.*;
import io.github.phantamanta44.wtflux.util.computercraft.CCMethod;
import io.github.phantamanta44.wtflux.util.computercraft.CCMethodBoolGetter;
import io.github.phantamanta44.wtflux.util.computercraft.CCMethodFloatGetter;
import io.github.phantamanta44.wtflux.util.computercraft.CCMethodIntGetter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class TileGenerator extends TileBasicInventory implements IEnergyProvider, IEnergyContainer, ITileItemNBT, IPeripheral {
    public static final int[] COIL_AMOUNTS = new int[]{128, 256, 512, 1024};
    public static final int[] CAP_AMOUNTS = new int[]{24000, 80000, 50000, 48000, 160000, 100000, 48000};
    public static final int[] CAP_RATES = new int[]{128, 256, 384, 256, 512, 768, -1};
    public static final float[] MELTING_POINTS = new float[]{
            449F, 1427F, 3215F, 3422F, 950F, 1085F, 1001F, 1063F,
            1593F, 328F, 512F, 1453F, 1084F, 961F, 1132F, 420F
    };
    public static final float[] RPM_CAPS = new float[]{
            12F, 34F, 32F, 60F, 24F, 20F, 20F, 20F, 36F, 15F, 15F,
            32F, 26F, 20F, 27F, 20F
    };
    public static final Class<? extends TileGenerator>[] GEN_TYPES = new Class[]{Furnace.class, Heat.class, Wind.class, Water.class, Nuke.class, Solar.class};

    int energy = 0, energyMax = 24000;
    byte gen = 0, dyn = 0, cap = 0, casing = 0;
    float momentum = 0F, temp = 23.0F;
    final boolean useResistance;
    final List<CCMethod> ccMethods;
    private boolean wasActive = false;

    public TileGenerator(int slots, boolean resist) {
        super(slots);

        useResistance = resist;
        ccMethods = Lists.newArrayList(
                new CCMethodBoolGetter("isActive", this::isActive),
                new CCMethodIntGetter("getOutputPower", this::getCapRate),
                new CCMethodIntGetter("getEnergy", this::getEnergyStored),
                new CCMethodIntGetter("getEnergyMax", this::getMaxEnergyStored),
                new CCMethodFloatGetter("getTemperature", this::getTemp),
                new CCMethodFloatGetter("getMeltingPoint", this::getMeltingPoint),
                new CCMethodFloatGetter("getVelocity", this::getMomentum),
                new CCMethodFloatGetter("getVelocityMax", this::getVelocityCap)
        );
    }

    @Override
    public void readItemTag(NBTTagCompound tag) {
        if (tag != null) {
            energy = tag.getInteger(ModReference.ENERGY);
            energyMax = tag.getInteger(ModReference.ENERGY_MAX);
            gen = tag.getByte(ModReference.GENTYPE);
            dyn = tag.getByte(ModReference.DYNTYPE);
            cap = tag.getByte(ModReference.CAPTYPE);
            casing = tag.getByte(ModReference.CASINGTYPE);
        } else {
            energy = 0;
            energyMax = CAP_AMOUNTS[4];
            gen = (byte) getBlockMetadata();
            dyn = (byte) 3;
            cap = (byte) 4;
            casing = (byte) 3;
        }
        markForUpdate();
        onInit();
        init = true;
    }

    @Override
    public void writeItemTag(NBTTagCompound tag) {
        tag.setInteger(ModReference.ENERGY, energy);
        tag.setInteger(ModReference.ENERGY_MAX, energyMax);
        tag.setByte(ModReference.GENTYPE, gen);
        tag.setByte(ModReference.DYNTYPE, dyn);
        tag.setByte(ModReference.CAPTYPE, cap);
        tag.setByte(ModReference.CASINGTYPE, casing);
    }

    @Override
    public ItemStack getNBTItem() {
        ItemStack stack = new ItemStack(blockType, 1, gen);
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeItemTag(tagCompound);
        stack.setTagCompound(tagCompound);
        return stack;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        compound.setInteger(ModReference.ENERGY, energy);
        compound.setInteger(ModReference.ENERGY_MAX, energyMax);
        compound.setByte(ModReference.GENTYPE, gen);
        compound.setByte(ModReference.DYNTYPE, dyn);
        compound.setByte(ModReference.CAPTYPE, cap);
        compound.setByte(ModReference.CASINGTYPE, casing);
        compound.setFloat(ModReference.MOMENTUM, momentum);
        compound.setFloat(ModReference.TEMP, temp);
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return init;
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        if (init) {
            int toTransfer = Math.max(Math.min(getCapRate(), maxExtract), 0);
            if (toTransfer > 0 && !simulate) {
                energy -= toTransfer;
                markForUpdate();
            }
            return toTransfer;
        }
        return 0;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return energy;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return energyMax;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return energyMax;
    }

    @Override
    protected void tick() {
        try {
            boolean dirty = doGeneration();

            dirty |= simulateInduction();
            dirty |= distributeEnergy();
            dirty |= simulateHeat();

            energy = Math.max(0, Math.min(energy, energyMax));

            if (dirty)
                markForUpdate();

            if (world.isRemote) {
                boolean active = isActive();
                if (active != wasActive)
                    world.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                wasActive = active;
            }
        } catch (MachineOverloadException ignored) {
        }
    }

    public boolean simulateInduction() throws MachineOverloadException {
        if (dyn >= COIL_AMOUNTS.length)
            dyn = 3;
        float voltage = MathUtil.voltageFromFlux(momentum, COIL_AMOUNTS[dyn]);
        if (useResistance)
            voltage /= MathUtil.resistanceFromHeat(temp);
        energy += voltage;
        float momentumLossFactor = (0.997F - momentum / 2400F);
        momentum *= momentumLossFactor;
        temp += Math.max(momentum / 4200F, 0);
        if (momentum > RPM_CAPS[casing]) {
            doOverspeed();
            throw new MachineOverloadException();
        }
        return true;
    }

    public boolean distributeEnergy() {
        BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        Map<EnumFacing, IEnergyReceiver> tiles = Maps.newHashMap();
        for (EnumFacing dir : EnumFacing.VALUES) {
            TileEntity tile = world.getTileEntity(blockPos);
            if (tile != null && tile instanceof IEnergyReceiver)
                tiles.put(dir.getOpposite(), (IEnergyReceiver) tile);
        }
        Set<Map.Entry<EnumFacing, IEnergyReceiver>> tileSet = tiles.entrySet();
        int dist = (int) Math.floor((float) Math.min(energy, getCapRate()) / (float) tileSet.size());
        for (Map.Entry<EnumFacing, IEnergyReceiver> tile : tileSet) {
            int v = tile.getValue().receiveEnergy(tile.getKey(), dist, false);
            energy -= v;
        }
        return !tileSet.isEmpty();
    }

    public boolean simulateHeat() throws MachineOverloadException {
        float prevTemp = temp;
        temp += (0.01F + world.rand.nextFloat()) * 0.001F * (getPassiveTemp() - temp);
        if (temp > MELTING_POINTS[casing]) {
            doMeltdown();
            throw new MachineOverloadException();
        }
        return prevTemp != temp;
    }

    public float getPassiveTemp() {
        BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        float temp = 23.0F;
        temp *= 0.95F + 0.15F * world.getSunBrightnessFactor(1.0F);
        temp *= 0.6F + 0.75F * world.getBiome(blockPos).getTemperature(blockPos);
        temp += 8F * (Math.max(70 - pos.getY(), 0F) / 70F);
        for (EnumFacing dir : EnumFacing.VALUES) {
            int x = pos.getX() + dir.getFrontOffsetX(), y = pos.getY() + dir.getFrontOffsetY(), z = pos.getZ() + dir.getFrontOffsetZ();
            Fluid fluid = FluidRegistry.lookupFluidForBlock((Block) world.getBlockState(blockPos));
            if (fluid != null) {
                float effTemp = useResistance
                        ? temp + (Math.min(fluid.getTemperature(world, blockPos) - 273.15F, 1000F) - temp) / 60F
                        : fluid.getTemperature(world, blockPos) - 273.15F;
                temp = (temp + effTemp) / 2F;
            }
        }
        return temp;
    }

    public float getTemp() {
        return temp;
    }

    public float getMomentum() {
        return momentum;
    }

    public int getCapRate() {
        int rate = CAP_RATES[cap];
        return rate >= 0 ? rate : Integer.MAX_VALUE;
    }

    protected void onInit() {
    }

    public abstract boolean isActive();

    protected abstract boolean doGeneration();

    protected boolean momentumFromHeat() {
        float prev = momentum;
        momentum = Math.min(momentum + Math.max((temp - 72.0F) / 1555.2F, 0), 384);
        return prev != momentum;
    }

    public static TileGenerator getAppropriateTile(int meta) {
        Class<? extends TileGenerator> genClass = GEN_TYPES[meta];
        try {
            return genClass.getConstructor().newInstance();
        } catch (Exception ex) {
            return null;
        }
    }

    public float getMeltingPoint() {
        return MELTING_POINTS[casing];
    }

    public void doMeltdown() {
        BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        world.setBlockState(blockPos, Blocks.LAVA.getDefaultState());
    }

    public float getVelocityCap() {
        return RPM_CAPS[casing];
    }

    public void doOverspeed() {
        BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
        world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), momentum * 0.3F, false);
    }

    @Nonnull
    @Override
    public String[] getMethodNames() {
        return ccMethods.stream().map(CCMethod::getName).toArray(String[]::new);
    }

    @Nullable
    @Override
    public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] arguments) throws LuaException, InterruptedException {
        return ccMethods.get(method).execute(computer, context, arguments);
    }

    @Override
    public void attach(@Nonnull IComputerAccess computer) {

    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {

    }

    @Nonnull
    @Override
    public String getType() {
        return "wtfGen" + this.getClass().getSimpleName();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other;
    }

    public static class Furnace extends TileGenerator {

        private int burnTime = 0;
        private int totalBurnTime = 1;
        BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

        public Furnace() {
            super(1, true);
            ccMethods.add(new CCMethodIntGetter("getBurnTime", this::getBurnTime));
            ccMethods.add(new CCMethodIntGetter("getFuelEnergy", this::getBurnTimeMax));
        }

        @Override
        public boolean isActive() {
            return burnTime > 0;
        }

        @Override
        protected boolean doGeneration() {
            boolean dirty = false;

            if (burnTime > 0) {
                burnTime--;
                temp += (float) totalBurnTime / (world.rand.nextFloat() * 2857F + 80000F);
                dirty = true;
            }

            dirty |= momentumFromHeat();

            if (burnTime == 0 && slots[0] != null) {
                int fuelValue = TileEntityFurnace.getItemBurnTime(slots[0]);
                if (fuelValue > 0) {
                    //slots[0].getMaxStackSize()--;
                    if (slots[0].getMaxStackSize() <= 0)
                        slots[0] = slots[0].getItem().getContainerItem(slots[0]);
                    burnTime = totalBurnTime = fuelValue;
                    dirty = true;
                }
            }
            return dirty;
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            super.readFromNBT(tag);
            burnTime = tag.getInteger(ModReference.BURN_TIME);
            totalBurnTime = tag.getInteger(ModReference.BURN_TIME_TOTAL);
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            tag.setInteger(ModReference.BURN_TIME, burnTime);
            tag.setInteger(ModReference.BURN_TIME_TOTAL, totalBurnTime);
            return tag;
        }

        public int getBurnTime() {
            return burnTime;
        }

        public int getBurnTimeMax() {
            return totalBurnTime;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack stack) {
            return TileEntityFurnace.isItemFuel(stack);
        }

        @Override
        public int getField(int i) {
            return 0;
        }

        @Override
        public void setField(int i, int i1) {

        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {

        }
    }

    public static class Heat extends TileGenerator implements IFluidHandler {

        private static final int TANK_SIZE = 10000;
        private SingleFluidTank tank = new SingleFluidTank(FluidRegistry.WATER, TANK_SIZE);

        public Heat() {
            super(2, false);
            ccMethods.add(new CCMethodBoolGetter("isTankEmpty", () -> tank.getFluidAmount() <= 0));
            ccMethods.add(new CCMethodIntGetter("getTankAmount", tank::getFluidAmount));
            ccMethods.add(new CCMethodIntGetter("getTankCapacity", tank::getCapacity));
        }

        @Override
        public boolean isActive() {
            return tank.getFluidAmount() >= 10;
        }

        @Override
        protected boolean doGeneration() {
            boolean dirty = false;
            if (tank.getFluidAmount() >= 10) {
                tank.drain(10, true);
                dirty |= momentumFromHeat();
            }
            if (slots[0] != null && slots[1] == null) {
                if (slots[0].getItem() instanceof IFluidContainerItem) {
                    IFluidContainerItem bucket = (IFluidContainerItem) slots[0].getItem();
                    FluidStack fluid = bucket.getFluid(slots[0]);
                    if (fluid.getFluid() == FluidRegistry.WATER) {
                        tank.fill(bucket.drain(slots[0], Math.min(1000, TANK_SIZE - tank.getFluidAmount()), true), true);
                        dirty = true;
                    }
                } else if (FluidContainerRegistry.isFilledContainer(slots[0])) {
                    FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(slots[0]);
                    if (fluid.getFluid() == FluidRegistry.WATER && TANK_SIZE - tank.getFluidAmount() >= fluid.amount) {
                        tank.fill(fluid, true);
                        slots[1] = slots[0].getItem().getContainerItem(slots[0]);
                        slots[0] = null;
                        dirty = true;
                    }
                }
            }
            return dirty;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int getField(int i) {
            return 0;
        }

        @Override
        public void setField(int i, int i1) {

        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {

        }

        @Override
        public IFluidTankProperties[] getTankProperties() {
            return new IFluidTankProperties[0];
        }

        @Override
        public int fill(FluidStack fluidStack, boolean b) {
            return 0;
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack fluidStack, boolean b) {
            return null;
        }

        @Nullable
        @Override
        public FluidStack drain(int i, boolean b) {
            return null;
        }
    }

    public static class Wind extends TileGenerator {

        public Wind() {
            super(0, true);
        }

        @Override
        public boolean isActive() {
            return pos.getY() >= 64;
        }

        @Override
        protected boolean doGeneration() {
            BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
            if (pos.getY() < 64)
                return false;

            float powerFactor = 0.23F * (float)Math.pow((float)(pos.getY() - 63) / (float)(world.getActualHeight() - 63), 2);
            if (world.isThundering())
                powerFactor *= 1.5F;
            for (int x = -3; x <= 3; x++) {
                for (int z = -3; z <= 3; z++) {
                    for (int y = -1; y <= 1; y++) {
                        if (!world.isAirBlock(blockPos)) {
                            if (world.isBlockFullCube(blockPos))
                                powerFactor *= 0.5F;
                            else
                                powerFactor *= 0.8F;
                        }
                    }
                }
            }
            momentum += powerFactor;
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int getField(int i) {
            return 0;
        }

        @Override
        public void setField(int i, int i1) {

        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {

        }
    }

    public static class Water extends TileGenerator implements IFluidHandler {

        private static final int TANK_SIZE = 4000, LWR_SIZE = 1000;
        private SingleFluidTank tank = new SingleFluidTank(FluidRegistry.WATER, TANK_SIZE);
        private SingleFluidTank lowerTank = new SingleFluidTank(FluidRegistry.WATER, LWR_SIZE);
        BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

        public Water() {
            super(2, true);
            ccMethods.add(new CCMethodBoolGetter("isTankEmpty", () -> tank.getFluidAmount() <= 0));
            ccMethods.add(new CCMethodIntGetter("getTankAmount", tank::getFluidAmount));
            ccMethods.add(new CCMethodIntGetter("getTankCapacity", tank::getCapacity));
            ccMethods.add(new CCMethodBoolGetter("isLowerTankEmpty", () -> lowerTank.getFluidAmount() <= 0));
            ccMethods.add(new CCMethodIntGetter("getLowerTankAmount", lowerTank::getFluidAmount));
            ccMethods.add(new CCMethodIntGetter("getLowerTankCapacity", lowerTank::getCapacity));
        }

        @Override
        public boolean isActive() {
            return tank.getFluidAmount() >= 1 && lowerTank.getFluidAmount() < 1000;
        }

        @Override
        protected boolean doGeneration() {
            boolean dirty = false;
            if (tank.getFluidAmount() >= 1 && lowerTank.getFluidAmount() < 1000) {
                tank.drain(1, true);
                lowerTank.fill(new FluidStack(FluidRegistry.WATER, 1), true);
                momentum += 0.003F;
                momentum *= 1.001F;
                dirty = true;
            }
            if (lowerTank.getFluidAmount() >= 1000) {
                if (world.isAirBlock(blockPos)) {
                    lowerTank.drain(1000, true);
                    world.setBlockState(blockPos, Blocks.FLOWING_WATER.getDefaultState());
                }
            }
            if (slots[0] != null && slots[1] == null) {
                if (slots[0].getItem() instanceof IFluidContainerItem) {
                    IFluidContainerItem bucket = (IFluidContainerItem)slots[0].getItem();
                    FluidStack fluid = bucket.getFluid(slots[0]);
                    if (fluid.getFluid() == FluidRegistry.WATER) {
                        tank.fill(bucket.drain(slots[0], Math.min(1000, TANK_SIZE - tank.getFluidAmount()), true), true);
                        dirty = true;
                    }
                }
                else if (FluidContainerRegistry.isFilledContainer(slots[0])) {
                    FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(slots[0]);
                    if (fluid.getFluid() == FluidRegistry.WATER && TANK_SIZE - tank.getFluidAmount() >= fluid.amount) {
                        tank.fill(fluid, true);
                        slots[1] = slots[0].getItem().getContainerItem(slots[0]);
                        slots[0] = null;
                        dirty = true;
                    }
                }
            }
            return dirty;
        }

        @Override
        public int fill(FluidStack fluidStack, boolean b) {
            return tank.fill(fluidStack, b);
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack fluidStack, boolean b) {
            return lowerTank.drain(fluidStack.amount, b);
        }

        @Nullable
        @Override
        public FluidStack drain(int i, boolean b) {
            return lowerTank.drain(i, b);
        }

        @Override
        public IFluidTankProperties[] getTankProperties() {
            return new IFluidTankProperties[0];
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            super.readFromNBT(tag);
            tank = SingleFluidTank.loadFromNBT(tag.getCompoundTag(ModReference.MACHINE_TANK));
            lowerTank = SingleFluidTank.loadFromNBT(tag.getCompoundTag(ModReference.MACHINE_TANK_2));
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            NBTTagCompound tankTag = new NBTTagCompound(), tankTag2 = new NBTTagCompound();
            tank.writeToNBT(tankTag);
            lowerTank.writeToNBT(tankTag2);
            tag.setTag(ModReference.MACHINE_TANK, tankTag);
            tag.setTag(ModReference.MACHINE_TANK_2, tankTag2);
        }

        public IFluidTank getTank() {
            return tank;
        }

        public IFluidTank getLowerTank() {
            return lowerTank;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack stack) {
            return slot == 0 && stack.getItem() instanceof IFluidContainerItem
                    || FluidContainerRegistry.isFilledContainer(stack);
        }

        @Override
        public int getField(int i) {
            return 0;
        }

        @Override
        public void setField(int i, int i1) {

        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {

        }
    }
}
