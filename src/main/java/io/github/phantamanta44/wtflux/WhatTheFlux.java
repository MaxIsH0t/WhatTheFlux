package io.github.phantamanta44.wtflux;

import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LibCore.MODID, version = LibCore.MODVER)
public class WhatTheFlux {

    @Instance(LibCore.MODID)
    public static WhatTheFlux instance;

    @SidedProxy(clientSide = "io.github.phantamanta44.wtflux.proxy.ClientProxy", serverSide = "io.github.phantamanta44.wtflux.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final CreativeTabs tabWTF = new CreativeTabWtf();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.onPreInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.onInit();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.onPostInit();
    }

}
