package io.github.phantamanta44.wtflux.block;

import cofh.api.block.IDismantleable;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.phantamanta44.wtflux.WhatTheFlux;
import io.github.phantamanta44.wtflux.item.block.ItemBlockGenerator;
import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import io.github.phantamanta44.wtflux.tile.TileMod;
import io.github.phantamanta44.wtflux.util.IconHelper;
import io.github.phantamanta44.wtflux.util.WtfUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockGenerator extends BlockModSubs implements ITileEntityProvider, IDismantleable {

    public BlockGenerator() {
        super(Material.iron, 6);
        setHardness(4F);
        setResistance(7.5F);
        setBlockName(LibLang.GENERATOR_BLOCK_NAME);
    }

    @Override
    public Block setBlockName(String name) {
        GameRegistry.registerBlock(this, ItemBlockGenerator.class, name);
        return super.setBlockName(name);
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
        TileGenerator tile = (TileGenerator)world.getTileEntity(x, y, z);
        boolean active = tile != null && tile.isActive();
        if (face == 0)
            return icons[0];
        else if (face == 1)
            return icons[active ? world.getBlockMetadata(x, y, z) * 4 + 2 : world.getBlockMetadata(x, y, z) * 4 + 1];
        else
            return icons[active ? world.getBlockMetadata(x, y, z) * 4 + 4 : world.getBlockMetadata(x, y, z) * 4 + 3];
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
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean canHarvest) {
        if (WtfUtil.canHarvest(world, x, y, z, player)) {
            List<ItemStack> drops = compileDrops(world, x, y, z, world.getBlockMetadata(x, y, z));
            for (ItemStack stack : drops)
                WtfUtil.dropItem(world, x, y, z, stack);
        }
        return world.setBlockToAir(x, y, z);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        return OreDictionary.EMPTY_LIST;
    }

    public ArrayList<ItemStack> compileDrops(IBlockAccess world, int x, int y, int z, int meta) {
        ArrayList<ItemStack> drops = Lists.newArrayList();
        TileGenerator tile = (TileGenerator)world.getTileEntity(x, y, z);
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
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        TileGenerator tile = (TileGenerator)world.getTileEntity(x, y, z);
        if (tile != null && tile.isInitialized())
            return tile.getNBTItem();
        return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return TileGenerator.getAppropriateTile(meta);
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops) {
        int meta = world.getBlockMetadata(x, y, z);
        ArrayList<ItemStack> items = compileDrops(world, x, y, z, meta);
        if (!returnDrops) {
            for (ItemStack stack : items)
                WtfUtil.dropItem(world, x, y, z, stack);
        }
        world.setBlockToAir(x, y, z);
        return items;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
        return ((TileMod)world.getTileEntity(x, y, z)).isInitialized();
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

}
