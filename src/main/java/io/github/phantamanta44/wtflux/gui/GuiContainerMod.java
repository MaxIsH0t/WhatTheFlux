package io.github.phantamanta44.wtflux.gui;

import io.github.phantamanta44.wtflux.gui.component.GuiComponent;
import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.lib.LibLang;

import java.util.Collection;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Sets;

public abstract class GuiContainerMod extends GuiContainer {

	protected static ResourceLocation resLoc;
	protected static String invName;
	protected Collection<GuiComponent> comps = Sets.newHashSet();
	
	public GuiContainerMod(Container cont) {
		super(cont);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mX, int mY) {
		String resolvedName = LibLang.get(invName);
		int nameXPos = xSize / 2 - fontRendererObj.getStringWidth(resolvedName) / 2;
		fontRendererObj.drawString(resolvedName, nameXPos, 6, LibCore.GUI_FONT_COLOR);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		for (GuiComponent comp : comps)
			comp.render(mc, this);
		
		for (GuiComponent comp : comps)
			comp.mouseOver(mc, this, mX - guiLeft, mY - guiTop);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float some, int random, int args) {
		mc.renderEngine.bindTexture(resLoc);
		int x = width / 2 - xSize / 2, y = height / 2 - ySize / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);	
	}

}
