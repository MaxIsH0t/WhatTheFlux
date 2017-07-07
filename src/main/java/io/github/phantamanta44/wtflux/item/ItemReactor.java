package io.github.phantamanta44.wtflux.item;


import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.lib.LibNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemReactor extends ItemModSubs {

    public static final int BLASTER = 0, CONTROL_ROD = 1, WASTE = 2, COOLANT_CELL = 3, PLATE = 4, CORE = 5, CONDENSER = 6, RPV = 7, INJECTOR = 8, VALVE = 9, HOW_CRADLE = 10, ROD_CRADLE = 11, CASING = 12;

    public ItemReactor() {
        super(13);
        setUnlocalizedName(LibLang.REACTOR_COMPONENT_NAME);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean flag) {
        int meta = stack.getItemDamage();
        if (meta == 0 || meta == 1) {
            float damagePercent = (float)(getVirtualMaxDamage(stack) - getVirtualDamage(stack)) / (float)getVirtualMaxDamage(stack);
            String color = (damagePercent == 1F ? EnumChatFormatting.AQUA : (damagePercent > 0.7F ? EnumChatFormatting.GREEN : (damagePercent > 0.5F ? EnumChatFormatting.YELLOW : (damagePercent > 0.2F ? EnumChatFormatting.GOLD : EnumChatFormatting.DARK_RED)))).toString();
            info.add(String.format("%s: %s%d%%", LibLang.get(LibLang.INF_DURA), color, (int)Math.max(damagePercent * 100, 1)));
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        int damage = stack.getItemDamage();
        if (damage == BLASTER || damage == CONTROL_ROD)
            return (double)getVirtualDamage(stack) / (double)getVirtualMaxDamage(stack);
        return super.getDurabilityForDisplay(stack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        int damage = stack.getItemDamage();
        return (damage == BLASTER || damage == CONTROL_ROD) && getVirtualDamage(stack) > 0;
    }

    public int getVirtualDamage(ItemStack stack) {
        if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(LibNBT.DURABILITY))
            return stack.stackTagCompound.getInteger(LibNBT.DURABILITY);
        return 0;
    }

    public int getVirtualMaxDamage(ItemStack stack) {
        switch (stack.getItemDamage()) {
        case BLASTER:
            return 128;
        case CONTROL_ROD:
            return 4000;
        default:
            return 0;
        }
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        switch (stack.getItemDamage()) {
        case BLASTER:
        case CONTROL_ROD:
            return 1;
        default:
            return 64;
        }
    }

    public ItemStack decrementUses(ItemStack stack) {
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
            if (stack.stackTagCompound.getInteger(LibNBT.DURABILITY) >= getVirtualMaxDamage(stack))
                stack.stackSize--;
            if (stack.stackSize < 0)
                return null;
        }
        return stack;
    }

}
