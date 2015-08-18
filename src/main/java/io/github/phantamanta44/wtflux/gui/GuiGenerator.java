package io.github.phantamanta44.wtflux.gui;

import io.github.phantamanta44.wtflux.gui.component.GCEnergyMeter;
import io.github.phantamanta44.wtflux.gui.component.GCGenHeatMeter;
import io.github.phantamanta44.wtflux.gui.component.GCSpeedometer;
import io.github.phantamanta44.wtflux.inventory.ContainerGenerator;
import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.lib.LibResource;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiGenerator<T extends TileGenerator> extends GuiContainerMod {
	
	protected T tile;
	
	public GuiGenerator(InventoryPlayer ipl, T te) {
		super(ContainerGenerator.newInstance(ipl, te));
		comps.add(new GCEnergyMeter(165, 9, 67, te));
		comps.add(new GCSpeedometer(157, 9, te));
		tile = te;
	}
	
	public GuiGenerator(Container cont) {
		super(cont);
		throw new IllegalStateException("This shouldn't happen!");
	}
	
	public static class Furnace extends GuiGenerator<TileGenerator.Furnace> {

		static {
			resLoc = LibResource.TEX_GUI_FURN;
			invName = LibLang.GUI_GEN_FURNACE;
		}
		
		public Furnace(InventoryPlayer ipl, TileGenerator.Furnace te) {
			super(ipl, te);
			comps.add(new GCGenHeatMeter(160, 18, 58, te));
		}
		
		@Override
		public void drawGuiContainerForegroundLayer(int mX, int mY) {
			super.drawGuiContainerForegroundLayer(mX, mY);
			mc.renderEngine.bindTexture(resLoc);
			float b = tile.getBurnTime(), bM = tile.getBurnTimeMax(), per = b / bM;
			int sz = (int)(per * 13);
			drawTexturedModalRect(81, 38 - sz, 176, 13 - sz, 13, sz);
		}
		
	}

}
