package io.github.phantamanta44.wtflux.util.computercraft;

import dan200.computercraft.api.lua.LuaException;

public class CCUtils {

    public static void argsZeroLength(Object[] args) throws LuaException {
        if (args.length != 0)
            throw new LuaException("Wrong number of arguments. 1 expected.");
    }

}
