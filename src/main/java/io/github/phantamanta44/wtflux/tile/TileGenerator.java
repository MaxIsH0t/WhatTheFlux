package io.github.phantamanta44.wtflux.tile;

import io.github.phantamanta44.wtflux.item.ItemReactor;
import io.github.phantamanta44.wtflux.item.WtfItems;
import io.github.phantamanta44.wtflux.lib.LibNBT;
import io.github.phantamanta44.wtflux.util.MathUtil;
import io.github.phantamanta44.wtflux.util.SingleFluidTank;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

import com.google.common.collect.Maps;

public abstract class TileGenerator extends TileBasicInventory implements IEnergyProvider {
	
	public static final int[] COIL_AMOUNTS = new int[] {128, 256, 512};
	public static final int[] CAP_AMOUNTS = new int[] {24000, 80000, 50000};
	public static final Class<? extends TileGenerator>[] GEN_TYPES = new Class[] {Furnace.class, Heat.class, Wind.class, Water.class, Nuke.class, Solar.class};
	
	protected int energy = 0, energyMax = 24000;
	protected byte gen = 0, dyn = 0, cap = 0;
	protected float momentum = 0F, temp = 23.0F;
	
	public TileGenerator(int slots) {
		super(slots);
	}
	
	@Override
	public void readItemTag(NBTTagCompound tag) {
		if (tag != null) {
			energy = tag.getInteger(LibNBT.ENERGY);
			energyMax = tag.getInteger(LibNBT.ENERGY_MAX);
			gen = tag.getByte(LibNBT.GENTYPE);
			dyn = tag.getByte(LibNBT.DYNTYPE);
			cap = tag.getByte(LibNBT.CAPTYPE);
			markForUpdate();
		}
		onInit();
		init = true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		readItemTag(tag);
		momentum = tag.getFloat(LibNBT.MOMENTUM);
		temp = tag.getFloat(LibNBT.TEMP);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger(LibNBT.ENERGY, energy);
		tag.setInteger(LibNBT.ENERGY_MAX, energyMax);
		tag.setByte(LibNBT.GENTYPE, gen);
		tag.setByte(LibNBT.DYNTYPE, dyn);
		tag.setByte(LibNBT.CAPTYPE, cap);
		tag.setFloat(LibNBT.MOMENTUM, momentum);
		tag.setFloat(LibNBT.TEMP, temp);
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return init;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (init) {
			int toTransfer = Math.max(Math.min(Math.min(getTransferRate(), maxExtract), energy), 0);
			if (toTransfer > 0 && !simulate) {
				energy -= toTransfer;
				markForUpdate();
			}
			return toTransfer;
		}
		return 0;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energy;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return energyMax;
	}
	
	
	@Override
	protected void tick() {
		boolean dirty = false;
		
		dirty |= doGeneration();
		dirty |= simulateInduction();
		dirty |= distributeEnergy();
		dirty |= simulateHeat();
		
		energy = Math.max(0, Math.min(energy, energyMax));
		
		if (dirty)
			markForUpdate();
	}
	
	public int getTransferRate() {
		return cap * 128 + 128;
	}
	
	public boolean simulateInduction() {
		float voltage = MathUtil.voltageFromFlux(momentum, COIL_AMOUNTS[dyn]), resist = MathUtil.resistanceFromHeat(temp);
		int current = (int)(voltage / resist);
		energy += current;
		float momentumLossFactor = (0.997F - momentum / 2400F);
		momentum *= momentumLossFactor;
		temp += Math.max(momentum / 4200F, 0);
		return true;
	}
	
