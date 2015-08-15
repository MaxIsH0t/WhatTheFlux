package io.github.phantamanta44.wtflux.tile;

import io.github.phantamanta44.wtflux.lib.LibNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyProvider;

public class TileGenerator extends TileBasicInventory implements IEnergyProvider {
	
	public static final int[] CAP_AMOUNTS = new int[] {24000, 80000, 50000};
	
	private int energy = 0, energyMax = 24000;
	private byte gen = 0, dyn = 0, cap = 0;
	
	public TileGenerator(int meta) {
		super(0);
		gen = (byte)meta;
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
		init = true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		readItemTag(tag);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger(LibNBT.ENERGY, energy);
		tag.setInteger(LibNBT.ENERGY_MAX, energyMax);
		tag.setByte(LibNBT.GENTYPE, gen);
		tag.setByte(LibNBT.DYNTYPE, dyn);
		tag.setByte(LibNBT.CAPTYPE, cap);
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return init;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (init) {
			int transferRate = cap * 128 + 128;
			int toTransfer = Math.max(energy - transferRate, 0);
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
	public void tick() {
		boolean dirty = false;
		
		// TODO Generation procedures
		
		if (dirty)
			markForUpdate();
	}

}
