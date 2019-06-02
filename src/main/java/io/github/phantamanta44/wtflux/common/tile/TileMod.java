package io.github.phantamanta44.wtflux.common.tile;

import io.github.phantamanta44.wtflux.util.VanillaPacketDispatcher;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class TileMod extends TileEntity
{
    BlockPos blockPos = new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ());
    protected boolean init = false;
    protected abstract void tick();

    public void markForUpdate() {
        if (world != null && !world.isRemote) {
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, blockPos);
        }
        markDirty();
    }

    public boolean isInit() {
        return init;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(blockPos, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }
}
