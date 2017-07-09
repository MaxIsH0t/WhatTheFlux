package io.github.phantamanta44.wtflux.lib;

import net.minecraft.util.ResourceLocation;

public class LibResource {

    public static final String GUI_KEY = LibCore.MODPREF + "textures/gui/";
    public static final ResourceLocation TEX_GUI_FURNACE = new ResourceLocation(GUI_KEY + "gen_furnace.png");
    public static final ResourceLocation TEX_GUI_HEAT = new ResourceLocation(GUI_KEY + "gen_heat.png");
    public static final ResourceLocation TEX_GUI_WIND = new ResourceLocation(GUI_KEY + "gen_wind.png");
    public static final ResourceLocation TEX_GUI_WATER = new ResourceLocation(GUI_KEY + "gen_water.png");
    public static final ResourceLocation TEX_GUI_NUKE = new ResourceLocation(GUI_KEY + "gen_nuke.png");
    public static final ResourceLocation TEX_GUI_SOLAR = new ResourceLocation(GUI_KEY + "gen_solar.png");
    public static final ResourceLocation TEX_GUI_SENSOR = new ResourceLocation(GUI_KEY + "sensor.png");

    public static final String GC_KEY = GUI_KEY + "component/";
    public static final ResourceLocation TEX_GC_BASE = new ResourceLocation(GC_KEY + "base.png");

    public static final ResourceLocation SND_GUI_BUTTON = new ResourceLocation("gui.button.press");

}
