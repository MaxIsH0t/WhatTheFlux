package io.github.phantamanta44.wtflux.block;

import io.github.phantamanta44.wtflux.item.block.ItemBlockCompressed;
import io.github.phantamanta44.wtflux.lib.LibLang;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockCompressed extends BlockModSubs {

    public static final int ZINC = 0, URAN = 1;

    public BlockCompressed() {
        super(Material.iron, 2);
        setHardness(4F);
        setResistance(7.5F);
        setBlockName(LibLang.METAL_BLOCK_NAME);
    }

    @Override
    public Block setBlockName(String name) {
        GameRegistry.registerBlock(this, ItemBlockCompressed.class, name);
        return super.setBlockName(name);
    }

    @Override
    public boolean isBeaconBase(IBlockAccess world, int x, int y, int z, int bX, int bY, int bZ) {
        return true;
    }

}
