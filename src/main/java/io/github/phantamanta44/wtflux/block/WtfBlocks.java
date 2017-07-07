package io.github.phantamanta44.wtflux.block;

import net.minecraft.block.Block;

public final class WtfBlocks {

    public static Block blockMetal;
    public static Block blockOre;
    public static Block blockGen;

    public static void init() {
        blockMetal = new BlockCompressed();
        blockOre = new BlockOre();
        blockGen = new BlockGenerator();
    }

}
