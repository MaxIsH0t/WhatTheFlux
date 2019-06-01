package io.github.phantamanta44.wtflux.block;

import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.renderer.IIcon;
import io.github.phantamanta44.wtflux.util.ModUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

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

    @Override
    protected IIcon icons(IBlockState blockState) {
        return null;
    }

    @Override
    public boolean canProvidePower() {
        return false;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int face) {
        return 0;
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops) {
        return null;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        return false;
    }
}
