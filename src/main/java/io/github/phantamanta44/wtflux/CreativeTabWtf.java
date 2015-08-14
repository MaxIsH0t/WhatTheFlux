package io.github.phantamanta44.wtflux;

import io.github.phantamanta44.wtflux.item.WtfItems;
import io.github.phantamanta44.wtflux.item.ItemResource;
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
		return new ItemStack(WtfItems.itemRes, 1, ItemResource.INGOT_ZINC);
	}
	
	@Override
	public Item getTabIconItem() {
		return WtfItems.itemRes;
	}
	
}
