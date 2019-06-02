package io.github.phantamanta44.wtflux.util;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTSerializable {

    void writeToNBT(NBTTagCompound tag);

    void readFromNBT(NBTTagCompound tag);

}
