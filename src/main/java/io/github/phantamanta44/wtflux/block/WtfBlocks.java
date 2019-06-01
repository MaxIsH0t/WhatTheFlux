package io.github.phantamanta44.wtflux.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public final class WtfBlocks {

    public static Block blockMetal;
    public static Block blockOre;
    public static Block blockGen;
    public static Block blockSensor;

    public static void init() {
        blockMetal = new BlockCompressed("block_compressed", Material.IRON);
        blockOre = new BlockOre("block_ore", Material.IRON, 1);
        blockGen = new BlockGenerator("block_generator", Material.IRON);
        blockSensor = new BlockSensor("block_sensor", Material.IRON);
    }

}
