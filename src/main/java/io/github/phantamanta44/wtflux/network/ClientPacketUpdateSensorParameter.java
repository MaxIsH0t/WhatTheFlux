package io.github.phantamanta44.wtflux.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.github.phantamanta44.wtflux.inventory.ContainerDummy;
import io.github.phantamanta44.wtflux.tile.TileSensor;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class ClientPacketUpdateSensorParameter implements IMessage {

    private byte type;
    private Object value;

    @Deprecated
    public ClientPacketUpdateSensorParameter() {
        // NO-OP
    }

    public ClientPacketUpdateSensorParameter(int parameter) {
        this.type = (byte)0;
        this.value = parameter;
    }

    public ClientPacketUpdateSensorParameter(float parameter) {
        this.type = (byte)1;
        this.value = parameter;
    }

    public int getInt() {
        return (int)value;
    }

    public float getFloat() {
        return (float)value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        switch (type = buf.readByte()) {
            case 0:
                value = buf.readInt();
                break;
            case 1:
                value = buf.readFloat();
                break;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(type);
        switch (type) {
            case 0:
                buf.writeInt((int)value);
                break;
            case 1:
                buf.writeFloat((float)value);
                break;
        }
    }

    public static class Handler implements IMessageHandler<ClientPacketUpdateSensorParameter, IMessage> {

        @Override
        public IMessage onMessage(ClientPacketUpdateSensorParameter msg, MessageContext ctx) {
            Container cont = ctx.getServerHandler().playerEntity.openContainer;
            if (cont != null && cont instanceof ContainerDummy) {
                TileEntity tile = ((ContainerDummy)cont).getWrappedTile();
                if (tile != null && tile instanceof TileSensor) {
                    switch (msg.type) {
                        case 0:
                            if (tile instanceof TileSensor.Energy)
                                ((TileSensor)tile).setParameter(msg.getInt());
                            break;
                        case 1:
                            if (tile instanceof TileSensor.RPM || tile instanceof TileSensor.Temperature)
                                ((TileSensor)tile).setParameter(msg.getFloat());
                            break;
                    }
                }
            }
            return null;
        }

    }

}
