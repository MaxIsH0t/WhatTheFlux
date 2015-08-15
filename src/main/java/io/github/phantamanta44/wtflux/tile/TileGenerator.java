package io.github.phantamanta44.wtflux.tile;

import io.github.phantamanta44.wtflux.lib.LibNBT;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

import com.google.common.collect.Maps;

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
	public void tick() {
		boolean dirty = false;
		
		// TODO Generation procedures
		energy += 10;
		
		Map<ForgeDirection, IEnergyReceiver> tiles = Maps.newHashMap();
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if (tile != null && tile instanceof IEnergyReceiver) {
				ForgeDirection from = dir.getOpposite();
				tiles.put(dir.getOpposite(), (IEnergyReceiver)tile);
				dirty = true;
			}
		}
		Set<Entry<ForgeDirection, IEnergyReceiver>> tileSet = tiles.entrySet();
		int dist = (int)Math.floor((float)Math.min(energy, getTransferRate()) / (float)tileSet.size());
		for (Entry<ForgeDirection, IEnergyReceiver> tile : tileSet) {
			int v = ((IEnergyReceiver)tile.getValue()).receiveEnergy(tile.getKey(), dist, false);
			energy -= v;
		}
		
		if (dirty)
			markForUpdate();
	}
	
	public int getTransferRate() {
		return cap * 128 + 128;
	}

}
