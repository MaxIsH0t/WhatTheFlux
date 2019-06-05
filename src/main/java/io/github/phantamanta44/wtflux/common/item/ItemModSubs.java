package io.github.phantamanta44.wtflux.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemModSubs extends ItemMod
{
    protected final int subitemCount;

    public ItemModSubs(String name, int items) {
        super(name);
        subitemCount = items;
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        Item parent = new Item();
        for (int i = 0; i < subitemCount; i++)
            items.add(new ItemStack(parent, 1, i));

    }
}
