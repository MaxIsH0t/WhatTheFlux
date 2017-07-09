package io.github.phantamanta44.wtflux.block;

import cofh.api.block.IDismantleable;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.phantamanta44.wtflux.WhatTheFlux;
import io.github.phantamanta44.wtflux.item.block.ItemBlockDirectional;
import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.tile.TileMod;
import io.github.phantamanta44.wtflux.tile.TileSensor;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockSensor extends BlockModSubs implements ITileEntityProvider, IDismantleable {

    public static final int TEMP = 0, ENERGY = 1, RPM = 2;

    public BlockSensor() {
        super(Material.iron, 3);
        setHardness(4F);
        setResistance(7.5F);
        setBlockName(LibLang.SENSOR_BLOCK_NAME);
    }

    @Override
    public Block setBlockName(String name) {
        GameRegistry.registerBlock(this, ItemBlockDirectional.class, name);
        return super.setBlockName(name);
    }

    @Override
    public void registerBlockIcons(IIconRegister registry) {
        icons = new IIcon[7];
        icons[6] = IconHelper.forBlock(registry, this, "Face");
        for (int i = 0; i < 3; i++) {
            icons[i * 2] = IconHelper.forBlock(registry, this, i, "off");
            icons[i * 2 + 1] = IconHelper.forBlock(registry, this, i, "on");
        }
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int face) {
        TileSensor tile = (TileSensor)world.getTileEntity(x, y, z);
        return icons[tile != null
                ? face == tile.getFacing() ? 6 : world.getBlockMetadata(x, y, z) * 2 + (tile.isTripped() ? 1 : 0)
                : face == 3 ? 6 : world.getBlockMetadata(x, y, z) * 2];
    }

    @Override
    public IIcon getIcon(int face, int meta) {
        return icons[face == 3 ? 6 : meta * 2];
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int face) {
        TileSensor tile = (TileSensor)world.getTileEntity(x, y, z);
        return tile != null ? (face == tile.getOppositeFace() ? 0 : (tile.isTripped() ? 15 : 0)) : 0;
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops) {
        world.setBlockToAir(x, y, z);
        ItemStack stack = new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
        if (!returnDrops)
            WtfUtil.dropItem(world, x, y, z, stack);
        return Lists.newArrayList(stack);
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

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return TileSensor.getAppropriateTile(meta);
    }

}
