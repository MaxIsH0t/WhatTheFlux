package io.github.phantamanta44.wtflux.item;

import io.github.phantamanta44.wtflux.util.ModUtil;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemResource extends ItemModSubs {

    public static final int INGOT_ZINC = 0, DUST_ZINC = 1, NUGGET_ZINC = 2, GEAR_ZINC = 3,
            INGOT_URAN = 4, DUST_URAN = 5, NUGGET_URAN = 6, GEAR_URAN = 7;

    public ItemResource(final String name) {
        super(8);
        ModUtil.setRegistryNames(this, name);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        switch (stack.getItemDamage()) {
        default:
            return EnumRarity.COMMON;
        }
    }

}
