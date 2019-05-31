package io.github.phantamanta44.wtflux.util;

import io.github.phantamanta44.wtflux.lib.LibCore;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public final class ModUtil
{
    /**
     * Sets the {@link IForgeRegistryEntry.Impl#setRegistryName(ResourceLocation) Registry Name} and the
     *
     * @param entry the {@link IForgeRegistryEntry.Impl IForgeRegistryEntry.Impl<?>} to set the names for
     * @param name  the name for the entry that the registry name is derived from
     *
     * @return the entry
     */
    public static <T extends IForgeRegistryEntry.Impl<?>> T setRegistryNames(final T entry, final String name) {

        return setRegistryNames(entry, String.valueOf(new ResourceLocation(LibCore.MODID, name)));
    }
}
