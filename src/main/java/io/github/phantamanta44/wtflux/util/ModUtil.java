package io.github.phantamanta44.wtflux.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.phantamanta44.wtflux.lib.LibCore;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.minecraft.util.EnumFacing.*;

public final class ModUtil
{
    public static final EnumFacing[] VALID_DIRECTIONS = {DOWN, UP, NORTH, SOUTH, WEST, EAST};

    private static class ContainerKey
    {
        ItemStack container;
        FluidStack stack;
        private ContainerKey(ItemStack container)
        {
            this.container = container;
        }
        private ContainerKey(ItemStack container, FluidStack stack)
        {
            this(container);
            this.stack = stack;
        }
        @Override
        public int hashCode()
        {
            int code = 1;
            code = 31*code + container.getItem().hashCode();
            code = 31*code + container.getItemDamage();
            if (stack != null)
                code = 31*code + stack.getFluid().hashCode();
            return code;
        }
        @Override
        public boolean equals(Object o)
        {
            if (!(o instanceof ContainerKey)) return false;
            ContainerKey ck = (ContainerKey)o;
            if (container.getItem() != ck.container.getItem()) return false;
            if (container.getItemDamage() != ck.container.getItemDamage()) return false;
            if (stack == null && ck.stack != null) return false;
            if (stack != null && ck.stack == null) return false;
            if (stack == null && ck.stack == null) return true;
            if (stack.getFluid() != ck.stack.getFluid()) return false;
            return true;
        }
    }

    private static Map<ContainerKey, FluidContainerData> containerFluidMap = Maps.newHashMap();
    private static final ItemStack NULL_EMPTYCONTAINER = new ItemStack(Items.BUCKET);
    private static Map<ContainerKey, FluidContainerData> filledContainerMap = Maps.newHashMap();
    private static Set<ContainerKey> emptyContainers = Sets.newHashSet();

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

    public static ItemStack loadItemStackFromNBT(NBTTagCompound p_77949_0_)
    {
        ItemStack itemstack = new ItemStack(Blocks.PUMPKIN);
        itemstack.writeToNBT(p_77949_0_);
        return itemstack.getItem() != null ? itemstack : null;
    }

    /**
     * Register a new fluid containing item.
     *
     * @param data
     *            See {@link FluidContainerData}.
     * @return True if container was successfully registered; false if it already is, or an invalid parameter was passed.
     */
    public static boolean registerFluidContainer(FluidContainerData data)
    {
        if (isFilledContainer(data.filledContainer) || data.filledContainer == null)
        {
            return false;
        }
        if (data.fluid == null || data.fluid.getFluid() == null)
        {
            FMLLog.bigWarning("Invalid registration attempt for a fluid container item %s has occurred. The registration has been denied to prevent crashes. The mod responsible for the registration needs to correct this.", data.filledContainer.getItem().getUnlocalizedName(data.filledContainer));
            return false;
        }
        containerFluidMap.put(new ContainerKey(data.filledContainer), data);

        if (data.emptyContainer != null && data.emptyContainer != NULL_EMPTYCONTAINER)
        {
            filledContainerMap.put(new ContainerKey(data.emptyContainer, data.fluid), data);
            emptyContainers.add(new ContainerKey(data.emptyContainer));
        }

        MinecraftForge.EVENT_BUS.post(new FluidContainerRegisterEvent(data));
        return true;
    }

    public static boolean isFilledContainer(ItemStack container)
    {
        return container != null && getFluidForFilledItem(container) != null;
    }

    /**
     * Wrapper class for the registry entries. Ensures that none of the attempted registrations
     * contain null references unless permitted.
     */
    public static class FluidContainerData
    {
        public final FluidStack fluid;
        public final ItemStack filledContainer;
        public final ItemStack emptyContainer;

        public FluidContainerData(FluidStack stack, ItemStack filledContainer, ItemStack emptyContainer)
        {
            this(stack, filledContainer, emptyContainer, false);
        }

