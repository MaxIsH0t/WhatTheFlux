package io.github.phantamanta44.wtflux.tile;

import io.github.phantamanta44.wtflux.lib.LibNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class TileBasicInventory extends TileMod implements IInventory {

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
	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		slots[slot].stackSize -= amt;
		return slots[slot].stackSize > 0 ? slots[slot] : null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return slots[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		slots[slot] = stack;
	}

	@Override
	public String getInventoryName() {
		return "wtfinventory";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {
		// NO-OP
	}

	@Override
	public void closeInventory() {
		// NO-OP
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagList tagList = new NBTTagList();
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				slots[i].writeToNBT(itemTag);
				itemTag.setInteger(LibNBT.SLOT, i);
				tagList.appendTag(itemTag);
			}
		}
		tag.setTag(LibNBT.ITEMS, tagList);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		NBTTagList tagList = tag.getTagList(LibNBT.ITEMS, 10);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
			int slot = itemTag.getInteger(LibNBT.SLOT);
			slots[slot] = ItemStack.loadItemStackFromNBT(itemTag);
		}
		
	}
	
}
