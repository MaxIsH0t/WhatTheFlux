package io.github.phantamanta44.wtflux.recipe;

import io.github.phantamanta44.wtflux.item.ItemCapacitor;
import io.github.phantamanta44.wtflux.item.ItemMisc;
import io.github.phantamanta44.wtflux.item.ItemRotary;
import io.github.phantamanta44.wtflux.item.WtfItems;
import io.github.phantamanta44.wtflux.lib.LibDict;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cofh.api.modhelpers.ThermalExpansionHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public final class MasterRecipeManager {
	
	public static void addRecipes() {
		OreDictionary.registerOre(LibDict.INGOT_GRAPH, new ItemStack(WtfItems.itemMisc, 1, ItemMisc.GRAPHITE));
		OreDictionary.registerOre(LibDict.DUST_GRAPH, new ItemStack(WtfItems.itemMisc, 1, ItemMisc.RAW_GRAPHITE));
		IngotRecipeHandler.registerRecipes();
		
		// Copper Thread
		addOreDictRecipe(new ItemStack(WtfItems.itemMisc, 2, ItemMisc.COPPER_THREAD), "ccc", 'c', LibDict.NUGGET_COPPER);
		
		// Iron Rod
		addOreDictRecipe(new ItemStack(WtfItems.itemMisc, 1, ItemMisc.IRON_ROD), "n", "i", "n", 'n', LibDict.NUGGET_IRON, 'i', LibDict.INGOT_IRON);
		
		// Solenoid
		addOreDictRecipe(new ItemStack(WtfItems.itemMisc, 1, ItemMisc.SOLENOID), "ccc", "cic", "ccc", 'c', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.COPPER_THREAD), 'i', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.IRON_ROD));
		
		// Cathode
		addOreDictRecipe(new ItemStack(WtfItems.itemMisc, 1, ItemMisc.CATHODE), "c", "w", "i", 'c', LibDict.INGOT_COPPER, 'w', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.COPPER_THREAD), 'i', LibDict.NUGGET_IRON);
		
		// Anode
		addOreDictRecipe(new ItemStack(WtfItems.itemMisc, 1, ItemMisc.ANODE), "c", "w", "i", 'c', LibDict.INGOT_ZINC, 'w', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.COPPER_THREAD), 'i', LibDict.NUGGET_IRON);
		
		// Heat Conductor
		for (ItemStack ore : OreDictionary.getOres(LibDict.DUST_LEAD)) {
			for (ItemStack ore2 : OreDictionary.getOres(LibDict.INGOT_COPPER))
				ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(ore.getItem(), 2, ore.getItemDamage()), new ItemStack(ore2.getItem(), 1, ore2.getItemDamage()), new ItemStack(WtfItems.itemMisc, 1, ItemMisc.HEAT_COND));
		}
		
		// Turbine Blade
		addOreDictRecipe(new ItemStack(WtfItems.itemMisc, 1, ItemMisc.TURBINE), "in", "im", "bn", 'i', LibDict.NUGGET_IRON, 'b', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.IRON_ROD), 'n', LibDict.NUGGET_INVAR, 'm', LibDict.INGOT_INVAR);
		
		// Industrial Mirror
		addOreDictRecipe(new ItemStack(WtfItems.itemMisc, 1, ItemMisc.MIRROR), "bs", "bs", "bs", 's', LibDict.DUST_SILVER, 'b', LibDict.INGOT_NICKEL);
		addOreDictRecipe(new ItemStack(WtfItems.itemMisc, 1, ItemMisc.MIRROR), "sb", "sb", "sb", 's', LibDict.DUST_SILVER, 'b', LibDict.INGOT_NICKEL);
		
		// Carborundum
		ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(Items.clay_ball, 2), new ItemStack(Items.coal), new ItemStack(WtfItems.itemMisc, 1, ItemMisc.RAW_GRAPHITE));
		
		// Graphite
		addSmelting(new ItemStack(WtfItems.itemMisc, 1, ItemMisc.GRAPHITE), new ItemStack(WtfItems.itemMisc, 1, ItemMisc.RAW_GRAPHITE), 0);
		
		// Methylethylene
		for (ItemStack coal : OreDictionary.getOres(LibDict.ORE_COAL))
			ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(coal.getItem(), 1, coal.getItemDamage()), new ItemStack(Items.flint, 2), new ItemStack(Items.coal, 5), new ItemStack(WtfItems.itemMisc, 2, ItemMisc.PROPENE), 30);
		
		// Polypropylene
		for (ItemStack nickel : OreDictionary.getOres(LibDict.DUST_NICKEL))
			ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(WtfItems.itemMisc, 4, ItemMisc.PROPENE), new ItemStack(nickel.getItem(), 1, nickel.getItemDamage()), new ItemStack(WtfItems.itemMisc, 4, ItemMisc.PLASTIC));
		
		// Copper Coils
		for (int i = 0; i < 3; i++)
			addOreDictRecipe(new ItemStack(WtfItems.itemDyn, 1, i), "ccc", "c c", "ccc", 'c', i == 0 ? new ItemStack(WtfItems.itemMisc, 1, ItemMisc.COPPER_THREAD) : new ItemStack(WtfItems.itemDyn, 1, i - 1));
		
		// Dynamos
		for (int i = 0; i < 3; i++)
			addOreDictRecipe(new ItemStack(WtfItems.itemDyn, 1, 3 + i), " c ", "csc", " c ", 'c', new ItemStack(WtfItems.itemDyn, 1, i), 's', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.SOLENOID));
		
		// Heat Furnace
		addOreDictRecipe(new ItemStack(WtfItems.itemRot, 1, ItemRotary.FURN), "ppp", "bfb", 'f', new ItemStack(Blocks.furnace), 'p', new ItemStack(Blocks.heavy_weighted_pressure_plate), 'b', new ItemStack(Blocks.brick_block));
		
		// Heat Sink
		addOreDictRecipe(new ItemStack(WtfItems.itemRot, 1, ItemRotary.HEAT), "rrr", "iti", 'r', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.IRON_ROD), 'i', LibDict.INGOT_IRON, 't', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.HEAT_COND));
		
		// Wind Turbine
		addOreDictRecipe(new ItemStack(WtfItems.itemRot, 1, ItemRotary.WIND), " t ", " i ", "t t", 't', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.TURBINE), 'i', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.IRON_ROD));
		
		// Water Turbine
		addOreDictRecipe(new ItemStack(WtfItems.itemRot, 1, ItemRotary.WATER), " t ", "tit", " t ", 't', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.TURBINE), 'i', LibDict.INGOT_COPPER);
		
		// Reaction Chamber
		addOreDictRecipe(new ItemStack(WtfItems.itemRot, 1, ItemRotary.NUKE), "lgl", "lul", "lgl", 'l', LibDict.INGOT_LEAD, 'g', LibDict.INGOT_GRAPH, 'u', LibDict.GEAR_URAN);
		
		// Parabolic Trough
		addOreDictRecipe(new ItemStack(WtfItems.itemRot, 1, ItemRotary.SOLAR), "ggg", "mbm", "hmh", 'g', LibDict.GLASS_PANE, 'h', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.HEAT_COND), 'm', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.MIRROR), 'b', new ItemStack(Items.bucket));
		
		// Ceramic Dielectric
		ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(Items.clay_ball, 2), new ItemStack(Blocks.sand), new ItemStack(WtfItems.itemCap, 2, ItemCapacitor.DIELEC_1));
		
		// Plastic Film Dielectric
		addShapelessOreDictRecipe(new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.DIELEC_2), new ItemStack(WtfItems.itemMisc, 1, ItemMisc.PLASTIC), new ItemStack(WtfItems.itemMisc, 1, ItemMisc.PLASTIC), LibDict.DUST_TIN);
		
		// Zinc Oxide Dielectric
		for (ItemStack zinc : OreDictionary.getOres(LibDict.NUGGET_ZINC))
			ThermalExpansionHelper.addSmelterRecipe(4000, new ItemStack(zinc.getItem(), 64, zinc.getItemDamage()), new ItemStack(Items.flint, 4), new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.DIELEC_3));
		
		// Ceramic Capacitor
		addOreDictRecipe(new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.CAP_1), "ndn", "ndn", "a c", 'n', LibDict.NUGGET_ELEC, 'd', new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.DIELEC_1), 'a', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.CATHODE));
		addOreDictRecipe(new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.CAP_1), "ndn", "ndn", "c a", 'n', LibDict.NUGGET_ELEC, 'd', new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.DIELEC_1), 'a', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.CATHODE));
		
		// Film Capacitor
		addOreDictRecipe(new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.CAP_2), "ndn", "ndn", "a c", 'n', LibDict.INGOT_NICKEL, 'd', new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.DIELEC_2), 'a', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.CATHODE));
		addOreDictRecipe(new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.CAP_2), "ndn", "ndn", "c a", 'n', LibDict.INGOT_NICKEL, 'd', new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.DIELEC_2), 'a', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.CATHODE));
		
		// Electrolytic Capacitor
		addOreDictRecipe(new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.CAP_3), " a ", "dnd", " c ", 'n', LibDict.INGOT_SIG, 'd', new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.DIELEC_3), 'a', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.CATHODE));
		addOreDictRecipe(new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.CAP_3), " c ", "dnd", " a ", 'n', LibDict.INGOT_SIG, 'd', new ItemStack(WtfItems.itemCap, 1, ItemCapacitor.DIELEC_3), 'a', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(WtfItems.itemMisc, 1, ItemMisc.CATHODE));
	}

	protected static void addSmelting(ItemStack output, ItemStack input, int xp) {
		GameRegistry.addSmelting(input, output, xp);
	}
	
	protected static void addOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
	}

	protected static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, recipe));
	}
	
}
