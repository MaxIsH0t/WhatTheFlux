package io.github.phantamanta44.wtflux.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockModSubs extends BlockMod
{
    protected final int subblockCount;

    public BlockModSubs(String name, Material material, int blocks) {
        super(name, material);
        subblockCount = blocks;
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        Item parent = new Item();
        for (int i = 0; i < subblockCount; i++)
            items.add(new ItemStack(parent, 1, i));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return super.damageDropped(state);
    }
}
