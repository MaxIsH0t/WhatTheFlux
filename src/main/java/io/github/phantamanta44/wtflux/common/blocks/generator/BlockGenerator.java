package io.github.phantamanta44.wtflux.common.blocks.generator;

import com.google.common.collect.Lists;
import io.github.phantamanta44.wtflux.common.blocks.BlockModSubs;
import io.github.phantamanta44.wtflux.util.ModUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class BlockGenerator extends BlockModSubs {

    public BlockGenerator(String name, Material material, int blocks) {
        super(name, material, blocks);
        setHardness(4F);
        setResistance(7.5F);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (ModUtil.canHarvest(world, pos, player)) {
            List<ItemStack> drops = compileDrops(world, pos, state);
            for (ItemStack stack : drops)
                ModUtil.dropItem(world, pos, stack);
        }
        return world.setBlockToAir(pos);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return OreDictionary.EMPTY_LIST;
    }

    public ArrayList<ItemStack> compileDrops(IBlockAccess world, BlockPos blockPos, IBlockState meta) {
        ArrayList<ItemStack> drops = Lists.newArrayList();
        TileGenerator tile = (TileGenerator)world.getTileEntity(blockPos);
        if (tile != null && tile.isInitialized()) {
            drops.add(tile.getNBTItem());
            for (int i = 0; i < tile.getSizeInventory(); i++) {
                if (tile.getStackInSlot(i) != null)
                    drops.add(tile.getStackInSlot(i));
            }
        }
        return drops;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return super.getPickBlock(state, target, world, pos, player);
    }
}
