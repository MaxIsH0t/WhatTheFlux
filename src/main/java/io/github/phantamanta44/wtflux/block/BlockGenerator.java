package io.github.phantamanta44.wtflux.block;

import cofh.api.block.IDismantleable;
import com.google.common.collect.Lists;
import io.github.phantamanta44.wtflux.WhatTheFlux;
import io.github.phantamanta44.wtflux.renderer.IIcon;
import io.github.phantamanta44.wtflux.renderer.IIconRegister;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import io.github.phantamanta44.wtflux.tile.TileMod;
import io.github.phantamanta44.wtflux.util.IconHelper;
import io.github.phantamanta44.wtflux.util.ModUtil;
import io.github.phantamanta44.wtflux.util.WtfUtil;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class BlockGenerator extends BlockModSubs implements ITileEntityProvider, IDismantleable {

    public BlockGenerator(final String name, final Material material) {
        super(name, material, 6);
        ModUtil.setRegistryNames(this, name);
        setHardness(4F);
        setResistance(7.5F);
    }

    @Override
    public void registerBlockIcons(IIconRegister registry) {
        icons = new IIcon[subblockCount * 4 + 1];
        icons[0] = IconHelper.forBlock(registry, this, "Bottom");
        for (int i = 0; i < subblockCount; i++) {
            icons[i * 4 + 1] = IconHelper.forBlock(registry, this, i, "topOff");
            icons[i * 4 + 2] = IconHelper.forBlock(registry, this, i, "topOn");
            icons[i * 4 + 3] = IconHelper.forBlock(registry, this, i, "sideOff");
            icons[i * 4 + 4] = IconHelper.forBlock(registry, this, i, "sideOn");
        }
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int face) {
        BlockPos blockPos = new BlockPos(x, y, z);
        //TileGenerator tile = (TileGenerator)world.getTileEntity(x, y, z);
        //boolean active = tile != null && tile.isActive();
        if (face == 0)
            return icons[0];
        //else if (face == 1)
            //return icons[active ? world.getBlockState(blockPos)];
        //else
            //return icons[active ? world.getBlockMetadata(x, y, z) * 4 + 4 : world.getBlockMetadata(x, y, z) * 4 + 3];
        return null;
    }

    @Override
    protected IIcon icons(IBlockState blockState) {
        return null;
    }

    @Override
    public IIcon getIcon(int face, int meta) {
        if (face == 0)
            return icons[0];
        else if (face == 1)
            return icons[meta * 4 + 1];
        else
            return icons[meta * 4 + 3];
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
        BlockPos blockPos = new BlockPos(x, y, z);
        if (WtfUtil.canHarvest(world, x, y, z, player)) {
            List<ItemStack> drops = (List<ItemStack>) compileDrops(world, x, y, z, world.getBlockState(blockPos));
            for (ItemStack stack : drops)
                WtfUtil.dropItem(world, x, y, z, stack);
        }
        return world.setBlockToAir(blockPos);
    }

    public NonNullList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        return OreDictionary.EMPTY_LIST;
    }

    public ArrayList<ItemStack> compileDrops(IBlockAccess world, int x, int y, int z, IBlockState meta) {
        BlockPos blockPos = new BlockPos(x, y, z);
        ArrayList<ItemStack> drops = Lists.newArrayList();
        TileGenerator tile = (TileGenerator)world.getTileEntity(blockPos);
        if (tile != null && tile.isInitialized()) {
            drops.add(tile.getNBTItem());
            for (int i = 0; i < tile.getSizeInventory(); i++) {
                if (tile.getStackInSlot(i) != null)
                    drops.add(tile.getStackInSlot(i));
            }
        }
        return drops;
    }

    @Override
    public ItemStack getPickBlock(RayTraceResult target, World world, int x, int y, int z, EntityPlayer player) {
        BlockPos blockPos = new BlockPos(x, y, z);
        TileGenerator tile = (TileGenerator)world.getTileEntity(blockPos);
        if (tile != null && tile.isInitialized())
            return tile.getNBTItem();
        return null;//new ItemStack(this, 1, world.getBlockState(blockPos));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return TileGenerator.getAppropriateTile(meta);
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops) {
        BlockPos blockPos = new BlockPos(x, y, z);
        IBlockState meta = world.getBlockState(blockPos);
        ArrayList<ItemStack> items = compileDrops(world, x, y, z, meta);
        if (!returnDrops) {
            for (ItemStack stack : items)
                WtfUtil.dropItem(world, x, y, z, stack);
        }
        world.setBlockToAir(blockPos);
        return items;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return ((TileMod)world.getTileEntity(blockPos)).isInitialized();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        if (world.isRemote)
            return true;

        if (player.isSneaking())
            return false;

        player.openGui(WhatTheFlux.instance, 255, world, x, y, z);
        return true;
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player, boolean returnDrops) {
        return null;
    }

    @Override
    public boolean canDismantle(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }
}
