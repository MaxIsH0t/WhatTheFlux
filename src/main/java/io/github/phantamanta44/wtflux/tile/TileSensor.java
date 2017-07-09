package io.github.phantamanta44.wtflux.tile;

import cofh.api.tileentity.IReconfigurableFacing;
import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.lib.LibNBT;
import io.github.phantamanta44.wtflux.network.ClientPacketUpdateSensorParameter;
import io.github.phantamanta44.wtflux.network.WtfNet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class TileSensor extends TileMod implements IReconfigurableFacing {

    public static final Class<? extends TileMod>[] SENSOR_TYPES = new Class[] {Temperature.class, Energy.class, RPM.class};

    public static TileEntity getAppropriateTile(int meta) {
        try {
            return SENSOR_TYPES[meta].getConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getCause());
        } catch (Exception e) {
            return null;
        }
    }

    private ForgeDirection facing;
    private boolean tripped;

    public TileSensor() {
        this.facing = ForgeDirection.DOWN;
        this.tripped = false;
        init = true;
    }

    @Override
    protected void tick() {
        TileGenerator tile = getObservedTile();
        if (tile != null) {
            boolean trippedThisTick = exceedsThreshold(tile);
            if (tripped != trippedThisTick) {
                tripped = trippedThisTick;
                worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
                worldObj.notifyBlockChange(xCoord, yCoord, zCoord, blockType);
                markForUpdate();
            }
        } else if (tripped) {
            tripped = false;
            worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            worldObj.notifyBlockChange(xCoord, yCoord, zCoord, blockType);
            markForUpdate();
        }
    }

    abstract boolean exceedsThreshold(TileGenerator tile);

    public abstract Predicate<String> getConfigValidator();

    public abstract Consumer<String> getConfigCallback();

    public abstract String getConfigValue();

    public abstract String getGuiName();

    public abstract void setParameter(Object value);

    public int getOppositeFace() {
        return facing.getOpposite().ordinal();
    }

    @Override
    public int getFacing() {
        return facing.ordinal();
    }

    @Override
    public boolean allowYAxisFacing() {
        return true;
    }

    @Override
    public boolean rotateBlock() {
        facing = ForgeDirection.getOrientation((facing.ordinal() + 1) % 6);
        return true;
    }

    @Override
    public boolean setFacing(int side) {
        facing = ForgeDirection.getOrientation(side);
        return true;
    }

    public TileGenerator getObservedTile() {
        TileEntity tile = worldObj.getTileEntity(xCoord + facing.offsetX, yCoord + facing.offsetY, zCoord + facing.offsetZ);
        return tile != null && tile instanceof TileGenerator ? (TileGenerator)tile : null;
    }

    public boolean isTripped() {
        return tripped;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        facing = ForgeDirection.getOrientation(nbt.getInteger(LibNBT.FACING));
        tripped = nbt.getBoolean(LibNBT.ACTIVE);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger(LibNBT.FACING, facing.ordinal());
        nbt.setBoolean(LibNBT.ACTIVE, tripped);
    }

    public static class Temperature extends TileSensor {

        private float threshold = 0F;

        @Override
        boolean exceedsThreshold(TileGenerator tile) {
            return tile.getTemp() >= threshold;
        }

        @Override
        public Predicate<String> getConfigValidator() {
            return s -> {
                try {
                    return !Float.isNaN(Float.parseFloat(s));
                } catch (NumberFormatException e) {
                    return false;
                }
            };
        }

        @Override
        public Consumer<String> getConfigCallback() {
            return s -> {
                threshold = Float.parseFloat(s);
                WtfNet.INSTANCE.sendToServer(new ClientPacketUpdateSensorParameter(threshold));
            };
        }

        @Override
        public String getConfigValue() {
            return Float.toString(threshold);
        }

        @Override
        public String getGuiName() {
            return LibLang.GUI_SENSOR_TEMP;
        }

        @Override
        public void setParameter(Object value) {
            this.threshold = (float)value;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            super.readFromNBT(nbt);
            threshold = nbt.getFloat(LibNBT.THRESHOLD);
        }

        @Override
        public void writeToNBT(NBTTagCompound nbt) {
            super.writeToNBT(nbt);
            nbt.setFloat(LibNBT.THRESHOLD, threshold);
        }
        
    }

    public static class Energy extends TileSensor {

        private int threshold = 0;

        @Override
        boolean exceedsThreshold(TileGenerator tile) {
            return tile.getEnergyStored() >= threshold;
        }

        @Override
        public Predicate<String> getConfigValidator() {
            return s -> {
                try {
                    return Integer.parseInt(s) >= 0;
                } catch (NumberFormatException e) {
                    return false;
                }
            };
        }

        @Override
        public Consumer<String> getConfigCallback() {
            return s -> {
                threshold = Integer.parseInt(s);
                WtfNet.INSTANCE.sendToServer(new ClientPacketUpdateSensorParameter(threshold));
            };
        }

        @Override
        public String getConfigValue() {
            return Integer.toString(threshold);
        }

        @Override
        public String getGuiName() {
            return LibLang.GUI_SENSOR_ENERGY;
        }

        @Override
        public void setParameter(Object value) {
            this.threshold = (int)value;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            super.readFromNBT(nbt);
            threshold = nbt.getInteger(LibNBT.THRESHOLD);
        }

        @Override
        public void writeToNBT(NBTTagCompound nbt) {
            super.writeToNBT(nbt);
            nbt.setInteger(LibNBT.THRESHOLD, threshold);
        }

    }

    public static class RPM extends TileSensor {

        private float threshold = 0F;

        @Override
        boolean exceedsThreshold(TileGenerator tile) {
            return tile.getMomentum() >= threshold;
        }

        @Override
        public Predicate<String> getConfigValidator() {
            return s -> {
                try {
                    float f = Float.parseFloat(s);
                    return !Float.isNaN(f) && f >= 0;
                } catch (NumberFormatException e) {
                    return false;
                }
            };
        }

        @Override
        public Consumer<String> getConfigCallback() {
            return s -> {
                threshold = Float.parseFloat(s);
                WtfNet.INSTANCE.sendToServer(new ClientPacketUpdateSensorParameter(threshold));
            };
        }

        @Override
        public String getConfigValue() {
            return Float.toString(threshold);
        }

        @Override
        public String getGuiName() {
            return LibLang.GUI_SENSOR_RPM;
        }

        @Override
        public void setParameter(Object value) {
            this.threshold = (float)value;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            super.readFromNBT(nbt);
            threshold = nbt.getFloat(LibNBT.THRESHOLD);
        }

        @Override
        public void writeToNBT(NBTTagCompound nbt) {
            super.writeToNBT(nbt);
            nbt.setFloat(LibNBT.THRESHOLD, threshold);
        }

    }

}
