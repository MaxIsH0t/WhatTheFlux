package io.github.phantamanta44.wtflux.block;

import io.github.phantamanta44.wtflux.item.block.ItemBlockGenerator;
import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockGenerator extends BlockModSubs implements ITileEntityProvider {

	public BlockGenerator() {
		super(Material.iron, 6);
		setHardness(4F);
		setResistance(7.5F);
		setBlockName(LibLang.GENERATOR_BLOCK_NAME);
	}
	
	@Override
	public Block setBlockName(String name) {
		GameRegistry.registerBlock(this, ItemBlockGenerator.class, name);
		return super.setBlockName(name);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return TileGenerator.getAppropriateTile(meta);
	}

}
