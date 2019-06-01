package io.github.phantamanta44.wtflux;

import io.github.phantamanta44.wtflux.block.BlockCompressed;
import io.github.phantamanta44.wtflux.lib.LibCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = LibCore.MODID)
public final class EventSubscriber
{
    /**
     * Register Blocks
     **/
    @SubscribeEvent
    public static void onRegisterBlocksEvent(final RegistryEvent.Register<Block> event) {

        final IForgeRegistry<Block> registry = event.getRegistry();

        registry.register(new BlockCompressed("block_compressed", Material.IRON));
    }
}
