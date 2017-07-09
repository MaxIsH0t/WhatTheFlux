package io.github.phantamanta44.wtflux.util.computercraft;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;

import java.util.function.BooleanSupplier;

public class CCMethodBoolGetter extends CCMethod {

    private final BooleanSupplier source;

    public CCMethodBoolGetter(String name, BooleanSupplier source) {
        super(name);
        this.source = source;
    }

    @Override
    public Object[] execute(IComputerAccess com, ILuaContext ctx, Object[] args) throws LuaException, InterruptedException {
        CCUtils.argsZeroLength(args);
        return new Object[] {source.getAsBoolean()};
    }

}
