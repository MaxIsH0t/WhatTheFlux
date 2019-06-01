package io.github.phantamanta44.wtflux.lib;

import net.minecraft.util.text.translation.I18n;

public class LibLang {

    public static final String CREATIVE_TAB_NAME = "tab_" + LibCore.MODID;

    public static final String RES_NAME = "ITEM_RES";
    public static final String MISC_NAME = "itemMisc";
    public static final String DYNAMO_NAME = "itemDyn";
    public static final String GEN_COMPONENT_NAME = "itemGen";
    public static final String CAPACITOR_NAME = "itemCap";
    public static final String REACTOR_COMPONENT_NAME = "itemRct";

    public static final String METAL_BLOCK_NAME = "blockMetal";
    public static final String ORE_NAME = "blockOre";
    public static final String GENERATOR_BLOCK_NAME = "BLOCK_GENerator";
    public static final String SENSOR_BLOCK_NAME = "blockSensor";

    public static final String INF_KEY = LibCore.MODID + ".itemInfo.";
    public static final String INF_NO_TAG = INF_KEY + "noTag";
    public static final String INF_DURA = INF_KEY + "durability";
    public static final String INF_CHARGE = INF_KEY + "charge";
    public static final String INF_EXPAND = INF_KEY + "expand";
    public static final String INF_GEN = INF_KEY + "generator";
    public static final String INF_DYN = INF_KEY + "dynamo";
    public static final String INF_CAP = INF_KEY + "capacitor";
    public static final String INF_CASING = INF_KEY + "casing";
    public static final String INF_MP = INF_KEY + "meltingPoint";
    public static final String INF_RPM_CAP = INF_KEY + "rpmCap";

    public static final String TT_KEY = LibCore.MODID + ".tooltip.";
    public static final String TT_ENERGY = TT_KEY + "energyStored";
    public static final String TT_TEMP = TT_KEY + "temperature";
    public static final String TT_MOM = TT_KEY + "momentum";
    public static final String TT_FUEL = TT_KEY + "fuel";
    public static final String TT_WASTE = TT_KEY + "waste";
    public static final String TT_EMPTY = TT_KEY + "empty";

    public static final String GUI_KEY = LibCore.MODID + ".gui.";
    public static final String GUI_GEN_FURNACE = GUI_KEY + "genFurnace";
    public static final String GUI_GEN_HEAT = GUI_KEY + "genHeat";
    public static final String GUI_GEN_WIND = GUI_KEY + "genWind";
    public static final String GUI_GEN_WATER = GUI_KEY + "genWater";
    public static final String GUI_GEN_NUKE = GUI_KEY + "genNuke";
    public static final String GUI_GEN_SOLAR = GUI_KEY + "genSolar";
    public static final String GUI_SENSOR_TEMP = GUI_KEY + "sensorTemp";
    public static final String GUI_SENSOR_ENERGY = GUI_KEY + "sensorEnergy";
    public static final String GUI_SENSOR_RPM = GUI_KEY + "sensorRpm";

    public static final String NG_KEY = LibCore.MODID + ".nukeStatus.";
    public static final String NG_GOOD = NG_KEY + "good";
    public static final String NG_OFF = NG_KEY + "off";
    public static final String NG_NOFUEL = NG_KEY + "noFuel";
    public static final String NG_FULLWASTE = NG_KEY + "fullWaste";
    public static final String NG_NOCOOL = NG_KEY + "noCoolant";
    public static final String NG_NOHOW = NG_KEY + "noHowitzer";
    public static final String NG_NOCTRL = NG_KEY + "noControlRod";
    public static final String NG_FULLBUF = NG_KEY + "fullPower";

    public static final String PLAYER_INV = "container.inventory";

    public static final String[] GENTYPE = new String[] {".furnace", ".heat", ".wind", ".water", ".nuke", ".solar"};

    public static String get(String key) {
        return I18n.translateToLocal(key);
    }

    public static String getGenType(int type) {
        return get(INF_GEN + GENTYPE[type]);
    }

    public static String getDynType(int lvl) {
        return get(INF_DYN + "." + lvl);
    }

    public static String getCapType(int lvl) {
        return get(INF_CAP + "." + lvl);
    }

    public static String getCasingType(int lvl) {
        return get(INF_CASING + "." + lvl);
    }

}
