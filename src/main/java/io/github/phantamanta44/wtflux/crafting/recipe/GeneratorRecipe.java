package io.github.phantamanta44.wtflux.crafting.recipe;

import io.github.phantamanta44.wtflux.block.WtfBlocks;
import io.github.phantamanta44.wtflux.item.ItemMisc;
import io.github.phantamanta44.wtflux.item.WtfItems;
import io.github.phantamanta44.wtflux.lib.LibDict;
import io.github.phantamanta44.wtflux.lib.LibNBT;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GeneratorRecipe implements IRecipe {

    public static final String[] GEARS = new String[] {
            LibDict.GEAR_TIN, LibDict.GEAR_INVAR, LibDict.GEAR_PLAT, LibDict.GEAR_END,
            LibDict.GEAR_BRONZE, LibDict.GEAR_COPPER, LibDict.GEAR_ELEC, LibDict.GEAR_GOLD,
            LibDict.GEAR_IRON, LibDict.GEAR_LEAD, LibDict.GEAR_LUM, LibDict.GEAR_NICKEL,
            LibDict.GEAR_SIG, LibDict.GEAR_SILVER, LibDict.GEAR_URAN, LibDict.GEAR_ZINC
    };

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        if (inv.getStackInSlot(1) == null)
            return false;
        for (int i = 3; i <= 8; i++) {
            if (inv.getStackInSlot(i) == null)
                return false;
        }

        ItemStack gearA = inv.getStackInSlot(3);
        ItemStack dyn = inv.getStackInSlot(4);
        ItemStack gearB = inv.getStackInSlot(5);
        ItemStack cap = inv.getStackInSlot(7);

        int gearTier = -1;
        for (int i = 0; i < GEARS.length; i++) {
            if (LibDict.matches(gearA, GEARS[i]) && LibDict.matches(gearB, GEARS[i])) {
                gearTier = i;
                break;
            }
        }

        return gearTier != -1
                && inv.getStackInSlot(1).getItem() == WtfItems.itemRot
                && (dyn.getItem() == WtfItems.itemDyn && dyn.getItemDamage() > 2)
                && (cap.getItem() == WtfItems.itemCap && cap.getItemDamage() > 2)
                && checkLeads(inv.getStackInSlot(6), inv.getStackInSlot(8));
    }

    private static boolean checkLeads(ItemStack a, ItemStack b) {
        return isAnode(a) ? isCathode(b) : isCathode(a) && isAnode(b);
    }

    private static boolean isCathode(ItemStack stack) {
        return stack.getItem() == WtfItems.itemMisc && stack.getItemDamage() == ItemMisc.CATHODE;
    }

    private static boolean isAnode(ItemStack stack) {
        return stack.getItem() == WtfItems.itemMisc && stack.getItemDamage() == ItemMisc.ANODE;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack gen = inv.getStackInSlot(1);
        ItemStack gear = inv.getStackInSlot(3);
        ItemStack dyn = inv.getStackInSlot(4);
        ItemStack cap = inv.getStackInSlot(7);

        byte gearTier = 0;
        for (byte i = 0; i < GEARS.length; i++) {
            if (LibDict.matches(gear, GEARS[i])) {
                gearTier = i;
                break;
            }
        }

        ItemStack res = new ItemStack(WtfBlocks.blockGen, 1, gen.getItemDamage());
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte(LibNBT.GENTYPE, (byte)gen.getItemDamage());
        tag.setByte(LibNBT.DYNTYPE, (byte)(dyn.getItemDamage() - 3));
        tag.setByte(LibNBT.CAPTYPE, (byte)(cap.getItemDamage() - 3));
        tag.setByte(LibNBT.CASINGTYPE, gearTier);
        tag.setInteger(LibNBT.ENERGY, 0);
        tag.setInteger(LibNBT.ENERGY_MAX, TileGenerator.CAP_AMOUNTS[cap.getItemDamage() - 3]);
        res.setTagCompound(tag);

        return res;
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

}
