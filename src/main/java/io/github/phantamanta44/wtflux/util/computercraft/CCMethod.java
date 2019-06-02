package io.github.phantamanta44.wtflux.util.computercraft;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;

public abstract class CCMethod {

    private final String name;

    public CCMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Object[] execute(IComputerAccess com, ILuaContext ctx, Object[] args) throws LuaException, InterruptedException;

}
