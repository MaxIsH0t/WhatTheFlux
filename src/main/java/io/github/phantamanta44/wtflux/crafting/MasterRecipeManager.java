package io.github.phantamanta44.wtflux.crafting;

import cofh.api.util.ThermalExpansionHelper;
import io.github.phantamanta44.wtflux.block.BlockSensor;
import io.github.phantamanta44.wtflux.crafting.recipe.GeneratorRecipe;
import io.github.phantamanta44.wtflux.init.ModBlocks;
import io.github.phantamanta44.wtflux.init.ModItems;
import io.github.phantamanta44.wtflux.item.*;
import io.github.phantamanta44.wtflux.lib.LibDict;
import io.github.phantamanta44.wtflux.util.ModUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public final class MasterRecipeManager {

    public static void addRecipes() {
        OreDictionary.registerOre(LibDict.INGOT_GRAPH, new ItemStack(ModItems.itemMisc, 1, ItemMisc.GRAPHITE));
        OreDictionary.registerOre(LibDict.DUST_GRAPH, new ItemStack(ModItems.itemMisc, 1, ItemMisc.RAW_GRAPHITE));
        OreDictionary.registerOre(LibDict.DUST_BARIUM, new ItemStack(ModItems.itemRct, 1, ItemReactor.WASTE));
        IngotRecipeHandler.registerRecipes();

        // Copper Thread
        addOreDictRecipe(new ItemStack(ModItems.itemMisc, 2, ItemMisc.COPPER_THREAD), "ccc", 'c', LibDict.NUGGET_COPPER);

        // Iron Rod
        addOreDictRecipe(new ItemStack(ModItems.itemMisc, 1, ItemMisc.IRON_ROD), "n", "i", "n", 'n', LibDict.NUGGET_IRON, 'i', LibDict.INGOT_IRON);

        // Solenoid
        addOreDictRecipe(new ItemStack(ModItems.itemMisc, 1, ItemMisc.SOLENOID), "ccc", "cic", "ccc", 'c', new ItemStack(ModItems.itemMisc, 1, ItemMisc.COPPER_THREAD), 'i', new ItemStack(ModItems.itemMisc, 1, ItemMisc.IRON_ROD));

        // Cathode
        addOreDictRecipe(new ItemStack(ModItems.itemMisc, 1, ItemMisc.CATHODE), "c", "w", "i", 'c', LibDict.INGOT_COPPER, 'w', new ItemStack(ModItems.itemMisc, 1, ItemMisc.COPPER_THREAD), 'i', LibDict.NUGGET_IRON);

        // Anode
        addOreDictRecipe(new ItemStack(ModItems.itemMisc, 1, ItemMisc.ANODE), "c", "w", "i", 'c', LibDict.INGOT_ZINC, 'w', new ItemStack(ModItems.itemMisc, 1, ItemMisc.COPPER_THREAD), 'i', LibDict.NUGGET_IRON);

        // Heat Conductor
        for (ItemStack ore : OreDictionary.getOres(LibDict.DUST_LEAD)) {
            for (ItemStack ore2 : OreDictionary.getOres(LibDict.INGOT_COPPER))
                ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(ore.getItem(), 2, ore.getItemDamage()), new ItemStack(ore2.getItem(), 1, ore2.getItemDamage()), new ItemStack(ModItems.itemMisc, 1, ItemMisc.HEAT_COND));
        }

        // Turbine Blade
        addOreDictRecipe(new ItemStack(ModItems.itemMisc, 1, ItemMisc.TURBINE), "in", "im", "bn", 'i', LibDict.NUGGET_IRON, 'b', new ItemStack(ModItems.itemMisc, 1, ItemMisc.IRON_ROD), 'n', LibDict.NUGGET_INVAR, 'm', LibDict.INGOT_INVAR);

        // Industrial Mirror
        addOreDictRecipe(new ItemStack(ModItems.itemMisc, 1, ItemMisc.MIRROR), "bs", "bs", "bs", 's', LibDict.DUST_SILVER, 'b', LibDict.INGOT_NICKEL);
        addOreDictRecipe(new ItemStack(ModItems.itemMisc, 1, ItemMisc.MIRROR), "sb", "sb", "sb", 's', LibDict.DUST_SILVER, 'b', LibDict.INGOT_NICKEL);

        // Carborundum
        ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(Items.CLAY_BALL, 2), new ItemStack(Items.COAL), new ItemStack(ModItems.itemMisc, 1, ItemMisc.RAW_GRAPHITE));

        // Graphite
        addSmelting(new ItemStack(ModItems.itemMisc, 1, ItemMisc.GRAPHITE), new ItemStack(ModItems.itemMisc, 1, ItemMisc.RAW_GRAPHITE), 0);

        // Granular ethylethylene
        for (ItemStack coal : OreDictionary.getOres(LibDict.ORE_COAL))
            ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(coal.getItem(), 1, coal.getItemDamage()), new ItemStack(Items.FLINT, 2), new ItemStack(Items.COAL, 5), new ItemStack(ModItems.itemMisc, 2, ItemMisc.PROPENE), 30);

        // Polypropylene
        for (ItemStack nickel : OreDictionary.getOres(LibDict.DUST_NICKEL))
            ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(ModItems.itemMisc, 4, ItemMisc.PROPENE), new ItemStack(nickel.getItem(), 1, nickel.getItemDamage()), new ItemStack(ModItems.itemMisc, 4, ItemMisc.PLASTIC));

        // Ni-MH Cell Core
        for (ItemStack bucket : OreDictionary.getOres(LibDict.BUCKET_PYRO))
            addOreDictRecipe(new ItemStack(ModItems.itemMisc, 1, ItemMisc.BATTERY_CORE), "pnp", "cbc", "pap", 'p', new ItemStack(ModItems.itemMisc, 1, ItemMisc.PLASTIC), 'n', LibDict.GEAR_NICKEL, 'b', new ItemStack(bucket.getItem(), 1, bucket.getItemDamage()), 'a', LibDict.DUST_BARIUM, 'c', new ItemStack(ModItems.itemRct, 1, ItemReactor.COOLANT_CELL));

        // Copper Coils
        for (int i = 0; i < 3; i++)
            addOreDictRecipe(new ItemStack(ModItems.itemDyn, 1, i), "ccc", "c c", "ccc", 'c', i == 0 ? new ItemStack(ModItems.itemMisc, 1, ItemMisc.COPPER_THREAD) : new ItemStack(ModItems.itemDyn, 1, i - 1));

        // Dynamos
        addOreDictRecipe(new ItemStack(ModItems.itemDyn, 1, ItemDynamo.DYN_1), "csc", 'c', new ItemStack(ModItems.itemDyn, 1, ItemDynamo.COIL_3), 's', new ItemStack(ModItems.itemMisc, 1, ItemMisc.SOLENOID));
        addOreDictRecipe(new ItemStack(ModItems.itemDyn, 1, ItemDynamo.DYN_1), "c", "s", "c", 'c', new ItemStack(ModItems.itemDyn, 1, ItemDynamo.COIL_3), 's', new ItemStack(ModItems.itemMisc, 1, ItemMisc.SOLENOID));
        addOreDictRecipe(new ItemStack(ModItems.itemDyn, 1, ItemDynamo.DYN_2), " c ", "csc", " c ", 'c', new ItemStack(ModItems.itemDyn, 1, ItemDynamo.COIL_3), 's', new ItemStack(ModItems.itemMisc, 1, ItemMisc.SOLENOID));
        addOreDictRecipe(new ItemStack(ModItems.itemDyn, 1, ItemDynamo.DYN_3), "ccc", "csc", "ccc", 'c', new ItemStack(ModItems.itemDyn, 1, ItemDynamo.COIL_3), 's', new ItemStack(ModItems.itemMisc, 1, ItemMisc.SOLENOID));
        addOreDictRecipe(new ItemStack(ModItems.itemDyn, 1, ItemDynamo.DYN_4), "wew", "csc", "wew", 'c', new ItemStack(ModItems.itemDyn, 1, ItemDynamo.DYN_3), 's', new ItemStack(ModItems.itemMisc, 1, ItemMisc.SOLENOID), 'e', LibDict.GEAR_END, 'w', new ItemStack(ModItems.itemRct, 1, ItemReactor.COOLANT_CELL));

        // Heat Furnace
        addOreDictRecipe(new ItemStack(ModItems.itemRot, 1, ItemRotary.FURN), "ppp", "bfb", 'f', new ItemStack(Blocks.FURNACE), 'p', new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), 'b', new ItemStack(Blocks.BRICK_BLOCK));

        // Heat Sink
        addOreDictRecipe(new ItemStack(ModItems.itemRot, 1, ItemRotary.HEAT), "rrr", "iti", 'r', new ItemStack(ModItems.itemMisc, 1, ItemMisc.IRON_ROD), 'i', LibDict.INGOT_IRON, 't', new ItemStack(ModItems.itemMisc, 1, ItemMisc.HEAT_COND));

        // Wind Turbine
        addOreDictRecipe(new ItemStack(ModItems.itemRot, 1, ItemRotary.WIND), " t ", " i ", "t t", 't', new ItemStack(ModItems.itemMisc, 1, ItemMisc.TURBINE), 'i', new ItemStack(ModItems.itemMisc, 1, ItemMisc.IRON_ROD));

        // Water Turbine
        addOreDictRecipe(new ItemStack(ModItems.itemRot, 1, ItemRotary.WATER), " t ", "tit", " t ", 't', new ItemStack(ModItems.itemMisc, 1, ItemMisc.TURBINE), 'i', LibDict.INGOT_COPPER);

        // Fission Reactor
        addOreDictRecipe(new ItemStack(ModItems.itemRot, 1, ItemRotary.NUKE), "crc", "hvf", "ccc", 'c', new ItemStack(ModItems.itemRct, 1, ItemReactor.CASING), 'r', new ItemStack(ModItems.itemRct, 1, ItemReactor.ROD_CRADLE), 'h', new ItemStack(ModItems.itemRct, 1, ItemReactor.HOW_CRADLE), 'v', new ItemStack(ModItems.itemRct, 1, ItemReactor.RPV), 'f', new ItemStack(ModItems.itemRct, 1, ItemReactor.CONDENSER));
        addOreDictRecipe(new ItemStack(ModItems.itemRot, 1, ItemRotary.NUKE), "crc", "fvh", "ccc", 'c', new ItemStack(ModItems.itemRct, 1, ItemReactor.CASING), 'r', new ItemStack(ModItems.itemRct, 1, ItemReactor.ROD_CRADLE), 'h', new ItemStack(ModItems.itemRct, 1, ItemReactor.HOW_CRADLE), 'v', new ItemStack(ModItems.itemRct, 1, ItemReactor.RPV), 'f', new ItemStack(ModItems.itemRct, 1, ItemReactor.CONDENSER));

        // Parabolic Trough
        addOreDictRecipe(new ItemStack(ModItems.itemRot, 1, ItemRotary.SOLAR), "ggg", "mbm", "hmh", 'g', LibDict.GLASS_PANE, 'h', new ItemStack(ModItems.itemMisc, 1, ItemMisc.HEAT_COND), 'm', new ItemStack(ModItems.itemMisc, 1, ItemMisc.MIRROR), 'b', new ItemStack(Items.BUCKET));

        // Ceramic Dielectric
        ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(Items.CLAY_BALL, 2), new ItemStack(Blocks.SAND), new ItemStack(ModItems.itemCap, 2, ItemCapacitor.DIELEC_1));

        // Plastic Film Dielectric
        addShapelessOreDictRecipe(new ItemStack(ModItems.itemCap, 1, ItemCapacitor.DIELEC_2), new ItemStack(ModItems.itemMisc, 1, ItemMisc.PLASTIC), new ItemStack(ModItems.itemMisc, 1, ItemMisc.PLASTIC), LibDict.DUST_TIN);

        // Zinc Oxide Dielectric
        for (ItemStack zinc : OreDictionary.getOres(LibDict.NUGGET_ZINC))
            ThermalExpansionHelper.addSmelterRecipe(4000, new ItemStack(zinc.getItem(), 64, zinc.getItemDamage()), new ItemStack(Items.FLINT, 4), new ItemStack(ModItems.itemCap, 1, ItemCapacitor.DIELEC_3));

        // Ceramic Capacitor
        addOreDictRecipe(new ItemStack(ModItems.itemCap, 1, ItemCapacitor.CAP_1), "ndn", "ndn", "a c", 'n', LibDict.NUGGET_ELEC, 'd', new ItemStack(ModItems.itemCap, 1, ItemCapacitor.DIELEC_1), 'a', new ItemStack(ModItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(ModItems.itemMisc, 1, ItemMisc.CATHODE));
        addOreDictRecipe(new ItemStack(ModItems.itemCap, 1, ItemCapacitor.CAP_1), "ndn", "ndn", "c a", 'n', LibDict.NUGGET_ELEC, 'd', new ItemStack(ModItems.itemCap, 1, ItemCapacitor.DIELEC_1), 'a', new ItemStack(ModItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(ModItems.itemMisc, 1, ItemMisc.CATHODE));

        // Film Capacitor
        addOreDictRecipe(new ItemStack(ModItems.itemCap, 1, ItemCapacitor.CAP_2), "ndn", "ndn", "a c", 'n', LibDict.INGOT_NICKEL, 'd', new ItemStack(ModItems.itemCap, 1, ItemCapacitor.DIELEC_2), 'a', new ItemStack(ModItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(ModItems.itemMisc, 1, ItemMisc.CATHODE));
        addOreDictRecipe(new ItemStack(ModItems.itemCap, 1, ItemCapacitor.CAP_2), "ndn", "ndn", "c a", 'n', LibDict.INGOT_NICKEL, 'd', new ItemStack(ModItems.itemCap, 1, ItemCapacitor.DIELEC_2), 'a', new ItemStack(ModItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(ModItems.itemMisc, 1, ItemMisc.CATHODE));

        // Electrolytic Capacitor
        addOreDictRecipe(new ItemStack(ModItems.itemCap, 1, ItemCapacitor.CAP_3), " a ", "dnd", " c ", 'n', LibDict.INGOT_SIG, 'd', new ItemStack(ModItems.itemCap, 1, ItemCapacitor.DIELEC_3), 'a', new ItemStack(ModItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(ModItems.itemMisc, 1, ItemMisc.CATHODE));
        addOreDictRecipe(new ItemStack(ModItems.itemCap, 1, ItemCapacitor.CAP_3), " c ", "dnd", " a ", 'n', LibDict.INGOT_SIG, 'd', new ItemStack(ModItems.itemCap, 1, ItemCapacitor.DIELEC_3), 'a', new ItemStack(ModItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(ModItems.itemMisc, 1, ItemMisc.CATHODE));

        // Double Capacitors
        for (int i = 0; i < 3; i++) {
            addOreDictRecipe(new ItemStack(ModItems.itemCap, 1, i + 6), "dfd", "lgl", "a c", 'd', new ItemStack(ModItems.itemCap, 1, i), 'l', new ItemStack(ModItems.itemCap, 1, i + 3), 'g', GeneratorRecipe.GEARS[i + 1], 'a', new ItemStack(ModItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(ModItems.itemMisc, 1, ItemMisc.CATHODE), 'f', LibDict.INGOT_SIG);
            addOreDictRecipe(new ItemStack(ModItems.itemCap, 1, i + 6), "dfd", "lgl", "c a", 'd', new ItemStack(ModItems.itemCap, 1, i), 'l', new ItemStack(ModItems.itemCap, 1, i + 3), 'g', GeneratorRecipe.GEARS[i + 1], 'a', new ItemStack(ModItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(ModItems.itemMisc, 1, ItemMisc.CATHODE), 'f', LibDict.INGOT_SIG);
        }

        // Nickel-Metal Hydride Battery
        addOreDictRecipe(new ItemStack(ModItems.itemCap, 1, ItemCapacitor.CAP_7), " a ", "tbt", "tct", 'a', new ItemStack(ModItems.itemMisc, 1, ItemMisc.ANODE), 'c', new ItemStack(ModItems.itemMisc, 1, ItemMisc.CATHODE), 't', LibDict.INGOT_TIN, 'b', new ItemStack(ModItems.itemMisc, 1, ItemMisc.BATTERY_CORE));

        // Neutron Howitzer
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.BLASTER), "pp ", "v c", "ppp", 'c', LibDict.GEAR_URAN, 'v', LibDict.GEM_EMERALD, 'p', new ItemStack(ModItems.itemRct, 1, ItemReactor.PLATE));
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.BLASTER), " pp", "c v", "ppp", 'c', LibDict.GEAR_URAN, 'v', LibDict.GEM_EMERALD, 'p', new ItemStack(ModItems.itemRct, 1, ItemReactor.PLATE));

        // Control Rod
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.CONTROL_ROD), "gsg", "gmg", "gsg", 'g', LibDict.INGOT_GRAPH, 's', LibDict.INGOT_SILVER, 'm', LibDict.GEAR_SILVER);

        // Coolant Cell
        for (ItemStack cryo : OreDictionary.getOres(LibDict.BUCKET_CRYO))
            addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.COOLANT_CELL), "php", "pcp", " g ", 'p', new ItemStack(ModItems.itemRct, 1, ItemReactor.PLATE), 'h', new ItemStack(ModItems.itemMisc, 1, ItemMisc.HEAT_COND), 'c', new ItemStack(cryo.getItem(), 1, cryo.getItemDamage()), 'g', LibDict.INGOT_END);

        // Reactor Plating
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 4, ItemReactor.PLATE), "vlv", "lil", "vlv", 'v', LibDict.INGOT_INVAR, 'l', LibDict.INGOT_LEAD, 'i', new ItemStack(ModItems.itemMisc, 1, ItemMisc.IRON_ROD));

        // Reactor Core
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.CORE), "pgp", "pup", "pgp", 'p', new ItemStack(ModItems.itemRct, 1, ItemReactor.PLATE), 'g', LibDict.INGOT_GRAPH, 'u', LibDict.GEAR_URAN);

        // Fluid Condenser
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.CONDENSER), "iii", "v v", "ici", 'i', LibDict.INGOT_IRON, 'v', new ItemStack(ModItems.itemRct, 1, ItemReactor.VALVE), 'c', new ItemStack(ModItems.itemRct, 1, ItemReactor.COOLANT_CELL));

        // Reactor Pressurized Vessel
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.RPV), "pvp", "pcp", "pip", 'p', new ItemStack(ModItems.itemRct, 1, ItemReactor.PLATE), 'c', new ItemStack(ModItems.itemRct, 1, ItemReactor.CORE), 'i', new ItemStack(ModItems.itemRct, 1, ItemReactor.INJECTOR), 'v', new ItemStack(ModItems.itemRct, 1, ItemReactor.VALVE));

        // Coolant Injector
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.INJECTOR), " pp", "c v", "ppp", 'c', new ItemStack(ModItems.itemRct, 1, ItemReactor.COOLANT_CELL), 'v', new ItemStack(ModItems.itemRct, 1, ItemReactor.VALVE), 'p', new ItemStack(ModItems.itemMisc, 1, ItemMisc.PLASTIC));
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.INJECTOR), "pp ", "v c", "ppp", 'c', new ItemStack(ModItems.itemRct, 1, ItemReactor.COOLANT_CELL), 'v', new ItemStack(ModItems.itemRct, 1, ItemReactor.VALVE), 'p', new ItemStack(ModItems.itemMisc, 1, ItemMisc.PLASTIC));

        // Pressure Valve
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.VALVE), " l ", "pvp", 'l', new ItemStack(Blocks.LEVER), 'p', new ItemStack(ModItems.itemRct, 1, ItemReactor.PLATE), 'v', new ItemStack(Blocks.PISTON));

        // Neutron Howitzer Cradle
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.HOW_CRADLE), "ppp", "w  ", "ppp", 'p', new ItemStack(ModItems.itemRct, 1, ItemReactor.PLATE), 'w', new ItemStack(ModItems.itemMisc, 1, ItemMisc.PLASTIC));
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.HOW_CRADLE), "ppp", "  w", "ppp", 'p', new ItemStack(ModItems.itemRct, 1, ItemReactor.PLATE), 'w', new ItemStack(ModItems.itemMisc, 1, ItemMisc.PLASTIC));

        // Control Rod Cradle
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.ROD_CRADLE), "pvp", "p p", "p p", 'p', new ItemStack(ModItems.itemRct, 1, ItemReactor.PLATE), 'v', new ItemStack(Blocks.PISTON));

        // Reactor Casing
        addOreDictRecipe(new ItemStack(ModItems.itemRct, 1, ItemReactor.CASING), " p ", "pgp", " p ", 'p', new ItemStack(ModItems.itemRct, 1, ItemReactor.PLATE), 'c', LibDict.DUST_GRAPH);

        // Generator Thermometer
        ItemStack coilElectrum = new ItemStack(ModUtil.findItem("ThermalExpansion", "material"), 1, 3);
        addOreDictRecipe(new ItemStack(ModBlocks.blockSensor, 1, BlockSensor.TEMP), "igi", "rmr", "iei", 'i', LibDict.INGOT_IRON, 'g', LibDict.GLASS, 'e', coilElectrum, 'm', new ItemStack(ModItems.itemRot, 1, ItemRotary.HEAT), 'r', LibDict.REDSTONE);

        // Generator Voltometer
        addOreDictRecipe(new ItemStack(ModBlocks.blockSensor, 1, BlockSensor.ENERGY), "igi", "rmr", "iei", 'i', LibDict.INGOT_IRON, 'g', LibDict.GLASS, 'e', coilElectrum, 'm', new ItemStack(ModUtil.findItem("ThermalExpansion", "meter")), 'r', LibDict.REDSTONE);

        // Generator Dynamometer
        addOreDictRecipe(new ItemStack(ModBlocks.blockSensor, 1, BlockSensor.RPM), "igi", "rmr", "iei", 'i', LibDict.INGOT_IRON, 'g', LibDict.GLASS, 'e', coilElectrum, 'm', new ItemStack(ModItems.itemMisc, 1, ItemMisc.SOLENOID), 'r', LibDict.REDSTONE);

        //GameRegistry.addShapedRecipe(new GeneratorRecipe());
    }

    protected static void addSmelting(ItemStack output, ItemStack input, int xp) {
        GameRegistry.addSmelting(input, output, xp);
    }

    protected static void addOreDictRecipe(ItemStack output, Object... recipe) {
        //ModUtil.getRecipeList().add(new ShapedOreRecipe(output, recipe));
    }

    protected static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
        //ModUtil.getRecipeList().add(new ShapelessOreRecipe(output, recipe));
    }

}
