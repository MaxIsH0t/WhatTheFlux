package io.github.phantamanta44.wtflux;

import io.github.phantamanta44.wtflux.block.BlockCompressed;
import io.github.phantamanta44.wtflux.block.BlockGenerator;
import io.github.phantamanta44.wtflux.item.ItemResource;
import io.github.phantamanta44.wtflux.lib.LibCore;
import io.github.phantamanta44.wtflux.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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

        registry.register(new BlockGenerator("block_gen", Material.IRON));
    }

    /**
     * Register Items
     **/
    @SubscribeEvent
    public static void onRegisterItemsEvent(final RegistryEvent.Register<Item> event) {

        final IForgeRegistry<Item> registry = event.getRegistry();

        ForgeRegistries.BLOCKS.getValuesCollection().stream().filter((block) -> block.getRegistryName().getResourceDomain().equals(LibCore.MODID)).forEach((ourBlock) -> {
            registry.register(ModUtil.setRegistryNames(new ItemBlock(ourBlock), ourBlock.getRegistryName()));
        });

        registry.register(new ItemResource("item_res"));
    }
}
