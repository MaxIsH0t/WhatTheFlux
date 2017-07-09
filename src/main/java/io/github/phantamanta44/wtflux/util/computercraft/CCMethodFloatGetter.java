package io.github.phantamanta44.wtflux.util.computercraft;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import io.github.phantamanta44.wtflux.util.FloatSupplier;

public class CCMethodFloatGetter extends CCMethod {

    private final FloatSupplier source;

    public CCMethodFloatGetter(String name, FloatSupplier source) {
        super(name);
        this.source = source;
    }

    @Override
    public Object[] execute(IComputerAccess com, ILuaContext ctx, Object[] args) throws LuaException, InterruptedException {
        CCUtils.argsZeroLength(args);
        return new Object[] {source.getAsFloat()};
    }

}
