package io.github.phantamanta44.wtflux.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.phantamanta44.wtflux.gui.component.*;
import io.github.phantamanta44.wtflux.inventory.ContainerGenerator;
import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.lib.LibResource;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import io.github.phantamanta44.wtflux.util.WtfUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

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

        public Furnace(InventoryPlayer ipl, TileGenerator.Furnace te) {
            super(ipl, te);
            resLoc = LibResource.TEX_GUI_FURNACE;
            invName = LibLang.GUI_GEN_FURNACE;
            comps.add(new GCGenHeatMeter(160, 18, 58, te));
        }

        @Override
        public void drawGuiContainerForegroundLayer(int mX, int mY) {
            mc.renderEngine.bindTexture(resLoc);
            float b = tile.getBurnTime(), bM = tile.getBurnTimeMax(), per = b / bM;
            int sz = (int)(per * 13);
            drawTexturedModalRect(81, 38 - sz, 176, 13 - sz, 13, sz);
            if (WtfUtil.isMouseOver(81, 25, 13, 13, mX - guiLeft, mY - guiTop))
                drawHoveringText(tile.getBurnTime() + " Ticks", mX - guiLeft, mY - guiTop);
            super.drawGuiContainerForegroundLayer(mX, mY);
        }

    }

    public static class Heat extends GuiGenerator<TileGenerator.Heat> {

        public Heat(InventoryPlayer ipl, TileGenerator.Heat te) {
            super(ipl, te);
            resLoc = LibResource.TEX_GUI_HEAT;
            invName = LibLang.GUI_GEN_HEAT;
            comps.add(new GCGenHeatMeter(160, 18, 58, te));
            comps.add(new GCFluidTank(79, 19, 48, tile::getTank));
        }

    }

    public static class Wind extends GuiGenerator<TileGenerator.Wind> {

        public Wind(InventoryPlayer ipl, TileGenerator.Wind te) {
            super(ipl, te);
            resLoc = LibResource.TEX_GUI_WIND;
            invName = LibLang.GUI_GEN_WIND;
        }

        @Override
        public void drawGuiContainerForegroundLayer(int mX, int mY) {
            super.drawGuiContainerForegroundLayer(mX, mY);
        }

    }

    public static class Water extends GuiGenerator<TileGenerator.Water> {

        public Water(InventoryPlayer ipl, TileGenerator.Water te) {
            super(ipl, te);
            resLoc = LibResource.TEX_GUI_WATER;
            invName = LibLang.GUI_GEN_WATER;
            comps.add(new GCFluidTank(56, 19, 48, tile::getTank));
            comps.add(new GCFluidTank(101, 19, 48, tile::getLowerTank));
        }

        @Override
        public void drawGuiContainerForegroundLayer(int mX, int mY) {
            super.drawGuiContainerForegroundLayer(mX, mY);
        }

    }

    public static class Nuke extends GuiGenerator<TileGenerator.Nuke> {

        private static final int[] lh = new int[] {41, 32, 23, 14};

        public Nuke(InventoryPlayer ipl, TileGenerator.Nuke te) {
            super(ipl, te);
            resLoc = LibResource.TEX_GUI_NUKE;
            invName = LibLang.GUI_GEN_NUKE;
            comps.add(new GCGenHeatMeter(160, 18, 58, te));
            comps.add(new GCFluidTank(7, 11, 62, tile::getTank));
            comps.add(new GCReactorInfo(te));
        }

        @Override
        public void drawGuiContainerForegroundLayer(int mX, int mY) {
            String[] status = tile.getStatus();
            for (int i = 0; i < 4; i++)
                fontRendererObj.drawString(LibLang.get(status[i]), 56, lh[i], LibCore.COMP_FONT_COLOR);
            GL11.glColor4f(1F, 1F, 1F, 1F);

            for (GuiComponent comp : comps)
                comp.render(mc, this);

            for (GuiComponent comp : comps)
                comp.mouseOver(mc, this, mX - guiLeft, mY - guiTop);
        }

    }

    public static class Solar extends GuiGenerator<TileGenerator.Solar> {

        public Solar(InventoryPlayer ipl, TileGenerator.Solar te) {
            super(ipl, te);
            resLoc = LibResource.TEX_GUI_SOLAR;
            invName = LibLang.GUI_GEN_SOLAR;
            comps.add(new GCGenHeatMeter(160, 18, 58, te));
        }

        @Override
        public void drawGuiContainerForegroundLayer(int mX, int mY) {
            super.drawGuiContainerForegroundLayer(mX, mY);
        }

    }

}
