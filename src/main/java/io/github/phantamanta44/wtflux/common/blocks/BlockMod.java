package io.github.phantamanta44.wtflux.common.blocks;

import io.github.phantamanta44.wtflux.common.creativetab.ModCreativeTabs;
import io.github.phantamanta44.wtflux.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMod extends Block
{
    public BlockMod(final String name, final Material material) {
        super(material);
        ModUtil.setRegistryNames(this, name);
        setCreativeTab(ModCreativeTabs.MOD_TAB);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (this instanceof ITileEntityProvider) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity != null && tileEntity instanceof IInventory) {
                IInventory inventory = (IInventory)tileEntity;
                ItemStack itemStack;
                for (int i = 0; i < inventory.getSizeInventory(); i++) {
                    if ((itemStack = inventory.getStackInSlot(i)) != null)
                        ModUtil.dropItem(worldIn, pos, itemStack);
                }
            }
        }
        super.breakBlock(worldIn, pos, state);
    }
}
