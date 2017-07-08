package io.github.phantamanta44.wtflux.gui.component;

import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.lib.LibResource;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import io.github.phantamanta44.wtflux.util.WtfUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GCReactorInfo extends GuiComponent {

    private final TileGenerator.Nuke infoSrc;

    public GCReactorInfo(TileGenerator.Nuke te) {
        super(117, 11, 12, 62);
        infoSrc = te;
    }

    @Override
    public void render(Minecraft mc, GuiScreen gui) {
        mc.renderEngine.bindTexture(LibResource.TEX_GC_BASE);

        gui.drawTexturedModalRect(x, y, 0, 0, 1, 1);
        gui.drawTexturedModalRect(x, y + 61, 0, 2, 1, 1);
        gui.drawTexturedModalRect(x + 3, y, 2, 0, 1, 1);
        gui.drawTexturedModalRect(x + 3, y + 61, 2, 2, 1, 1);

        gui.drawTexturedModalRect(x + 8, y, 0, 0, 1, 1);
        gui.drawTexturedModalRect(x + 8, y + 61, 0, 2, 1, 1);
        gui.drawTexturedModalRect(x + 11, y, 2, 0, 1, 1);
        gui.drawTexturedModalRect(x + 11, y + 61, 2, 2, 1, 1);

        for (int i = 1; i < 61; i++) {
            gui.drawTexturedModalRect(x, y + i, 0, 1, 1, 1);
            gui.drawTexturedModalRect(x + 3, y + i, 2, 1, 1, 1);
            gui.drawTexturedModalRect(x + 8, y + i, 0, 1, 1, 1);
            gui.drawTexturedModalRect(x + 11, y + i, 2, 1, 1, 1);
        }

        for (int i = 1; i < 3; i++) {
            gui.drawTexturedModalRect(x + i, y, 1, 0, 1, 1);
            gui.drawTexturedModalRect(x + i, y + 61, 1, 2, 1, 1);
            gui.drawTexturedModalRect(x + 8 + i, y, 1, 0, 1, 1);
            gui.drawTexturedModalRect(x + 8 + i, y + 61, 1, 2, 1, 1);
            for (int j = 1; j < 61; j++) {
                gui.drawTexturedModalRect(x + i, y + j, 1, 1, 1, 1);
                gui.drawTexturedModalRect(x + i + 8, y + j, 1, 1, 1, 1);
            }
        }

        float f = infoSrc.getFuel(), fPer = f / 4000F, w = infoSrc.getWaste(), wPer = w / 4000F;
        int fSz = (int)(fPer * 60), wSz = (int)(wPer * 60);
        for (int i = 2; i < fSz + 2; i++)
            gui.drawTexturedModalRect(x + 1, y + 62 - i, 5, 0, 2, 1);
        for (int i = 2; i < wSz + 2; i++)
            gui.drawTexturedModalRect(x + 9, y + 62 - i,12, 0, 2, 1);
    }

    @Override
    public void mouseOver(Minecraft mc, GuiScreen gui, int mX, int mY) {
        if (WtfUtil.isMouseOver(x, y, 4, 62, mX, mY))
            drawHoveringText(gui, String.format("%s: %.2f / 4000 mL", LibLang.get(LibLang.TT_FUEL), infoSrc.getFuel()), mX, mY);

        if (WtfUtil.isMouseOver(x + 8, y, 4, 62, mX, mY))
            drawHoveringText(gui, String.format("%s: %.2f / 4000 mL", LibLang.get(LibLang.TT_WASTE), infoSrc.getWaste()), mX, mY);
    }

}
