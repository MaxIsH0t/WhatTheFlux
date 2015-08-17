package io.github.phantamanta44.wtflux.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ITileItemNBT {

	public void readItemTag(NBTTagCompound tag);
	
	public void writeItemTag(NBTTagCompound tag);
	
	public ItemStack getNBTItem();
	
}
