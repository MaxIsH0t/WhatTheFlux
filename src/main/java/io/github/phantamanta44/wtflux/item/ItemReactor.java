package io.github.phantamanta44.wtflux.item;


import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.lib.LibNBT;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public class ItemReactor extends ItemModSubs {

	public static final int BLASTER = 0, CONTROL_ROD = 1, WASTE = 2;
	
	public ItemReactor() {
		super(3);
		setUnlocalizedName(LibLang.REACTOR_COMPONENT_NAME);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean flag) {
		int meta = stack.getItemDamage();
		if (meta == 0 || meta == 1) {
			float damagePercent = (float)(getMaxDamage(stack) - getDamage(stack)) / (float)getMaxDamage(stack);
			String color = (damagePercent == 1F ? EnumChatFormatting.AQUA : (damagePercent > 0.7F ? EnumChatFormatting.GREEN : (damagePercent > 0.5F ? EnumChatFormatting.YELLOW : (damagePercent > 0.2F ? EnumChatFormatting.GOLD : EnumChatFormatting.DARK_RED)))).toString();
			info.add(String.format("%s: %s%d%%", LibLang.get(LibLang.INF_DURA), color, (int)Math.max(damagePercent * 100, 1)));
		}
	}
	
	@Override
	public int getDamage(ItemStack stack) {
		if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(LibNBT.DURABILITY))
			return stack.stackTagCompound.getInteger(LibNBT.DURABILITY);
		return 0;
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case BLASTER:
			return 36;
		case CONTROL_ROD:
			return 15000;
		default:
			return 0;
		}
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		int meta = stack.getItemDamage();
		return meta == 0 ? 1 : 64;
	}
	
	public void decrementUses(ItemStack stack) {
		int meta = stack.getItemDamage();
		if (meta == 0 || meta == 1) {
			if (stack.stackTagCompound != null) {
				if (stack.stackTagCompound.hasKey(LibNBT.DURABILITY)) {
					int dura = stack.stackTagCompound.getInteger(LibNBT.DURABILITY);
					stack.stackTagCompound.setInteger(LibNBT.DURABILITY, dura + 1);
				}
				else
					stack.stackTagCompound.setInteger(LibNBT.DURABILITY, 1);
			}
			else {
				stack.setTagCompound(new NBTTagCompound());
				stack.stackTagCompound.setInteger(LibNBT.DURABILITY, 1);
			}
			if (stack.stackTagCompound.getInteger(LibNBT.DURABILITY) >= getMaxDamage(stack))
				stack.stackSize--;
		}
	}

}
