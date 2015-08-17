package io.github.phantamanta44.wtflux.item;

import io.github.phantamanta44.wtflux.lib.LibLang;

public class ItemCapacitor extends ItemModSubs {
	
	public static final int DIELEC_1 = 0, DIELEC_2 = 1, DIELEC_3 = 2, CAP_1 = 3, CAP_2= 4, CAP_3 = 5, CAP_4 = 6, CAP_5 = 7, CAP_6 = 8;
	
	public ItemCapacitor() {
		super(9);
		setUnlocalizedName(LibLang.CAPACITOR_NAME);
	}
	
}
