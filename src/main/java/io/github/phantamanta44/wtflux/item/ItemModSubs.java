package io.github.phantamanta44.wtflux.item;

import io.github.phantamanta44.wtflux.renderer.IIcon;
import io.github.phantamanta44.wtflux.renderer.IIconRegister;
import io.github.phantamanta44.wtflux.util.IconHelper;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemModSubs extends ItemMod {

    protected final int subitemCount;
    protected IIcon[] icons;

    public ItemModSubs(int items) {
        super();
        subitemCount = items;
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(Item parent, CreativeTabs tab, List items) {
        for (int i = 0; i < subitemCount; i++)
            items.add(new ItemStack(parent, 1, i));
    }
}
