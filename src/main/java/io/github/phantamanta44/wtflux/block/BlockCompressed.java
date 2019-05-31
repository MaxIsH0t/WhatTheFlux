package io.github.phantamanta44.wtflux.block;

import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.util.ModUtil;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCompressed extends BlockModSubs {

    public static final int ZINC = 0, URAN = 1;

    public BlockCompressed(final String name, final Material material) {
        super(name, material, 2);
        ModUtil.setRegistryNames(this, name);
        setHardness(4F);
        setResistance(7.5F);
        setBlockName(LibLang.METAL_BLOCK_NAME);
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return true;
    }
}
