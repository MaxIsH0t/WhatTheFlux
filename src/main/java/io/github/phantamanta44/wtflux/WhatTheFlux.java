package io.github.phantamanta44.wtflux;

import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LibCore.MODID, version = LibCore.MODVER,
        dependencies = "required-after:cofhcore;" + "required-after:thermalexpansion;" + "required-after:thermalfoundation;" + "required-after:codechickenlib;" + "required-after:cofhworld;" + "required-after:redstoneflux;" + "after:before:enderio;")
public class WhatTheFlux {

    @Mod.Instance(LibCore.MODID)
    public static WhatTheFlux instance;

    @SidedProxy(clientSide = "io.github.phantamanta44.wtflux.proxy.ClientProxy", serverSide = "io.github.phantamanta44.wtflux.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final CreativeTabs tabWTF = new CreativeTabWtf();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.onPreInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.onInit();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.onPostInit();
    }

}
