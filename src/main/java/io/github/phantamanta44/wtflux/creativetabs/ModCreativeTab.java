package io.github.phantamanta44.wtflux.creativetabs;

import io.github.phantamanta44.wtflux.lib.LibCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModCreativeTab
{
    public static final CustomCreativeTab MOD_TAB = new CustomCreativeTab(LibCore.MODID + "food", false) {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(Items.FEATHER);
        }

    };

    /**
     * This class is used for an extra tab in the creative inventory. Many mods like to group their special items and blocks in a dedicated tab although it is also perfectly
     * acceptable to put them in the vanilla tabs where it makes sense.
     */
    public abstract static class CustomCreativeTab extends CreativeTabs
    {

        private final boolean hasSearchBar;

        public CustomCreativeTab(final String name, final boolean hasSearchBar)
        {
            super(name);
            this.hasSearchBar = hasSearchBar;
        }

        /**
         * gets the {@link net.minecraft.item.ItemStack ItemStack} to display for the tab's icon
         */
        @SideOnly(Side.CLIENT)
        @Override
        abstract public ItemStack getTabIconItem();

        /**
         * Useful for adding extra items such as full variants of energy related items
         */
        @SideOnly(Side.CLIENT)
        @Override
        public void displayAllRelevantItems(final NonNullList<ItemStack> items)
        {
            super.displayAllRelevantItems(items);
        }

        @Override
        public boolean hasSearchBar()
        {
            return this.hasSearchBar;
        }

        @Override
        public String getBackgroundImageName()
        {
            if (this.hasSearchBar)
            {
                return "item_search.png";
            }
            else
            {
                return super.getBackgroundImageName();
            }
        }

    }
}
