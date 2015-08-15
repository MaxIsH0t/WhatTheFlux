package io.github.phantamanta44.wtflux.item;

import io.github.phantamanta44.wtflux.lib.LibLang;

public class ItemRotary extends ItemModSubs {
	
	public static final int FURN = 0, HEAT = 1, WIND = 2, WATER = 3, NUKE = 4, SOLAR = 5;
	
	public ItemRotary() {
		super(6);
		setUnlocalizedName(LibLang.GEN_COMPONENT_NAME);
	}
	
}
