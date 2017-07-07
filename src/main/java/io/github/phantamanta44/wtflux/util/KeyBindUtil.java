package io.github.phantamanta44.wtflux.util;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

public class KeyBindUtil {

    public static String getName(KeyBinding bind) {
        return Keyboard.getKeyName(bind.getKeyCode());
    }

}
