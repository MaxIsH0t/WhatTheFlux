package io.github.phantamanta44.wtflux.item;

import net.minecraft.item.Item;

public final class WtfItems {

    public static Item itemRes;
    public static Item itemMisc;
    public static Item itemDyn;
    public static Item itemRot;
    public static Item itemCap;
    public static Item itemRct;

    public static void init() {
        itemRes = new ItemResource();
        itemMisc = new ItemMisc();
        itemDyn = new ItemDynamo();
        itemRot = new ItemRotary();
        itemCap = new ItemCapacitor();
        itemRct = new ItemReactor();
    }

}
