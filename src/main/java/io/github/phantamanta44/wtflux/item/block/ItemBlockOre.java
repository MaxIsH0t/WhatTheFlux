package io.github.phantamanta44.wtflux.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemBlockOre extends ItemBlockWithMetadataAndName {

	public ItemBlockOre(Block block) {
		super(block);
	}
	
	public EnumRarity getRarity(ItemStack stack) {
		switch (stack.getItemDamage()) {
		default:
			return EnumRarity.common;
		}
	}

}
