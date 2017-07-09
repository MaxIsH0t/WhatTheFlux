package io.github.phantamanta44.wtflux.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.github.phantamanta44.wtflux.lib.LibCore;

public class WtfNet {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LibCore.MODID);

    public static void init() {
        INSTANCE.registerMessage(ClientPacketUpdateSensorParameter.Handler.class, ClientPacketUpdateSensorParameter.class, 0, Side.SERVER);
    }

}
