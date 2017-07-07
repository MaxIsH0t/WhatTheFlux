package io.github.phantamanta44.wtflux.gui.component;

import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.lib.LibResource;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import io.github.phantamanta44.wtflux.util.WtfUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GCSpeedometer extends GuiComponent {

    private TileGenerator msource;

    public GCSpeedometer(int x, int y, TileGenerator ms) {
        super(x, y, 7, 7);
        msource = ms;
    }

    @Override
    public void render(Minecraft mc, GuiScreen gui) {
        mc.renderEngine.bindTexture(LibResource.TEX_GC_BASE);
        gui.drawTexturedModalRect(x, y, 0, 3, 7, 7);

        float m = msource.getMomentum();
        int level = (int)Math.min(m - (m % 12), 48F) / 12;
        gui.drawTexturedModalRect(x, y, 7 + level * 7, 3, 7, 7);
    }

    @Override
    public void mouseOver(Minecraft mc, GuiScreen gui, int mX, int mY) {
        if (WtfUtil.isMouseOver(x, y, width, height, mX, mY))
            drawHoveringText(gui, String.format("%s: %.2f RPM", LibLang.get(LibLang.TT_MOM), msource.getMomentum()), mX, mY);
    }

}