        public FluidContainerData(FluidStack stack, ItemStack filledContainer, ItemStack emptyContainer, boolean nullEmpty)
        {
            this.fluid = stack;
            this.filledContainer = filledContainer;
            this.emptyContainer = emptyContainer == null ? NULL_EMPTYCONTAINER : emptyContainer;

            if (stack == null || filledContainer == null || emptyContainer == null && !nullEmpty)
            {
                throw new RuntimeException("Invalid FluidContainerData - a parameter was null.");
            }
        }

        public FluidContainerData copy()
        {
            return new FluidContainerData(fluid, filledContainer, emptyContainer, true);
        }
    }

    public static class FluidContainerRegisterEvent extends Event
    {
        public final FluidContainerData data;

        public FluidContainerRegisterEvent(FluidContainerData data)
        {
            this.data = data.copy();
        }
    }

    /**
     * Determines the fluid type and amount inside a container.
     *
     * @param container
     *            The fluid container.
     * @return FluidStack representing stored fluid.
     */
    public static FluidStack getFluidForFilledItem(ItemStack container)
    {
        if (container == null)
        {
            return null;
        }

        FluidContainerData data = containerFluidMap.get(new ContainerKey(container));
        return data == null ? null : data.fluid.copy();
    }

    /**
     * Register an item with the item registry with a custom name : this allows for easier server->client resolution
     *
     * @param item The item to register
     * @param name The mod-unique name of the item
     */
    public static void registerItem(net.minecraft.item.Item item, String name)
    {
        registerItem(item, name);
    }

    /**
     * gets the way this piston should face for that entity that placed it.
     */
    public static int determineOrientation(World p_150071_0_, int p_150071_1_, int p_150071_2_, int p_150071_3_, EntityLivingBase p_150071_4_)
    {
        if (MathHelper.abs((float)p_150071_4_.posX - (float)p_150071_1_) < 2.0F && MathHelper.abs((float)p_150071_4_.posZ - (float)p_150071_3_) < 2.0F)
        {
            double d0 = p_150071_4_.posY + 1.82D - (double)p_150071_4_.posY;

            if (d0 - (double)p_150071_2_ > 2.0D)
            {
                return 1;
            }

            if ((double)p_150071_2_ - d0 > 0.0D)
            {
                return 0;
            }
        }

        int l = MathHelper.floor((double)(p_150071_4_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }

    public static EnumFacing getOrientation(int id)
    {
        if (id >= 0 && id < VALID_DIRECTIONS.length)
        {
            return VALID_DIRECTIONS[id];
        }
        return null;
    }

    /**
     * Look up a mod item in the global "named item list"
     * @param modId The modid owning the item
     * @param name The name of the item itself
     * @return The item or null if not found
     */
    public static Item findItem(String modId, String name)
    {
        return ModUtil.setRegistryNames(name);
    }

    private static Item setRegistryNames(String name) {
        return null;
    }

    private static final CraftingManager instance = new CraftingManager();

    /**
     * Returns the static instance of this class
     */
    public static final CraftingManager getInstance()
    {
        /** The static instance of this class */
        return instance;
    }

    private List recipes = new ArrayList();

    /**
     * returns the List<> of all recipes
     */
    public List getRecipeList()
    {
        return this.recipes;
    }

    /**
     * Register a block with the world, with the specified item class and block name
     * @param block The block to register
     * @param itemclass The item type to register with it : null registers a block without associated item.
     * @param name The mod-unique name to register it as, will get prefixed by your modid.
     */
    public static Block registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name)
    {
        return registerBlock(block, itemclass, name);
    }


    /**
     * Look up a mod block in the global "named item list"
     * @param modId The modid owning the block
     * @param name The name of the block itself
     * @return The block or null if not found
     */
    public static Item findBlock(String modId, String name)
    {
        return ModUtil.setRegistryNames(name);
    }
}
