package io.github.phantamanta44.wtflux.common.item;

import io.github.phantamanta44.wtflux.common.creativetab.ModCreativeTabs;
import io.github.phantamanta44.wtflux.util.ModUtil;
import net.minecraft.item.Item;

public class ItemMod extends Item
{
    public ItemMod(final String name)
    {
        ModUtil.setRegistryNames(this, name);
        setCreativeTab(ModCreativeTabs.MOD_TAB);
    }
}
