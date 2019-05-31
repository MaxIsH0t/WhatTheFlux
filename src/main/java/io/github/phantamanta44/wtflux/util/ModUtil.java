package io.github.phantamanta44.wtflux.util;

import io.github.phantamanta44.wtflux.lib.LibCore;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.StringUtils;

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

    /**
     * Turns a class's name into a registry name<br>
     * It expects the Class's Name to be in CamelCase format<br>
     * It returns the registry name in snake_case format<br>
     * <br>
     * Examples:<br>
     * (TileEntitySuperAdvancedFurnace, "TileEntity") -> super_advanced_furnace<br>
     * (EntityPortableGenerator, "Entity") -> portable_generator<br>
     * (TileEntityPortableGenerator, "Entity") -> tile_portable_generator<br>
     * (EntityPortableEntityGeneratorEntity, "Entity") -> portable_generator<br>
     *
     * @param clazz      the class
     * @param removeType the string to be removed from the class's name
     *
     * @return the recommended registry name for the class
     */
    public static String getRegistryNameForClass(final Class<?> clazz, final String removeType) {

        return StringUtils.uncapitalize(clazz.getSimpleName().replace(removeType, "")).replaceAll("([A-Z])", "_$1").toLowerCase();
    }
}
