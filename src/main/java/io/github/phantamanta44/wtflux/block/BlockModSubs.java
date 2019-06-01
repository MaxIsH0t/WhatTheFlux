package io.github.phantamanta44.wtflux.block;

import io.github.phantamanta44.wtflux.renderer.IIcon;
import io.github.phantamanta44.wtflux.renderer.IIconRegister;
import io.github.phantamanta44.wtflux.util.IconHelper;

import java.util.ArrayList;
import java.util.List;

import io.github.phantamanta44.wtflux.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockModSubs extends BlockMod {

    protected final int subblockCount;
    protected IIcon[] icons;

    public BlockModSubs(final String name, Material material, int blocks) {
        super(material);
        ModUtil.setRegistryNames(this, name);
        subblockCount = blocks;
    }

    @Override
    public void getSubBlocks(Item parent, CreativeTabs tab, List items) {
        for (int i = 0; i < subblockCount; i++)
            items.add(new ItemStack(parent, 1, i));
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registry) {
        icons = new IIcon[subblockCount];
        for (int i = 0; i < subblockCount; i++)
            icons[i] = IconHelper.forBlock(registry, this, i);
    }

    //public abstract void registerBlockIcons(IIconRegister registry);

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int face) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return icons(world.getBlockState(blockPos));
    }

    protected abstract IIcon icons(IBlockState blockState);

    @Override
    public IIcon getIcon(int face, int meta) {
        return icons[meta];
    }

    public abstract boolean canProvidePower();

    public abstract int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int face);

    public abstract boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean canHarvest);

    public abstract ItemStack getPickBlock(RayTraceResult target, World world, int x, int y, int z, EntityPlayer player);

    public abstract ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops);

    public abstract boolean canDismantle(EntityPlayer player, World world, int x, int y, int z);

    public abstract boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ);
}
