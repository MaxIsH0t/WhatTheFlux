package io.github.phantamanta44.wtflux.network;

import io.github.phantamanta44.wtflux.lib.LibCore;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class WtfNet {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LibCore.MODID);

    public static void init() {
        INSTANCE.registerMessage(ClientPacketUpdateSensorParameter.Handler.class, ClientPacketUpdateSensorParameter.class, 0, Side.SERVER);
    }

}
