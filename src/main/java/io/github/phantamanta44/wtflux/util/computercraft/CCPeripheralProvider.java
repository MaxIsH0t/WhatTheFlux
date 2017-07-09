package io.github.phantamanta44.wtflux.util.computercraft;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import io.github.phantamanta44.wtflux.block.WtfBlocks;
import net.minecraft.world.World;

public class CCPeripheralProvider implements IPeripheralProvider {

    @Override
    public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
        return world.getBlock(x, y, z) == WtfBlocks.blockGen ? (IPeripheral)world.getTileEntity(x, y, z) : null;
    }

}
