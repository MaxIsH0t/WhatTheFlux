package io.github.phantamanta44.wtflux.item.block;

import cofh.api.tileentity.IReconfigurableFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemBlockDirectional extends ItemBlockWithMetadataAndName {

    public ItemBlockDirectional(Block block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
            int direction = BlockPistonBase.determineOrientation(world, x, y, z, player);
            ((IReconfigurableFacing)world.getTileEntity(x, y, z))
                    .setFacing(ForgeDirection.getOrientation(direction).getOpposite().ordinal());
            return true;
        }
        return false;
    }

}
