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

    public static final String[] GEARS = new String[] {LibDict.GEAR_TIN, LibDict.GEAR_INVAR, LibDict.GEAR_PLAT, LibDict.GEAR_END};

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        if (inv.getStackInSlot(1) == null)
            return false;
        for (int i = 3; i <= 8; i++) {
            if (inv.getStackInSlot(i) == null)
                return false;
        }

        ItemStack dyn = inv.getStackInSlot(4);
        String gear;
        try {
            gear = GEARS[dyn.getItemDamage() - 3];
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
        ItemStack cap = inv.getStackInSlot(7);

        boolean valid = (inv.getStackInSlot(1).getItem() == WtfItems.itemRot);
        valid &= LibDict.matches(inv.getStackInSlot(3), gear);
        valid &= (dyn.getItem() == WtfItems.itemDyn && dyn.getItemDamage() > 2);
        valid &= LibDict.matches(inv.getStackInSlot(5), gear);
        valid &= (cap.getItem() == WtfItems.itemCap && cap.getItemDamage() > 2);
        valid &= checkLeads(inv.getStackInSlot(6), inv.getStackInSlot(8));
        return valid;
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
        ItemStack dyn = inv.getStackInSlot(4);
        ItemStack cap = inv.getStackInSlot(7);

        ItemStack res = new ItemStack(WtfBlocks.blockGen, 1, gen.getItemDamage());
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte(LibNBT.GENTYPE, (byte)gen.getItemDamage());
        tag.setByte(LibNBT.DYNTYPE, (byte)(dyn.getItemDamage() - 3));
        tag.setByte(LibNBT.CAPTYPE, (byte)(cap.getItemDamage() - 3));
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
