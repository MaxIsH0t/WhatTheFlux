package io.github.phantamanta44.wtflux.item;

import io.github.phantamanta44.wtflux.lib.LibLang;

public class ItemMisc extends ItemModSubs {

    public static final int COPPER_THREAD = 0, IRON_ROD = 1, SOLENOID = 2, CATHODE = 3, ANODE = 4, HEAT_COND = 5, TURBINE = 6, MIRROR = 7, RAW_GRAPHITE = 8, GRAPHITE = 9, PROPENE = 10, PLASTIC = 11, BATTERY_CORE = 12;

    public ItemMisc() {
        super(13);
        setUnlocalizedName(LibLang.MISC_NAME);
    }

}
