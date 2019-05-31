package io.github.phantamanta44.wtflux.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.phantamanta44.wtflux.gui.component.GuiComponent;
import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.lib.LibLang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Collection;

public abstract class GuiContainerMod extends GuiContainer {

    protected final Collection<GuiComponent> comps = Sets.newHashSet();
    protected ResourceLocation resLoc;
    protected String invName;

    public GuiContainerMod(Container cont) {
        super(cont);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mX, int mY) {
        fontRenderer.drawString(LibLang.get(LibLang.PLAYER_INV), 8, this.ySize - 96 + 2, 4210752);
        String resolvedName = LibLang.get(invName);
        int nameXPos = xSize / 2 - fontRenderer.getStringWidth(resolvedName) / 2;
        fontRenderer.drawString(resolvedName, nameXPos, 6, LibCore.GUI_FONT_COLOR);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (GuiComponent comp : comps)
            comp.render(mc, this);

        for (GuiComponent comp : comps)
            comp.mouseOver(mc, this, mX - guiLeft, mY - guiTop);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        comps.forEach(c -> c.onClick(Minecraft.getMinecraft(), this, x - guiLeft, y - guiTop, button));
        super.mouseClicked(x, y, button);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float some, int random, int args) {
        mc.renderEngine.bindTexture(resLoc);
        int x = width / 2 - xSize / 2, y = height / 2 - ySize / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    public void drawHoveringText(String string, int x, int y) {
        //func_146283_a(Lists.newArrayList(string), x, y);
        RenderHelper.enableGUIStandardItemLighting();
    }

    @Override
    protected void keyTyped(char typed, int keyCode) throws IOException {
        if (comps.stream().allMatch(c -> c.onKeyPress(Minecraft.getMinecraft(), this, keyCode, typed)))
            super.keyTyped(typed, keyCode);
    }

}
