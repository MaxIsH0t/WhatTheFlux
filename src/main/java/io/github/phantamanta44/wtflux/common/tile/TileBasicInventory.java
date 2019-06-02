package io.github.phantamanta44.wtflux.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public abstract class TileBasicInventory extends TileMod implements IInventory
{
    protected ItemStack[] slots;

    public TileBasicInventory(int size) {
        super();
        slots = new ItemStack[size];
    }

    @Override
    public int getSizeInventory() {
        return slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return slots[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int i1) {
        if (slots[i] != null) {
            if (slots[i].getMaxStackSize() <= i1) {
                ItemStack stack = slots[i];
                slots[i] = null;
                markDirty();
                return stack;
            }
            ItemStack stack = slots[i].splitStack(i1);
            markDirty();
            return stack;
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        if (slots[i] != null) {
            ItemStack stack = getStackInSlot(i);
            slots[i] = null;
            return stack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        slots[i] = itemStack;
        markDirty();
    }

    @Override
    public String getName() {
        return getClass().getTypeName();
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("items")) {
            NBTTagList tagList = compound.getTagList("items", 10);
            slots = new ItemStack[slots.length];
            for (int i = 0; i< tagList.tagCount(); i++) {
                NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
                int slot = itemTag.getInteger("SlotID");
                // TODO Fix
                //slots[slot] = ItemStack.
                //slots[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }
}
