package io.github.phantamanta44.wtflux;

import io.github.phantamanta44.wtflux.util.ModReference;
import io.github.phantamanta44.wtflux.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public final class EventSubscriber
{
    /**
     * Register Blocks
     **/
    @SubscribeEvent
    public static void onRegisterBlocksEvent(final RegistryEvent.Register<Block> event) {

        final IForgeRegistry<Block> registry = event.getRegistry();

    }

    /**
     * Register Items
     **/
    @SubscribeEvent
    public static void onRegisterItemsEvent(final RegistryEvent.Register<Item> event) {

        final IForgeRegistry<Item> registry = event.getRegistry();

        ForgeRegistries.BLOCKS.getValuesCollection().stream().filter((block) -> block.getRegistryName().getResourceDomain().equals(ModReference.MOD_ID)).forEach((ourBlock) -> {
            registry.register(ModUtil.setRegistryNames(new ItemBlock(ourBlock), ourBlock.getRegistryName()));
        });

    }
}
