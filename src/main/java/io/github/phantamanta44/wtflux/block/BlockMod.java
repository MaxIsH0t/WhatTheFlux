package io.github.phantamanta44.wtflux.block;

import io.github.phantamanta44.wtflux.WhatTheFlux;
import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.renderer.IIcon;
import io.github.phantamanta44.wtflux.renderer.IIconRegister;
import io.github.phantamanta44.wtflux.util.IconHelper;
import io.github.phantamanta44.wtflux.util.ModUtil;
import io.github.phantamanta44.wtflux.util.WtfUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class BlockMod extends Block {

    public BlockMod(Material material) {
        super(material);
        addToCreative();
    }

    public abstract void getSubBlocks(Item parent, CreativeTabs tab, List items);

    public abstract int damageDropped(int metadata);

    public void addToCreative() {
        setCreativeTab(WhatTheFlux.tabWTF);
    }

    /**
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (this instanceof ITileEntityProvider) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te != null && te instanceof IInventory) {
                IInventory inv = (IInventory)te;
                ItemStack stack;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    if ((stack = inv.getStackInSlot(i)) != null)
                        WtfUtil.dropItem(world, x, y, z, stack);
                }
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }**/

    @SideOnly(Side.CLIENT)
    public abstract void registerBlockIcons(IIconRegister registry);

    public abstract IIcon getIcon(IBlockAccess world, int x, int y, int z, int face);

    public abstract IIcon getIcon(int face, int meta);
}
