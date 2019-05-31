package io.github.phantamanta44.wtflux.proxy;

import dan200.computercraft.api.ComputerCraftAPI;
import io.github.phantamanta44.wtflux.WhatTheFlux;
import io.github.phantamanta44.wtflux.block.BlockOre;
import io.github.phantamanta44.wtflux.block.WtfBlocks;
import io.github.phantamanta44.wtflux.crafting.MasterRecipeManager;
import io.github.phantamanta44.wtflux.gui.GuiGenerator;
import io.github.phantamanta44.wtflux.gui.GuiSensor;
import io.github.phantamanta44.wtflux.handler.GuiHandler;
import io.github.phantamanta44.wtflux.inventory.ContainerDummy;
import io.github.phantamanta44.wtflux.inventory.ContainerGenerator;
import io.github.phantamanta44.wtflux.item.WtfItems;
import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.network.WtfNet;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import io.github.phantamanta44.wtflux.tile.TileSensor;
import io.github.phantamanta44.wtflux.util.BlockWithMeta;
import io.github.phantamanta44.wtflux.util.ModUtil;
import io.github.phantamanta44.wtflux.util.computercraft.CCPeripheralProvider;
import io.github.phantamanta44.wtflux.worldgen.OreGenSimple;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonProxy
{
    public static final Logger LOGGER = LogManager.getLogger();

    public void onPreInit() {
        WtfItems.init();
        WtfBlocks.init();
        registerTileEntities();
        registerContainers();
        WtfNet.init();
    }

    public void onInit() {
        MasterRecipeManager.addRecipes();
        GameRegistry.registerWorldGenerator(new OreGenSimple(new BlockWithMeta(WtfBlocks.blockOre, BlockOre.ZINC), 0, new BlockWithMeta(Blocks.STONE), 4, 72, 12, 8), 8);
        GameRegistry.registerWorldGenerator(new OreGenSimple(new BlockWithMeta(WtfBlocks.blockOre, BlockOre.URAN), 0, new BlockWithMeta(Blocks.STONE), 4, 30, 6, 10), 5);
        NetworkRegistry.INSTANCE.registerGuiHandler(WhatTheFlux.instance, new GuiHandler());
        ComputerCraftAPI.registerPeripheralProvider(new CCPeripheralProvider());
    }

    public void onPostInit() {
        // NO-OP
    }

    protected void registerTileEntities() {
        registerTileEntity(TileGenerator.Furnace.class);
        registerTileEntity(TileGenerator.Heat.class);
        registerTileEntity(TileGenerator.Wind.class);
        registerTileEntity(TileGenerator.Water.class);
        registerTileEntity(TileGenerator.Nuke.class);
        registerTileEntity(TileGenerator.Solar.class);
        registerTileEntity(TileSensor.Temperature.class);
        registerTileEntity(TileSensor.Energy.class);
        registerTileEntity(TileSensor.RPM.class);
    }

    protected void registerContainers() {
        registerContainer(TileGenerator.Furnace.class, GuiGenerator.Furnace.class, ContainerGenerator.Furnace.class);
        registerContainer(TileGenerator.Heat.class, GuiGenerator.Heat.class, ContainerGenerator.Heat.class);
        registerContainer(TileGenerator.Wind.class, GuiGenerator.Wind.class, ContainerGenerator.Wind.class);
        registerContainer(TileGenerator.Water.class, GuiGenerator.Water.class, ContainerGenerator.Water.class);
        registerContainer(TileGenerator.Nuke.class, GuiGenerator.Nuke.class, ContainerGenerator.Nuke.class);
        registerContainer(TileGenerator.Solar.class, GuiGenerator.Solar.class, ContainerGenerator.Solar.class);
        registerContainer(TileSensor.Temperature.class, GuiSensor.class, ContainerDummy.class);
        registerContainer(TileSensor.Energy.class, GuiSensor.class, ContainerDummy.class);
        registerContainer(TileSensor.RPM.class, GuiSensor.class, ContainerDummy.class);
    }

    protected void registerContainer(Class<? extends TileEntity> tile, Class<? extends GuiContainer> gui, Class<? extends Container> cont) {
        GuiHandler.guiMap.put(tile, gui);
        GuiHandler.containerMap.put(tile, cont);
    }

    private static void registerTileEntity(final Class<? extends TileEntity> clazz) {

        try {
            GameRegistry.registerTileEntity(clazz, new ResourceLocation(LibCore.MODID, ModUtil.getRegistryNameForClass(clazz, "TileEntity")));
        }
        catch (final Exception e) {
            LOGGER.error("Error registering Tile Entity " + clazz.getSimpleName());
            e.printStackTrace();
        }
    }

}
