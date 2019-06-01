package io.github.phantamanta44.wtflux;

import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * WhatTheFlux Mod!
 */
@Mod(modid = LibCore.MODID, version = LibCore.MODVER,
        dependencies = "required-after:cofhcore;" + "required-after:thermalexpansion;" + "required-after:thermalfoundation;" + "required-after:codechickenlib;" + "required-after:cofhworld;" + "required-after:redstoneflux;" + "after:before:enderio;")
public class WhatTheFlux {

    @Mod.Instance(LibCore.MODID)
    public static WhatTheFlux instance;

    @SidedProxy(clientSide = "io.github.phantamanta44.wtflux.proxy.ClientProxy", serverSide = "io.github.phantamanta44.wtflux.proxy.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Run before anything else. <s>Read your config, create blocks, items, etc, and register them with the GameRegistry</s>
     *
     * @see {@link net.minecraftforge.common.ForgeModContainer#preInit(FMLPreInitializationEvent) ForgeModContainer.preInit}
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println(LibCore.MODID + ":preInit");
        proxy.onPreInit();
    }

    /**
     * Do your mod setup. Build whatever data structures you care about. Register recipes, send FMLInterModComms messages to other mods.
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println(LibCore.MODID + ":init");
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
        System.out.println(LibCore.MODID + ":postInit");
        proxy.onPostInit();
    }
}