	public boolean distributeEnergy() {
		Map<ForgeDirection, IEnergyReceiver> tiles = Maps.newHashMap();
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if (tile != null && tile instanceof IEnergyReceiver) {
				ForgeDirection from = dir.getOpposite();
				tiles.put(dir.getOpposite(), (IEnergyReceiver)tile);
			}
		}
		Set<Entry<ForgeDirection, IEnergyReceiver>> tileSet = tiles.entrySet();
		int dist = (int)Math.floor((float)Math.min(energy, getTransferRate()) / (float)tileSet.size());
		for (Entry<ForgeDirection, IEnergyReceiver> tile : tileSet) {
			int v = ((IEnergyReceiver)tile.getValue()).receiveEnergy(tile.getKey(), dist, false);
			energy -= v;
		}
		return !tileSet.isEmpty();
	}
	
	public boolean simulateHeat() {
		float prevTemp = temp;
		temp += (0.01F + worldObj.rand.nextFloat()) * 0.001F * (getPassiveTemp() - temp);
		// TODO React explosively to overheating
		return prevTemp != temp;
	}
	
	public float getPassiveTemp() {
		float temp = 23.0F;
		temp *= 0.95F + 0.15F * worldObj.getSunBrightnessFactor(1.0F);
		temp *= 0.6F + 0.75F * worldObj.getBiomeGenForCoords(xCoord, zCoord).getFloatTemperature(xCoord, yCoord, zCoord);
		temp += 8F * (Math.max(70 - yCoord, 0F) / 70F);
		// TODO Check around for nearby hot liquids/blocks
		return temp;
	}
	
	protected void onInit() {
		// NO-OP
	}
	
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
	
	public static class Furnace extends TileGenerator {

		private int burnTime = 0;
		private int totalBurnTime = 0;
		
		public Furnace() {
			super(1);
		}

		@Override
		protected boolean doGeneration() {
			boolean dirty = false;
			
			if (burnTime > 0) {
				burnTime--;
				temp += (float)totalBurnTime / (worldObj.rand.nextFloat() * 2857F + 80000F);
				dirty = true;
			}
			
			dirty |= momentumFromHeat();

			if (burnTime == 0 && slots[0] != null) {
				int fuelValue = TileEntityFurnace.getItemBurnTime(slots[0]);
				if (fuelValue > 0) {
					slots[0].stackSize--;
					if (slots[0].stackSize == 0)
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
		public void writeToNBT(NBTTagCompound tag) {
			super.writeToNBT(tag);
			tag.setInteger(LibNBT.BURN_TIME, burnTime);
			tag.setInteger(LibNBT.BURN_TIME_TOTAL, totalBurnTime);
		}
		
	}
	
	public static class Heat extends TileGenerator implements IFluidHandler {
		
		private static final int TANK_SIZE = 10000;
		private SingleFluidTank tank = new SingleFluidTank(FluidRegistry.WATER, TANK_SIZE);
		
		public Heat() {
			super(2);
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
		public float getPassiveTemp() {
			float orig = super.getPassiveTemp();
			float yMod = 7.9F * (float)(worldObj.getActualHeight() - yCoord) / (float)worldObj.getActualHeight();
			return orig * yMod;
		}

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
		}
		
		@Override
		public void readFromNBT(NBTTagCompound tag) {
			super.readFromNBT(tag);
			tank = SingleFluidTank.loadFromNBT(tag.getCompoundTag(LibNBT.MACHINE_TANK));
		}
		
		@Override
		public void writeToNBT(NBTTagCompound tag) {
			super.writeToNBT(tag);
			NBTTagCompound tankTag = new NBTTagCompound();
			tank.writeToNBT(tankTag);
			tag.setTag(LibNBT.MACHINE_TANK, tankTag);
		}
		
	}

	public static class Wind extends TileGenerator {
		
		public Wind() {
			super(0);
		}
		
		@Override
		protected boolean doGeneration() {
			if (yCoord < 64)
				return false;
			
			float powerFactor = 0.23F * (float)Math.pow((float)(yCoord - 63) / (float)(worldObj.getActualHeight() - 63), 2);
			if (worldObj.isThundering())
				powerFactor *= 1.5F;
			for (int x = -3; x <= 3; x++) {
				for (int z = -3; z <= 3; z++) {
					for (int y = -1; y <= 1; y++) {
						if (!worldObj.isAirBlock(xCoord + x, yCoord + y, zCoord + z)) {
							if (worldObj.isBlockNormalCubeDefault(xCoord + x, yCoord + y, zCoord + z, true))
								powerFactor *= 0.8F;
							else
								powerFactor *= 0.5F;
						}
					}
				}
			}
			momentum += powerFactor;
			return true;
		}
		
	}
	
	public static class Water extends TileGenerator implements IFluidHandler {
		
		private static final int TANK_SIZE = 4000, LWR_SIZE = 1000;
		private SingleFluidTank tank = new SingleFluidTank(FluidRegistry.WATER, TANK_SIZE);
		private SingleFluidTank lowerTank = new SingleFluidTank(FluidRegistry.WATER, LWR_SIZE);
		
		public Water() {
			super(2);
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
				if (worldObj.isAirBlock(xCoord, yCoord - 1, zCoord) && yCoord > 1) {
					lowerTank.drain(1000, true);
					worldObj.setBlock(xCoord, yCoord - 1, zCoord, Blocks.flowing_water);
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
		}
		
		@Override
		public void readFromNBT(NBTTagCompound tag) {
			super.readFromNBT(tag);
			tank = SingleFluidTank.loadFromNBT(tag.getCompoundTag(LibNBT.MACHINE_TANK));
			lowerTank = SingleFluidTank.loadFromNBT(tag.getCompoundTag(LibNBT.MACHINE_TANK_2));
		}
		
		@Override
		public void writeToNBT(NBTTagCompound tag) {
			super.writeToNBT(tag);
			NBTTagCompound tankTag = new NBTTagCompound(), tankTag2 = new NBTTagCompound();
			tank.writeToNBT(tankTag);
			lowerTank.writeToNBT(tankTag2);
			tag.setTag(LibNBT.MACHINE_TANK, tankTag);
			tag.setTag(LibNBT.MACHINE_TANK_2, tankTag2);
		}
		
	}
	
	public static class Nuke extends TileGenerator implements IFluidHandler {
		
		private static final int TANK_SIZE = 16000;
		private int fuel = 0, waste = 0;
		private SingleFluidTank tank = new SingleFluidTank(FluidRegistry.WATER, TANK_SIZE);
		
		public Nuke() {
			super(6);
		}
		
		@Override
		protected boolean doGeneration() {
			if (fuel > 0) {
				if (waste < 1000)
					tryDoReaction();
			}
			else
				tryInjectFuel();
			
			if (waste >= 1000)
				tryEjectWaste();
			
			if (tank.getFluidAmount() >= temp && temp > getPassiveTemp()) {
				tank.drain((int)temp, true);
				momentumFromHeat();
				temp *= 0.9F + 0.1F * (Math.max(0, 2000 - temp) / 2000);
			}
			
			if (slots[3] != null && slots[4] == null) {
				if (slots[3].getItem() instanceof IFluidContainerItem) {
					IFluidContainerItem bucket = (IFluidContainerItem)slots[3].getItem();
					FluidStack fluid = bucket.getFluid(slots[3]);
					if (fluid.getFluid() == FluidRegistry.WATER)
						tank.fill(bucket.drain(slots[3], Math.min(1000, TANK_SIZE - tank.getFluidAmount()), true), true);
				}
				else if (FluidContainerRegistry.isFilledContainer(slots[3])) {
					FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(slots[3]);
					if (fluid.getFluid() == FluidRegistry.WATER && TANK_SIZE - tank.getFluidAmount() >= fluid.amount) {
						tank.fill(fluid, true);
						slots[4] = slots[3].getItem().getContainerItem(slots[3]);
						slots[3] = null;
					}
				}
			}
			
			return true;
		}
		
		private void tryDoReaction() {
			if (slots[2] != null) {
				if (slots[2].getItem() == WtfItems.itemRct && slots[2].getItemDamage() == ItemReactor.CONTROL_ROD) {
					((ItemReactor)WtfItems.itemRct).decrementUses(slots[2]);
					fuel--;
					waste++;
					temp += (0.5 + worldObj.rand.nextFloat() * 1.5);
				}
			}
		}
		
		private void tryInjectFuel() {
			if (slots[1] != null) {
				if (slots[1].getItem() == WtfItems.itemRct && slots[1].getItemDamage() == ItemReactor.BLASTER) {
					((ItemReactor)WtfItems.itemRct).decrementUses(slots[1]);
					fuel = 1000;
				}
			}
		}
		
		private void tryEjectWaste() {
			if (slots[5] == null) {
				waste -= 1000;
				slots[5] = new ItemStack(WtfItems.itemRct, 1, ItemReactor.WASTE);
			}
			else if (slots[5].getItem() == WtfItems.itemRct && slots[5].getItemDamage() == ItemReactor.WASTE && slots[5].stackSize < slots[5].getMaxStackSize()) {
				waste -= 1000;
				slots[5].stackSize++;
			}
		}
		
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
		}
		
		@Override
		public void readFromNBT(NBTTagCompound tag) {
			super.readFromNBT(tag);
			fuel = tag.getInteger(LibNBT.MACHINE_FUEL);
			waste = tag.getInteger(LibNBT.MACHINE_WASTE);
			tank = SingleFluidTank.loadFromNBT(tag.getCompoundTag(LibNBT.MACHINE_TANK));
		}
		
		@Override
		public void writeToNBT(NBTTagCompound tag) {
			super.writeToNBT(tag);
			NBTTagCompound tankTag = new NBTTagCompound();
			tank.writeToNBT(tankTag);
			tag.setInteger(LibNBT.MACHINE_FUEL, fuel);
			tag.setInteger(LibNBT.MACHINE_WASTE, waste);
			tag.setTag(LibNBT.MACHINE_TANK, tankTag);
		}
		
	}
	
	public static class Solar extends TileGenerator {
		
		public Solar() {
			super(0);
		}
		
		@Override
		protected boolean doGeneration() {
			boolean dirty = false;
			if (worldObj.isDaytime() && !worldObj.provider.hasNoSky && worldObj.canBlockSeeTheSky(xCoord, yCoord + 1, zCoord)) {
				int time = (int)(worldObj.getWorldTime() % 24000L);
				if (time < 13000 || time > 22650) {
					float add = (float)Math.sin(MathUtil.timeToRad(time)) * 0.1F;
					temp += (2F - ((float)temp / 200F) * 1F) * add;
					dirty = true;
				}
			}
			dirty |= momentumFromHeat();
			return dirty;
		}
		
	}
}
