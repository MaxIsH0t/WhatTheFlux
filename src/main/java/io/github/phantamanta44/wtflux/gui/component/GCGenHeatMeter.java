package io.github.phantamanta44.wtflux.gui.component;

import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.lib.LibResource;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import io.github.phantamanta44.wtflux.util.WtfUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GCGenHeatMeter extends GuiComponent {

    private TileGenerator tsource;

    public GCGenHeatMeter(int x, int y, int length, TileGenerator tc) {
        super(x, y, 4, length);
        tsource = tc;
    }

    @Override
    public void render(Minecraft mc, GuiScreen gui) {
        mc.renderEngine.bindTexture(LibResource.TEX_GC_BASE);
        gui.drawTexturedModalRect(x, y, 0, 0, 1, 1);
        gui.drawTexturedModalRect(x, y + height - 1, 0, 2, 1, 1);
        gui.drawTexturedModalRect(x + width - 1, y, 2, 0, 1, 1);
        gui.drawTexturedModalRect(x + width - 1, y + height - 1, 2, 2, 1, 1);
        for (int i = 1; i < height - 1; i++) {
            gui.drawTexturedModalRect(x, y + i, 0, 1, 1, 1);
            gui.drawTexturedModalRect(x + width - 1, y + i, 2, 1, 1, 1);
        }
        for (int i = 1; i < width - 1; i++) {
            gui.drawTexturedModalRect(x + i, y, 1, 0, 1, 1);
            gui.drawTexturedModalRect(x + i, y + height - 1, 1, 2, 1, 1);
            for (int j = 1; j < height - 1; j++)
                gui.drawTexturedModalRect(x + i, y + j, 1, 1, 1, 1);
        }

        float t = Math.max(Math.min(tsource.getTemp(), 5000F), 0), per = t / 5000F;
        int sz = (int)(per * (height - 2));
        for (int i = 2; i < sz + 2; i++)
            gui.drawTexturedModalRect(x + 1, y + height - i, 7, 0, 2, 1);

    }

    @Override
    public void mouseOver(Minecraft mc, GuiScreen gui, int mX, int mY) {
        if (WtfUtil.isMouseOver(x, y, width, height, mX, mY))
            drawHoveringText(gui, String.format("%s: %.2f°C", LibLang.get(LibLang.TT_TEMP), tsource.getTemp()), mX, mY);
    }

}
