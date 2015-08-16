package io.github.phantamanta44.wtflux.lib;

import net.minecraft.util.StatCollector;

public class LibLang {

	public static final String CREATIVE_TAB_NAME = "tab_" + LibCore.MODID;
	
    public static final String RES_NAME = "itemRes";
    public static final String MISC_NAME = "itemMisc";
    public static final String DYNAMO_NAME = "itemDyn";
    public static final String GEN_COMPONENT_NAME = "itemGen";
    public static final String CAPACITOR_NAME = "itemCap";
    public static final String REACTOR_COMPONENT_NAME = "itemRct";
	
	public static final String METAL_BLOCK_NAME = "blockMetal";
	public static final String ORE_NAME = "blockOre";
	public static final String GENERATOR_BLOCK_NAME = "blockGenerator";
	
	public static final String INF_KEY = LibCore.MODID + ".itemInfo.";
	public static final String INF_NO_TAG = INF_KEY + "noTag";
	public static final String INF_DURA = INF_KEY + "durability";
	public static final String INF_CHARGE = INF_KEY + "charge";
	public static final String INF_EXPAND = INF_KEY + "expand";
	public static final String INF_GEN = INF_KEY + "generator";
	public static final String INF_DYN = INF_KEY + "dynamo";
	public static final String INF_CAP = INF_KEY + "capacitor";
	
	public static final String[] GENTYPE = new String[] {".furnace", ".heat", ".wind", ".water", ".nuke", ".solar"};
	
	public static String get(String key) {
		return StatCollector.translateToLocal(key);
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
	
}
