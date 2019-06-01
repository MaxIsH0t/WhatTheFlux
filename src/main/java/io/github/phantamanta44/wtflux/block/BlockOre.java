package io.github.phantamanta44.wtflux.block;

import io.github.phantamanta44.wtflux.item.block.ItemBlockOre;
import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.renderer.IIcon;
import io.github.phantamanta44.wtflux.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockOre extends BlockModSubs {

    public static final int ZINC = 0, URAN = 1;

    public BlockOre(final String name, Material material, int blocks) {
        super(name, Material.ROCK, 2);
        setHardness(1.2F);
        setResistance(4.8F);
        setHarvestLevel(LibCore.TOOL_PICK, 2);
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
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean canHarvest) {
        return false;
    }

    @Override
    public ItemStack getPickBlock(RayTraceResult target, World world, int x, int y, int z, EntityPlayer player) {
        return null;
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