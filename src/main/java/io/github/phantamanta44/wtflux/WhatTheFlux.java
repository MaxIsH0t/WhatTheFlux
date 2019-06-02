package io.github.phantamanta44.wtflux;

import io.github.phantamanta44.wtflux.common.CommonProxy;
import io.github.phantamanta44.wtflux.util.ModReference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * WhatTheFlux Mod!
 */
@Mod(modid = ModReference.MOD_ID, version = ModReference.VERSION,
        dependencies = "required-after:cofhcore;" + "required-after:thermalexpansion;" + "required-after:thermalfoundation;" + "required-after:codechickenlib;" + "required-after:cofhworld;" + "required-after:redstoneflux;" + "after:before:enderio;")
public class WhatTheFlux {

    @Mod.Instance(ModReference.MOD_ID)
    public static WhatTheFlux instance;

    @SidedProxy(clientSide = "io.github.phantamanta44.wtflux.client.ClientProxy", serverSide = "io.github.phantamanta44.wtflux.common.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Run before anything else. <s>Read your config, create blocks, items, etc, and register them with the GameRegistry</s>
     *
     * @see {@link net.minecraftforge.common.ForgeModContainer#preInit(FMLPreInitializationEvent) ForgeModContainer.preInit}
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println(ModReference.MOD_ID + ":preInit");
        proxy.onPreInit();
    }

    /**
     * Do your mod setup. Build whatever data structures you care about. Register recipes, send FMLInterModComms messages to other mods.
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println(ModReference.MOD_ID + ":init");
        proxy.onInit();
        MinecraftForge.EVENT_BUS.register(WhatTheFlux.instance);
    }

    /**
     * Mod compatibility, or anything which depends on other mods’ init phases being finished.
     *
     * @see {@link net.minecraftforge.common.ForgeModContainer#postInit(FMLPostInitializationEvent) ForgeModContainer.postInit}
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println(ModReference.MOD_ID + ":postInit");
        proxy.onPostInit();
    }
}
