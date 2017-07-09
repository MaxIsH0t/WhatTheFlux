package io.github.phantamanta44.wtflux.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerDummy extends ContainerMod {

    private final TileEntity tile;

    public ContainerDummy(InventoryPlayer ipl, TileEntity tile) {
        this.tile = tile;
        addPlayerInventory(ipl);
    }

    public TileEntity getWrappedTile() {
        return tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player.getDistanceSq(tile.xCoord + 0.5D, tile.yCoord + 0.5D, tile.zCoord + 0.5D) < 64D;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        return null;
    }

}
