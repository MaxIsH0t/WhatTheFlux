package io.github.phantamanta44.wtflux;

import io.github.phantamanta44.wtflux.item.ItemRotary;
import io.github.phantamanta44.wtflux.item.WtfItems;
import io.github.phantamanta44.wtflux.lib.LibLang;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabWtf extends CreativeTabs {

	public CreativeTabWtf() {
		super(LibLang.CREATIVE_TAB_NAME);
	}
	
	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(WtfItems.itemRot, 1, ItemRotary.NUKE);
	}
	
	@Override
	public Item getTabIconItem() {
		return WtfItems.itemRot;
	}
	
}
