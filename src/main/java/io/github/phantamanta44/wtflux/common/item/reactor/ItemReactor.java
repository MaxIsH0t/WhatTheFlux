package io.github.phantamanta44.wtflux.common.item.reactor;

import io.github.phantamanta44.wtflux.common.item.ItemModSubs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemReactor extends ItemModSubs
{
    public static final int BLASTER = 0, CONTROL_ROD = 1, WASTE = 2, COOLANT_CELL = 3, PLATE = 4, CORE = 5, CONDENSER = 6, RPV = 7, INJECTOR = 8, VALVE = 9, HOW_CRADLE = 10, ROD_CRADLE = 11, CASING = 12;

    public ItemReactor(String name) {
        super(name, 13);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int meta = stack.getItemDamage();
        if (meta == 0 || meta == 1) {
            float damagePercent = (float)(getVirtualMaxDamage(stack) - getVirtualDamage(stack)) / (float)getVirtualMaxDamage(stack);
            String color = (damagePercent == 1F ? EnumChatFormatting.AQUA : (damagePercent > 0.7F ? EnumChatFormatting.GREEN : (damagePercent > 0.5F ? EnumChatFormatting.YELLOW : (damagePercent > 0.2F ? EnumChatFormatting.GOLD : EnumChatFormatting.DARK_RED)))).toString();
            tooltip.add(String.format("%s: %s%d%%", LibLang.get(LibLang.INF_DURA), color, (int)Math.max(damagePercent * 100, 1)));
        }
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
}
