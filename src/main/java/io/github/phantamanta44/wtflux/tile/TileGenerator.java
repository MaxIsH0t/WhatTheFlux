package io.github.phantamanta44.wtflux.tile;

import cofh.api.fluid.IFluidContainerItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.phantamanta44.wtflux.item.ItemReactor;
import io.github.phantamanta44.wtflux.item.WtfItems;
import io.github.phantamanta44.wtflux.lib.LibDict;
import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.lib.LibNBT;
import io.github.phantamanta44.wtflux.util.*;
import io.github.phantamanta44.wtflux.util.computercraft.CCMethod;
import io.github.phantamanta44.wtflux.util.computercraft.CCMethodBoolGetter;
import io.github.phantamanta44.wtflux.util.computercraft.CCMethodFloatGetter;
import io.github.phantamanta44.wtflux.util.computercraft.CCMethodIntGetter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class TileGenerator extends TileBasicInventory implements IEnergyProvider, IEnergyContainer, ITileItemNBT, IPeripheral {

    BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

    public static final int[] COIL_AMOUNTS = new int[] {128, 256, 512, 1024};
    public static final int[] CAP_AMOUNTS = new int[] {24000, 80000, 50000, 48000, 160000, 100000, 48000};
    public static final int[] CAP_RATES = new int[] {128, 256, 384, 256, 512, 768, -1};
    public static final float[] MELTING_POINTS = new float[] {
            449F, 1427F, 3215F, 3422F, 950F, 1085F, 1001F, 1063F,
            1593F, 328F, 512F, 1453F, 1084F, 961F, 1132F, 420F
    };
    public static final float[] RPM_CAPS = new float[] {
            12F, 34F, 32F, 60F, 24F, 20F, 20F, 20F, 36F, 15F, 15F,
            32F, 26F, 20F, 27F, 20F
    };
    public static final Class<? extends TileGenerator>[] GEN_TYPES = new Class[] {Furnace.class, Heat.class, Wind.class, Water.class, Nuke.class, Solar.class};

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
            energy = tag.getInteger(LibNBT.ENERGY);
            energyMax = tag.getInteger(LibNBT.ENERGY_MAX);
            gen = tag.getByte(LibNBT.GENTYPE);
            dyn = tag.getByte(LibNBT.DYNTYPE);
            cap = tag.getByte(LibNBT.CAPTYPE);
            casing = tag.getByte(LibNBT.CASINGTYPE);
        } else {
            energy = 0;
            energyMax = CAP_AMOUNTS[4];
            gen = (byte)getBlockMetadata();
            dyn = (byte)3;
            cap = (byte)4;
            casing = (byte)3;
        }
        markForUpdate();
        onInit();
        init = true;
    }

    @Override
    public void writeItemTag(NBTTagCompound tag) {
        tag.setInteger(LibNBT.ENERGY, energy);
        tag.setInteger(LibNBT.ENERGY_MAX, energyMax);
        tag.setByte(LibNBT.GENTYPE, gen);
        tag.setByte(LibNBT.DYNTYPE, dyn);
        tag.setByte(LibNBT.CAPTYPE, cap);
        tag.setByte(LibNBT.CASINGTYPE, casing);
    }

    @Override
    public ItemStack getNBTItem() {
        ItemStack stack = new ItemStack(blockType, 1, gen);
        NBTTagCompound tag = new NBTTagCompound();
        writeItemTag(tag);
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        readItemTag(tag);
        momentum = tag.getFloat(LibNBT.MOMENTUM);
        temp = tag.getFloat(LibNBT.TEMP);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger(LibNBT.ENERGY, energy);
        tag.setInteger(LibNBT.ENERGY_MAX, energyMax);
        tag.setByte(LibNBT.GENTYPE, gen);
        tag.setByte(LibNBT.DYNTYPE, dyn);
        tag.setByte(LibNBT.CAPTYPE, cap);
        tag.setByte(LibNBT.CASINGTYPE, casing);
        tag.setFloat(LibNBT.MOMENTUM, momentum);
        tag.setFloat(LibNBT.TEMP, temp);
        return tag;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return init;
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        if (init) {
            int toTransfer = Math.max(Math.min(Math.min(getCapRate(), maxExtract), energy), 0);
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
                    world.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getZ() + 1, getPos().getY() + 1, getPos().getZ() + 1);
                wasActive = active;
            }
        } catch (MachineOverloadException ignored) { }
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
        Map<EnumFacing, IEnergyReceiver> tiles = Maps.newHashMap();
        for (EnumFacing dir : EnumFacing.VALUES) {
            TileEntity tile = world.getTileEntity(blockPos);
            if (tile != null && tile instanceof IEnergyReceiver)
                tiles.put(dir.getOpposite(), (IEnergyReceiver)tile);
        }
        Set<Map.Entry<EnumFacing, IEnergyReceiver>> tileSet = tiles.entrySet();
        int dist = (int)Math.floor((float)Math.min(energy, getCapRate()) / (float)tileSet.size());
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
        float temp = 23.0F;
        temp *= 0.95F + 0.15F * world.getSunBrightnessFactor(1.0F);
        temp *= 0.6F + 0.75F * world.getBiome(blockPos).getTemperature(blockPos);
        temp += 8F * (Math.max(70 - getPos().getY(), 0F) / 70F);
        for (EnumFacing dir : EnumFacing.VALUES) {
            int x = getPos().getX() + getPos().getX(), y = getPos().getY() + getPos().getY(), z = getPos().getZ() + getPos().getZ();
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
        // NO-OP
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

    @Override
    public String[] getMethodNames() {
        return ccMethods.stream().map(CCMethod::getName).toArray(String[]::new);
    }

    @Override
    public Object[] callMethod(IComputerAccess com, ILuaContext ctx, int method, Object[] args) throws LuaException, InterruptedException {
        return ccMethods.get(method).execute(com, ctx, args);
    }

    @Override
    public void attach(IComputerAccess computer) {
        // NO-OP
    }

    @Override
    public void detach(IComputerAccess computer) {
        // NO-OP
    }

    @Override
    public String getType() {
        return "wtfGen" + this.getClass().getSimpleName();
    }

    @Override
    public boolean equals(IPeripheral o) {
        return this == o;
    }

    public static class Furnace extends TileGenerator {

        private int burnTime = 0;
        private int totalBurnTime = 1;

        public Furnace() {
            super(1, true);
            ccMethods.add(new CCMethodIntGetter("getBurnTime", this::getBurnTime));
            ccMethods.add(new CCMethodIntGetter("getFuelEnergy", this::getBurnTimeMax));
        }

        @Override
        public boolean isActive() {
            BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
            //return burnTime > 0 && !world.isBlockIndirectlyGettingPowered(blockPos);
            return burnTime > world.isBlockIndirectlyGettingPowered(blockPos);
        }

        @Override
        protected boolean doGeneration() {
        	//if (world.isBlockIndirectlyGettingPowered(blockPos))
        		//return false;

            boolean dirty = false;

            if (burnTime > 0) {
                burnTime--;
                temp += (float)totalBurnTime / (world.rand.nextFloat() * 2857F + 80000F);
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
            burnTime = tag.getInteger(LibNBT.BURN_TIME);
            totalBurnTime = tag.getInteger(LibNBT.BURN_TIME_TOTAL);
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            tag.setInteger(LibNBT.BURN_TIME, burnTime);
            tag.setInteger(LibNBT.BURN_TIME_TOTAL, totalBurnTime);
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
        public ItemStack removeStackFromSlot(int index) {
            return null;
        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack stack) {
            return TileEntityFurnace.isItemFuel(stack);
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {

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
                    IFluidContainerItem bucket = (IFluidContainerItem)slots[0].getItem();
                    FluidStack fluid = bucket.getFluid(slots[0]);
                    if (fluid.getFluid() == FluidRegistry.WATER) {
                        tank.fill(bucket.drain(slots[0], Math.min(1000, TANK_SIZE - tank.getFluidAmount()), true), true);
                        dirty = true;
                    }
                }
                else if (ModUtil.isFilledContainer(slots[0])) {
                    FluidStack fluid = ModUtil.getFluidForFilledItem(slots[0]);
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
        public float getPassiveTemp() {
            float orig = super.getPassiveTemp();
            if (orig > 0) {
                float yMod = 1.15F * (float)(world.getActualHeight() - getPos().getY()) / (float)world.getActualHeight();
                return orig * yMod;
            }
            return orig;
        }

        /**
        @Override
        public int fill(EnumFacing from, FluidStack stack, boolean doFill) {
            return tank.fill(stack, doFill);
        }

        @Override
        public FluidStack drain(EnumFacing from, FluidStack stack, boolean doDrain) {
            return null;
        }

        @Override
        public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
            return null;
        }

        @Override
        public boolean canFill(EnumFacing from, Fluid fluid) {
            return fluid == FluidRegistry.WATER;
        }

        @Override
        public boolean canDrain(EnumFacing from, Fluid fluid) {
            return false;
        }

        @Override
        public FluidTankInfo[] getTankInfo(EnumFacing from) {
            return new FluidTankInfo[] {new FluidTankInfo(tank)};
        }**/

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            super.readFromNBT(tag);
            tank = SingleFluidTank.loadFromNBT(tag.getCompoundTag(LibNBT.MACHINE_TANK));
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            NBTTagCompound tankTag = new NBTTagCompound();
            tank.writeToNBT(tankTag);
            tag.setTag(LibNBT.MACHINE_TANK, tankTag);
            return tankTag;
        }

        public IFluidTank getTank() {
            return tank;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public ItemStack removeStackFromSlot(int index) {
            return null;
        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack stack) {
            return slot == 0 && stack.getItem() instanceof IFluidContainerItem
                    || ModUtil.isFilledContainer(stack);
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {

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
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return null;
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }

    public static class Wind extends TileGenerator {

        public Wind() {
            super(0, true);
        }

        @Override
        public boolean isActive() {
            return getPos().getY() >= 64;
        }

        @Override
        protected boolean doGeneration() {
            if (getPos().getY() < 64)
                return false;

            float powerFactor = 0.23F * (float)Math.pow((float)(getPos().getY() - 63) / (float)(world.getActualHeight() - 63), 2);
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
        public ItemStack removeStackFromSlot(int index) {
            return null;
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {

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
            //return tank.getFluidAmount() >= 1 && lowerTank.getFluidAmount() < 1000
                    //&& !world.isBlockIndirectlyGettingPowered(blockPos);
            return tank.getFluidAmount() >= 1; //&& lowerTank.getFluidAmount();// < 1000 world.isBlockIndirectlyGettingPowered(blockPos);
        }

        @Override
        protected boolean doGeneration() {
			//if (world.isBlockIndirectlyGettingPowered(blockPos))
				//return false;

            boolean dirty = false;
            if (tank.getFluidAmount() >= 1 && lowerTank.getFluidAmount() < 1000) {
                tank.drain(1, true);
                lowerTank.fill(new FluidStack(FluidRegistry.WATER, 1), true);
                momentum += 0.003F;
                momentum *= 1.001F;
                dirty = true;
            }
            if (lowerTank.getFluidAmount() >= 1000) {
                BlockPos blockPos2 = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
                if (world.isAirBlock(blockPos2)) {
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
                else if (ModUtil.isFilledContainer(slots[0])) {
                    FluidStack fluid = ModUtil.getFluidForFilledItem(slots[0]);
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

        /**
        @Override
        public int fill(ForgeDirection from, FluidStack stack, boolean doFill) {
            if (from == ForgeDirection.DOWN)
                return 0;
            return tank.fill(stack, doFill);
        }

        @Override
        public FluidStack drain(ForgeDirection from, FluidStack stack, boolean doDrain) {
            if (from != ForgeDirection.DOWN || stack.getFluid() != FluidRegistry.WATER)
                return null;
            return lowerTank.drain(stack.amount, doDrain);
        }

        @Override
        public FluidStack drain(ForgeDirection from, int amt, boolean doDrain) {
            if (from != ForgeDirection.DOWN)
                return null;
            return lowerTank.drain(amt, doDrain);
        }

        @Override
        public boolean canFill(ForgeDirection from, Fluid fluid) {
            return fluid == FluidRegistry.WATER && from != ForgeDirection.DOWN;
        }

        @Override
        public boolean canDrain(ForgeDirection from, Fluid fluid) {
            return fluid == FluidRegistry.WATER && from == ForgeDirection.DOWN;
        }

        @Override
        public FluidTankInfo[] getTankInfo(ForgeDirection from) {
            if (from == ForgeDirection.DOWN)
                return new FluidTankInfo[] {new FluidTankInfo(lowerTank)};
            return new FluidTankInfo[] {new FluidTankInfo(tank)};
        }**/

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            super.readFromNBT(tag);
            tank = SingleFluidTank.loadFromNBT(tag.getCompoundTag(LibNBT.MACHINE_TANK));
            lowerTank = SingleFluidTank.loadFromNBT(tag.getCompoundTag(LibNBT.MACHINE_TANK_2));
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            NBTTagCompound tankTag = new NBTTagCompound(), tankTag2 = new NBTTagCompound();
            tank.writeToNBT(tankTag);
            lowerTank.writeToNBT(tankTag2);
            tag.setTag(LibNBT.MACHINE_TANK, tankTag);
            tag.setTag(LibNBT.MACHINE_TANK_2, tankTag2);
            return tankTag;
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
        public ItemStack removeStackFromSlot(int index) {
            return null;
        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack stack) {
            return slot == 0 && stack.getItem() instanceof IFluidContainerItem
                    || ModUtil.isFilledContainer(stack);
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {

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
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return null;
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }

    public static class Nuke extends TileGenerator implements IFluidHandler {

        private static final int TANK_SIZE = 16000;
        private float fuel = 0, waste = 0;
        private SingleFluidTank tank = new SingleFluidTank(FluidRegistry.WATER, TANK_SIZE);
        private String[] status = new String[] {LibLang.NG_GOOD, "", "", ""};
        private int statusIndex;
        private boolean active;

        public Nuke() {
            super(6, false);
            ccMethods.add(new CCMethodBoolGetter("isTankEmpty", () -> tank.getFluidAmount() <= 0));
            ccMethods.add(new CCMethodIntGetter("getTankAmount", tank::getFluidAmount));
            ccMethods.add(new CCMethodIntGetter("getTankCapacity", tank::getCapacity));
            ccMethods.add(new CCMethodFloatGetter("getFuel", () -> fuel));
            ccMethods.add(new CCMethodFloatGetter("getWaste", () -> waste));
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        protected boolean doGeneration() {
            Arrays.fill(status, "");
            status[0] = LibLang.NG_GOOD;
            statusIndex = 0;

            //if (world.isBlockIndirectlyGettingPowered(blockPos)) {
                //status(LibLang.NG_OFF);
                //return false;
            //}

            if (fuel > 0F) {
                if (waste < 4000F)
                    tryDoReaction();
            } else {
                active = false;
                status(LibLang.NG_NOFUEL);
            }

            if (fuel < 3000F)
                tryInjectFuel();

            if (waste >= 1000F)
                tryEjectWaste();

            if (tank.getFluidAmount() >= (temp / 4F)) {
                if (temp > getPassiveTemp()) {
                    tank.drain((int)(temp / 4F), true);
                    momentumFromHeat();
                    float tempFac = Math.max(0.01F, 2000F - temp) / 2000F;
                    temp *= (0.9988F + 0.001F * tempFac);
                }
            }
            else
                status(LibLang.NG_NOCOOL);

            if (slots[3] != null && slots[4] == null) {
                if (slots[3].getItem() instanceof IFluidContainerItem) {
                    IFluidContainerItem bucket = (IFluidContainerItem)slots[3].getItem();
                    FluidStack fluid = bucket.getFluid(slots[3]);
                    if (fluid.getFluid() == FluidRegistry.WATER)
                        tank.fill(bucket.drain(slots[3], Math.min(1000, TANK_SIZE - tank.getFluidAmount()), true), true);
                } else if (ModUtil.isFilledContainer(slots[3])) {
                    FluidStack fluid = ModUtil.getFluidForFilledItem(slots[3]);
                    if (fluid.getFluid() == FluidRegistry.WATER && TANK_SIZE - tank.getFluidAmount() >= fluid.amount) {
                        tank.fill(fluid, true);
                        slots[4] = slots[3].getItem().getContainerItem(slots[3]);
                        slots[3] = null;
                    }
                }
            }

            if (energy >= energyMax)
                status(LibLang.NG_FULLBUF);
            return true;
        }

        private void tryDoReaction() {
            if (slots[2] != null) {
                if (slots[2].getItem() == WtfItems.itemRct && slots[2].getItemDamage() == ItemReactor.CONTROL_ROD) {
                    float fuelCost = temp / 35F;
                    if (fuel >= fuelCost) {
                        if (4000F - waste >= fuelCost) {
                            slots[2] = ((ItemReactor)WtfItems.itemRct).decrementUses(slots[2]);
                            fuel -= fuelCost;
                            waste += fuelCost;
                            float tempFac = Math.max(0.01F, 30000F - temp) / 15000F;
                            temp += (16F + world.rand.nextFloat() * 24F) * Math.max(0.001F, tempFac);
                            temp += world.rand.nextGaussian() * world.rand.nextFloat() * 100F * tempFac;
                            active = true;
                        } else {
                            status(LibLang.NG_FULLWASTE);
                            active = false;
                        }
                    } else {
                        status(LibLang.NG_NOFUEL);
                        active = false;
                    }
                    return;
                }
            }
            status(LibLang.NG_NOCTRL);
            active = false;
        }

        private void tryInjectFuel() {
            boolean flag1 = false, flag2 = false;
            if (slots[1] != null) {
                if (slots[1].getItem() == WtfItems.itemRct && slots[1].getItemDamage() == ItemReactor.BLASTER)
                    flag1 = true;
            }

            if (slots[0] != null) {
                if (LibDict.matches(slots[0], LibDict.INGOT_URAN))
                    flag2 = true;
            }

            if (flag1) {
                if (flag2) {
                    slots[1] = ((ItemReactor)WtfItems.itemRct).decrementUses(slots[1]);
                    decrStackSize(0, 1);
                    fuel += 1000F;
                }
            }
            else
                status(LibLang.NG_NOHOW);
        }

        private void tryEjectWaste() {
            if (slots[5] == null) {
                waste -= 1000F;
                slots[5] = new ItemStack(WtfItems.itemRct, 1, ItemReactor.WASTE);
            }
            else if (slots[5].getItem() == WtfItems.itemRct && slots[5].getItemDamage() == ItemReactor.WASTE && slots[5].getMaxStackSize() < slots[5].getMaxStackSize()) {
                waste -= 1000F;
                //slots[5].stackSize++;
            }
            else
                status(LibLang.NG_FULLWASTE);
        }

        /**
        @Override
        public int fill(ForgeDirection from, FluidStack stack, boolean doFill) {
            return tank.fill(stack, doFill);
        }

        @Override
        public FluidStack drain(ForgeDirection from, FluidStack stack, boolean doDrain) {
            return null;
        }

        @Override
        public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
            return null;
        }

        @Override
        public boolean canFill(ForgeDirection from, Fluid fluid) {
            return fluid == FluidRegistry.WATER;
        }

        @Override
        public boolean canDrain(ForgeDirection from, Fluid fluid) {
            return false;
        }

        @Override
        public FluidTankInfo[] getTankInfo(ForgeDirection from) {
            return new FluidTankInfo[] {new FluidTankInfo(tank)};
        }**/

        @Override
        public void doMeltdown() {
            world.createExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), 21.67F, true);
            world.setBlockState(blockPos, (IBlockState) Block.getBlockFromName("ThermalFoundation:FluidPyrotheum"));
            // TODO Radioactive harm of some kind
        }

        @Override
        public void doOverspeed() {
            doMeltdown();
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            super.readFromNBT(tag);
            fuel = tag.getFloat(LibNBT.MACHINE_FUEL);
            waste = tag.getFloat(LibNBT.MACHINE_WASTE);
            tank = SingleFluidTank.loadFromNBT(tag.getCompoundTag(LibNBT.MACHINE_TANK));
            active = tag.getBoolean(LibNBT.ACTIVE);
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            NBTTagCompound tankTag = new NBTTagCompound();
            tank.writeToNBT(tankTag);
            tag.setFloat(LibNBT.MACHINE_FUEL, fuel);
            tag.setFloat(LibNBT.MACHINE_WASTE, waste);
            tag.setTag(LibNBT.MACHINE_TANK, tankTag);
            tag.setBoolean(LibNBT.ACTIVE, active);
            return tankTag;
        }

        public IFluidTank getTank() {
            return tank;
        }

        public float getFuel() {
            return fuel;
        }

        public float getWaste() {
            return waste;
        }

        public String[] getStatus() {
            return status;
        }

        public void status(String s) {
            if (statusIndex < status.length)
                status[statusIndex++] = s;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public ItemStack removeStackFromSlot(int index) {
            return null;
        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack stack) {
            switch (slot) {
                case 0: // fuel
                    return LibDict.matches(stack, LibDict.INGOT_URAN);
                case 1: // neutron howitzer
                    return stack.getItem() == WtfItems.itemRct && stack.getItemDamage() == ItemReactor.BLASTER;
                case 2: // control rod
                    return stack.getItem() == WtfItems.itemRct && stack.getItemDamage() == ItemReactor.CONTROL_ROD;
                case 3: // fluid in
                    return stack.getItem() instanceof IFluidContainerItem || ModUtil.isFilledContainer(stack);
                case 4: // fluid out
                case 5: // waste
                    return false;
            }
            return true;
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {

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
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return null;
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }

    public static class Solar extends TileGenerator implements IFluidHandler {

        private static final int TANK_SIZE = 10000;
        private SingleFluidTank tank = new SingleFluidTank(FluidRegistry.WATER, TANK_SIZE);

        public Solar() {
            super(2, true);
            ccMethods.add(new CCMethodBoolGetter("isTankEmpty", () -> tank.getFluidAmount() <= 0));
            ccMethods.add(new CCMethodIntGetter("getTankAmount", tank::getFluidAmount));
            ccMethods.add(new CCMethodIntGetter("getTankCapacity", tank::getCapacity));
        }

        @Override
        public boolean isActive() {
            return world.isDaytime() && !world.provider.hasSkyLight() && world.canSeeSky(blockPos);
        }

        @Override
        protected boolean doGeneration() {
            boolean dirty = false;
            if (slots[0] != null && slots[1] == null) {
                if (slots[0].getItem() instanceof IFluidContainerItem) {
                    IFluidContainerItem bucket = (IFluidContainerItem)slots[0].getItem();
                    FluidStack fluid = bucket.getFluid(slots[0]);
                    if (fluid.getFluid() == FluidRegistry.WATER) {
                        tank.fill(bucket.drain(slots[0], Math.min(1000, TANK_SIZE - tank.getFluidAmount()), true), true);
                        dirty = true;
                    }
                }
                else if (ModUtil.isFilledContainer(slots[0])) {
                    FluidStack fluid = ModUtil.getFluidForFilledItem(slots[0]);
                    if (fluid.getFluid() == FluidRegistry.WATER && TANK_SIZE - tank.getFluidAmount() >= fluid.amount) {
                        tank.fill(fluid, true);
                        slots[1] = slots[0].getItem().getContainerItem(slots[0]);
                        slots[0] = null;
                        dirty = true;
                    }
                }
            }
            if (world.isDaytime() && !world.provider.hasSkyLight() && world.canSeeSky(blockPos)) {
                int time = (int)(world.getWorldTime() % 24000L);
                if (time < 13000 || time > 22650) {
                    float add = (float)Math.sin(MathUtil.timeToRad(time)) * 0.1F;
                    temp += (2F - (temp / 200F)) * add;
                    dirty = true;
                }
            }
            if (tank.getFluidAmount() > 0) {
                int toConsume = Math.max(MathHelper.floor(Math.pow(temp - 100F, 0.5F)), 0);
                if (toConsume > 0) {
                    int drained = tank.drain(toConsume, true).amount;
                    momentum += (float)drained / 100F;
                    dirty = true;
                }
            }
            return dirty;
        }

        @Override
        public float getPassiveTemp() {
            float orig = super.getPassiveTemp();
            if (orig > 0) {
                float yMod = 1.15F * (float)(world.getActualHeight() - getPos().getY()) / (float)world.getActualHeight();
                return orig * yMod;
            }
            return orig;
        }

        /**
        @Override
        public int fill(EnumFacing from, FluidStack stack, boolean doFill) {
            return tank.fill(stack, doFill);
        }

        @Override
        public FluidStack drain(EnumFacing from, FluidStack stack, boolean doDrain) {
            return null;
        }

        @Override
        public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
            return null;
        }

        @Override
        public boolean canFill(EnumFacing from, Fluid fluid) {
            return fluid == FluidRegistry.WATER;
        }

        @Override
        public boolean canDrain(EnumFacing from, Fluid fluid) {
            return false;
        }

        @Override
        public FluidTankInfo[] getTankInfo(EnumFacing from) {
            return new FluidTankInfo[] {new FluidTankInfo(tank)};
        }**/

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            super.readFromNBT(tag);
            tank = SingleFluidTank.loadFromNBT(tag.getCompoundTag(LibNBT.MACHINE_TANK));
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            NBTTagCompound tankTag = new NBTTagCompound();
            tank.writeToNBT(tankTag);
            tag.setTag(LibNBT.MACHINE_TANK, tankTag);
            return tankTag;
        }

        public IFluidTank getTank() {
            return tank;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public ItemStack removeStackFromSlot(int index) {
            return null;
        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack stack) {
            return slot == 0 && (stack.getItem() instanceof IFluidContainerItem
                    || ModUtil.isFilledContainer(stack));
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {

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
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return null;
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }

}
