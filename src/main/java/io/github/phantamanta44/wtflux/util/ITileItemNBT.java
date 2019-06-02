package io.github.phantamanta44.wtflux.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ITileItemNBT {

    void readItemTag(NBTTagCompound tag);

    void writeItemTag(NBTTagCompound tag);

    ItemStack getNBTItem();

}