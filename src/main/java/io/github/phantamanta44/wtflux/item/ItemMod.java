package io.github.phantamanta44.wtflux.item;

import io.github.phantamanta44.wtflux.creativetabs.ModCreativeTab;
import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.renderer.IIcon;
import io.github.phantamanta44.wtflux.renderer.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemMod extends Item {

    public ItemMod() {
        super();
        setCreativeTab(ModCreativeTab.MOD_TAB);
    }

    @Override
    public Item setUnlocalizedName(String name) {
        return super.setUnlocalizedName(name);
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack itemstack) {
        return super.getUnlocalizedNameInefficiently(itemstack).replaceAll("item\\.", "item." + LibCore.MODPREF);
    }

    public abstract void getSubItems(Item parent, CreativeTabs tab, List items);


    @SideOnly(Side.CLIENT)
    public abstract void registerIcons(IIconRegister registry);

    public abstract IIcon getIconFromDamage(int meta);
}
