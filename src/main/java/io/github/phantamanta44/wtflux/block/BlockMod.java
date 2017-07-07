package io.github.phantamanta44.wtflux.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.phantamanta44.wtflux.WhatTheFlux;
import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.util.IconHelper;
import io.github.phantamanta44.wtflux.util.WtfUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMod extends Block {

    public BlockMod(Material material) {
        super(material);
        addToCreative();
    }

    @Override
    public Block setBlockName(String name) {
        if (GameRegistry.findBlock(LibCore.MODID, name) == null)
            GameRegistry.registerBlock(this, name);
        return super.setBlockName(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registry) {
        blockIcon = IconHelper.forBlock(registry, this);
    }

    public void addToCreative() {
        setCreativeTab(WhatTheFlux.tabWTF);
    }

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
    }

}
