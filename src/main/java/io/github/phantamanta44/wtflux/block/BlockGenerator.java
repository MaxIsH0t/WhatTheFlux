package io.github.phantamanta44.wtflux.block;

import io.github.phantamanta44.wtflux.item.block.ItemBlockGenerator;
import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import io.github.phantamanta44.wtflux.tile.TileMod;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cofh.api.block.IDismantleable;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockGenerator extends BlockModSubs implements ITileEntityProvider, IDismantleable {

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
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fort) {
		ArrayList<ItemStack> drops = Lists.newArrayList();
		TileGenerator tile = (TileGenerator)world.getTileEntity(x, y, z);
		if (tile != null && tile.isInitialized()) {
			drops.add(tile.getNBTItem());
			for (int i = 0; i < tile.getSizeInventory(); i++) {
				if (tile.getStackInSlot(i) != null)
					drops.add(tile.getStackInSlot(i));
			}
			return drops;
		}
		drops.add(new ItemStack(this, 1, meta));
		return drops;
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		TileGenerator tile = (TileGenerator)world.getTileEntity(x, y, z);
		if (tile != null && tile.isInitialized())
			return tile.getNBTItem();
		return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return TileGenerator.getAppropriateTile(meta);
	}

	@Override
	public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops) {
		ArrayList<ItemStack> items = getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
		if (!returnDrops) {
			for (ItemStack stack : items) {
				float f = world.rand.nextFloat() * 0.8F + 0.1F;
				float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
				float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
				EntityItem ent = new EntityItem(world, x + f, y + f1, z + f2, stack);
				float f3 = 0.05F;
				ent.motionX = (double)((float)world.rand.nextGaussian() * f3);
				ent.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
				ent.motionZ = (double)((float)world.rand.nextGaussian() * f3);
				world.spawnEntityInWorld(ent);
			}
		}
		world.setBlockToAir(x, y, z);
		return items;
	}

	@Override
	public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
		return ((TileMod)world.getTileEntity(x, y, z)).isInitialized();
	}

}
