package io.github.phantamanta44.wtflux.tile;

import io.github.phantamanta44.wtflux.util.VanillaPacketDispatcher;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public abstract class TileMod extends TileEntity {

    protected boolean init = false;

    protected abstract void tick();

    public void markForUpdate() {
        if (world != null && !world.isRemote)
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, getPos().getX(), getPos().getY(), getPos().getZ());
        markDirty();
    }

    public boolean isInitialized() {
        return init;
    }

    public TileMod() {
        if (init)
            tick();
    }

    /**
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity();
        //return new SPacketUpdateTileEntity(getPos().getX(), getPos().getY(), getPos().getZ(), 0, tag);
    }**/

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return super.getUpdatePacket();
    }

    @Override
    public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
        super.onDataPacket(manager, packet);
        readFromNBT(packet.getNbtCompound());
    }


}
