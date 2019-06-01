package io.github.phantamanta44.wtflux.gui.component;

import io.github.phantamanta44.wtflux.lib.LibResource;
import io.github.phantamanta44.wtflux.util.WtfUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class GCTextInput extends GuiComponent {

    private final int length;
    private final Predicate<String> validator;
    private final Consumer<String> callback;
    private boolean focused, mouseOver, valid;
    private String value;

    public GCTextInput(int x, int y, int length, Predicate<String> validator, Consumer<String> callback) {
        super(x, y, length * 9 + 9 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT,
                Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 4);
        this.length = length;
        this.validator = validator;
        this.callback = callback;
        this.focused = this.mouseOver = false;
        this.value = "";
        updateValidity();
    }

    public void setValue(String value) {
        this.value = value.substring(0, Math.min(value.length(), length));
        updateValidity();
    }

    private void updateValidity() {
        valid = validator.test(value);
    }

    @Override
    public void render(Minecraft mc, GuiScreen gui) {
        mc.renderEngine.bindTexture(LibResource.TEX_GC_BASE);
        gui.drawTexturedModalRect(x, y, 0, 23, 1, 1);
        gui.drawTexturedModalRect(x + length * 9 + 3, y, 2, 23, 1, 1);
        gui.drawTexturedModalRect(x + length * 9 + 3, y + mc.fontRenderer.FONT_HEIGHT + 3, 2, 25, 1, 1);
        gui.drawTexturedModalRect(x, y + mc.fontRenderer.FONT_HEIGHT + 3, 0, 25, 1, 1);
        for (int i = 1; i < length * 9 + 3; i++) {
            gui.drawTexturedModalRect(x + i, y, 1, 23, 1, 1);
            gui.drawTexturedModalRect(x + i, y + mc.fontRenderer.FONT_HEIGHT + 3, 1, 25, 1, 1);
            for (int j = 1; j < mc.fontRenderer.FONT_HEIGHT + 3; j++)
                gui.drawTexturedModalRect(x + i, y + j, 1, 24, 1, 1);
        }
        for (int i = 1; i < mc.fontRenderer.FONT_HEIGHT + 3; i++) {
            gui.drawTexturedModalRect(x, y + i, 0, 24, 1, 1);
            gui.drawTexturedModalRect(x + length * 9 + 3, y + i, 2, 24, 1, 1);
        }
        if (focused && (System.currentTimeMillis() % 1000) < 500) {
            int xPos = x + mc.fontRenderer.getStringWidth(value) + 2;
            for (int i = 0; i < mc.fontRenderer.FONT_HEIGHT; i++)
                gui.drawTexturedModalRect(xPos, y + i + 2, 3, 23, 1, 1);
        }
        if (valid) {
            if (mouseOver)
                gui.drawTexturedModalRect(x + length * 9 + 5, y, 26, 10, 13, 13);
            else
                gui.drawTexturedModalRect(x + length * 9 + 5, y, 0, 10, 13, 13);
        } else {
            gui.drawTexturedModalRect(x + length * 9 + 5, y, 13, 10, 13, 13);
        }
        mc.fontRenderer.drawString(value, x + 2, y + 3, valid ? 0xABABAB : 0xDD1515, false);
    }

    @Override
    public void mouseOver(Minecraft mc, GuiScreen gui, int mX, int mY) {
        mouseOver = WtfUtil.isMouseOver(x + length * 9 + 5, y, mc.fontRenderer.FONT_HEIGHT + 4, mc.fontRenderer.FONT_HEIGHT + 4, mX, mY);
    }

    @Override
    public void onClick(Minecraft mc, GuiScreen gui, int mX, int mY, int button) {
        if (WtfUtil.isMouseOver(x, y, length * 9 + 4, mc.fontRenderer.FONT_HEIGHT + 4, mX, mY)) {
            if (button == 1)
                value = "";
            focused = true;
        } else {
            focused = false;
        }
        if (mouseOver && valid) {
            //mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(LibResource.SND_GUI_BUTTON, 1F));
            callback.accept(value);
        }
    }

    @Override
    public boolean onKeyPress(Minecraft mc, GuiScreen gui, int keyCode, char typed) {
        if (focused) {
            if (value.length() < length && typed >= 32 && typed < 127) {
                value += typed;
                updateValidity();
            } else if (keyCode == Keyboard.KEY_BACK && !value.isEmpty()) {
                value = value.substring(0, value.length() - 1);
                updateValidity();
            } else if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
                focused = false;
                updateValidity();
                if (valid)
                    callback.accept(value);
            } else if (keyCode == Keyboard.KEY_ESCAPE) {
                focused = false;
            }
            return false;
        }
        return true;
    }

}
