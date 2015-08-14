package io.github.phantamanta44.wtflux.item;

import io.github.phantamanta44.wtflux.lib.LibLang;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemResource extends ItemModSubs {

	public static final int INGOT_ZINC = 0, DUST_ZINC = 1, NUGGET_ZINC = 2, GEAR_ZINC = 3;
	
	public ItemResource() {
		super(4);
		setUnlocalizedName(LibLang.RES_NAME);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		switch (stack.getItemDamage()) {
		default:
			return EnumRarity.common;
		}
	}

}
