package io.github.phantamanta44.wtflux.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockWithMeta {

    private final Block block;
    private final int meta;

    public BlockWithMeta(Block type) {
        block = type;
        meta = 0;
    }

    public BlockWithMeta(Block type, int damage) {
        block = type;
        meta = damage;
    }

    public BlockWithMeta(IBlockAccess world, int x, int y, int z, int meta) {
        this.meta = meta;
        BlockPos blockPos = new BlockPos(x, y, z);
        block = (Block) world.getBlockState(blockPos);
        //meta = world.getBlockState(blockPos);
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
