package io.github.phantamanta44.wtflux.crafting;

import cofh.api.util.ThermalExpansionHelper;
import io.github.phantamanta44.wtflux.block.BlockCompressed;
import io.github.phantamanta44.wtflux.block.BlockOre;
import io.github.phantamanta44.wtflux.init.ModBlocks;
import io.github.phantamanta44.wtflux.init.ModItems;
import io.github.phantamanta44.wtflux.item.ItemResource;
import io.github.phantamanta44.wtflux.lib.LibDict;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class IngotRecipeHandler {

    protected static void registerRecipes() {
        addIngot(new ItemStack(ModBlocks.blockMetal, 1, BlockCompressed.ZINC), ModItems.itemRes, ItemResource.INGOT_ZINC, LibDict.ZINC, 4000, new ItemStack(ModBlocks.blockOre, 1, BlockOre.ZINC));
        addIngot(new ItemStack(ModBlocks.blockMetal, 1, BlockCompressed.URAN), ModItems.itemRes, ItemResource.INGOT_URAN, LibDict.URAN, 4000, new ItemStack(ModBlocks.blockOre, 1, BlockOre.URAN));
    }

    private static void addIngot(ItemStack block, Item ingot, int meta, String dict, int xp) {
        addIngot(block, ingot, meta, dict, xp, null);
    }

    private static void addIngot(ItemStack block, Item ingot, int meta, String dict, int xp, ItemStack ore) {
        registerOredictEntry(block, ingot, meta, dict);
        addNuggetRecipe(new ItemStack(ingot, 1, meta), LibDict.INGOT + dict, new ItemStack(ingot, 1, meta + 2), LibDict.NUGGET + dict);
        addGearRecipe(new ItemStack(ingot, 1, meta + 3), LibDict.INGOT + dict);
        addSmelting(new ItemStack(ingot, 1, meta), new ItemStack(ingot, 1, meta + 1), xp);
        addLargeStorageRecipe(block, LibDict.BLOCK + dict, new ItemStack(ingot, 1, meta), LibDict.INGOT + dict);
        ThermalExpansionHelper.addPulverizerRecipe(2400, new ItemStack(ingot, 1, meta), new ItemStack(ingot, 1, meta + 1));
        if (ore != null) {
            addSmelting(new ItemStack(ingot, 1, meta), ore, xp);
            ThermalExpansionHelper.addPulverizerRecipe(4000, ore, new ItemStack(ingot, 2, meta + 1));
        }
    }

    private static void registerOredictEntry(ItemStack block, Item ingot, int meta, String dict) {
        OreDictionary.registerOre(LibDict.BLOCK + dict, block);
        OreDictionary.registerOre(LibDict.INGOT + dict, new ItemStack(ingot, 1, meta));
        OreDictionary.registerOre(LibDict.DUST + dict, new ItemStack(ingot, 1, meta + 1));
        OreDictionary.registerOre(LibDict.NUGGET + dict, new ItemStack(ingot, 1, meta + 2));
        OreDictionary.registerOre(LibDict.GEAR + dict, new ItemStack(ingot, 1, meta + 3));
    }

    private static void addNuggetRecipe(ItemStack output, ItemStack input) {
        addOreDictRecipe(output, "ggg", "ggg", "ggg", 'g', input);
        addShapelessOreDictRecipe(new ItemStack(input.getItem(), 9, input.getItemDamage()), output);
    }

    private static void addNuggetRecipe(ItemStack output, ItemStack input, String oreDictIn) {
        addOreDictRecipe(output, "ggg", "ggg", "ggg", 'g', oreDictIn);
        addShapelessOreDictRecipe(new ItemStack(input.getItem(), 9, input.getItemDamage()), output);
    }

    private static void addNuggetRecipe(ItemStack output, String oreDictOut, ItemStack input, String oreDictIn) {
        addOreDictRecipe(output, "ggg", "ggg", "ggg", 'g', oreDictIn);
        addShapelessOreDictRecipe(new ItemStack(input.getItem(), 9, input.getItemDamage()), oreDictOut);
    }

    private static void addSmelting(ItemStack output, ItemStack input, int xp) {
        MasterRecipeManager.addSmelting(output, input, xp);
    }

    private static void addSmallStorageRecipe(ItemStack output, ItemStack input) {
        addOreDictRecipe(output, "rr", "rr", 'r', input);
        addShapelessOreDictRecipe(new ItemStack(input.getItem(), 4, input.getItemDamage()), output);
    }

    private static void addLargeStorageRecipe(ItemStack output, ItemStack input) {
        addOreDictRecipe(output, "ggg", "ggg", "ggg", 'g', input);
        addShapelessOreDictRecipe(new ItemStack(input.getItem(), 9, input.getItemDamage()), output);
    }

    private static void addSmallStorageRecipe(ItemStack output, ItemStack input, String oreDictIn) {
        addOreDictRecipe(output, "rr", "rr", 'r', oreDictIn);
        addShapelessOreDictRecipe(new ItemStack(input.getItem(), 4, input.getItemDamage()), output);
    }

    private static void addLargeStorageRecipe(ItemStack output, ItemStack input, String oreDictIn) {
        addOreDictRecipe(output, "ggg", "ggg", "ggg", 'g', oreDictIn);
        addShapelessOreDictRecipe(new ItemStack(input.getItem(), 9, input.getItemDamage()), output);
    }

    private static void addSmallStorageRecipe(ItemStack output, String oreDictOut, ItemStack input, String oreDictIn) {
        addOreDictRecipe(output, "rr", "rr", 'r', oreDictIn);
        addShapelessOreDictRecipe(new ItemStack(input.getItem(), 4, input.getItemDamage()), oreDictOut);
    }

    private static void addLargeStorageRecipe(ItemStack output, String oreDictOut, ItemStack input, String oreDictIn) {
        addOreDictRecipe(output, "ggg", "ggg", "ggg", 'g', oreDictIn);
        addShapelessOreDictRecipe(new ItemStack(input.getItem(), 9, input.getItemDamage()), oreDictOut);
    }

    private static void addGearRecipe(ItemStack gear, Object mat) {
        addOreDictRecipe(gear, " m ", "mim", " m ", 'i', LibDict.INGOT_IRON, 'm', mat);
    }

    private static void addOreDictRecipe(ItemStack output, Object... recipe) {
        MasterRecipeManager.addOreDictRecipe(output, recipe);
    }

    private static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
        MasterRecipeManager.addShapelessOreDictRecipe(output, recipe);
    }

}