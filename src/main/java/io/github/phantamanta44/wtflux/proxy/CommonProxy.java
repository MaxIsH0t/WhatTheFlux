package io.github.phantamanta44.wtflux.proxy;

import io.github.phantamanta44.wtflux.block.BlockOre;
import io.github.phantamanta44.wtflux.block.WtfBlocks;
import io.github.phantamanta44.wtflux.crafting.MasterRecipeManager;
import io.github.phantamanta44.wtflux.item.WtfItems;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import io.github.phantamanta44.wtflux.util.BlockWithMeta;
import io.github.phantamanta44.wtflux.worldgen.OreGenSimple;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {
	
	public void onPreInit() {
		WtfItems.init();
		WtfBlocks.init();
		registerTileEntities();
	}
	
	public void onInit() {
		MasterRecipeManager.addRecipes();
		GameRegistry.registerWorldGenerator(new OreGenSimple(new BlockWithMeta(WtfBlocks.blockOre, BlockOre.ZINC), 0, new BlockWithMeta(Blocks.stone), 4, 72, 12, 8), 8);
		GameRegistry.registerWorldGenerator(new OreGenSimple(new BlockWithMeta(WtfBlocks.blockOre, BlockOre.URAN), 0, new BlockWithMeta(Blocks.stone), 4, 30, 6, 10), 5);
	}
	
	public void onPostInit() {
		
	}
	
	protected void registerTileEntities() {
		addTEMapping(TileGenerator.Furnace.class);
		addTEMapping(TileGenerator.Heat.class);
		addTEMapping(TileGenerator.Wind.class);
		addTEMapping(TileGenerator.Water.class);
		addTEMapping(TileGenerator.Nuke.class);
		addTEMapping(TileGenerator.Solar.class);
	}
	
	protected void addTEMapping(Class c) {
		TileEntity.addMapping(c, c.getName());
	}
	
}
