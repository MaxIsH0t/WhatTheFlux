package io.github.phantamanta44.wtflux.util;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public class BlockWithMeta {
	
	private Block block;
	private int meta;
	
	public BlockWithMeta(Block type) {
		block = type;
		meta = 0;
	}
	
	public BlockWithMeta(Block type, int damage) {
		block = type;
		meta = damage;
	}
	
	public BlockWithMeta(IBlockAccess world, int x, int y, int z) {
		block = world.getBlock(x, y, z);
		meta = world.getBlockMetadata(x, y, z);
	}
	
	public Block getBlock() {
		return block;
	}
	
	public int getMeta() {
		return meta;
	}
	
	public boolean equals(Object o) {
		if (o instanceof BlockWithMeta) {
			BlockWithMeta bwm = (BlockWithMeta)o;
			return bwm.block.equals(block) && bwm.meta == meta;
		}
		return false;
	}
	
}
