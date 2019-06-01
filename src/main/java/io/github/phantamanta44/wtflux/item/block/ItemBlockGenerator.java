package io.github.phantamanta44.wtflux.item.block;

import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.lib.LibNBT;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import io.github.phantamanta44.wtflux.util.IEnergyContainerItem;
import io.github.phantamanta44.wtflux.util.KeyBindUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockGenerator extends ItemBlockWithMetadataAndName implements IEnergyContainerItem {

    public ItemBlockGenerator(Block block) {
        super(block);
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getTagCompound() != null) {
            tooltip.add(String.format("%s: %s%s / %s RF", LibLang.get(LibLang.INF_CHARGE), TextFormatting.AQUA, stack.getTagCompound().getInteger(LibNBT.ENERGY), stack.getTagCompound().getInteger(LibNBT.ENERGY_MAX)));
            KeyBinding bind = Minecraft.getMinecraft().gameSettings.keyBindSneak;
            if (!Keyboard.isKeyDown(bind.getKeyCode()))
                tooltip.add(String.format("%s" + LibLang.get(LibLang.INF_EXPAND), TextFormatting.DARK_GRAY, TextFormatting.BLUE + KeyBindUtil.getName(bind) + TextFormatting.DARK_GRAY));
            else {
                tooltip.add(String.format("%s: %s%s", LibLang.get(LibLang.INF_GEN), TextFormatting.GREEN, LibLang.getGenType(stack.getTagCompound().getByte(LibNBT.GENTYPE))));
                tooltip.add(String.format("%s: %s%s", LibLang.get(LibLang.INF_DYN), TextFormatting.GREEN, LibLang.getDynType(stack.getTagCompound().getByte(LibNBT.DYNTYPE))));
                tooltip.add(String.format("%s: %s%s", LibLang.get(LibLang.INF_CAP), TextFormatting.GREEN, LibLang.getCapType(stack.getTagCompound().getByte(LibNBT.CAPTYPE))));
                tooltip.add(String.format("%s: %s%s", LibLang.get(LibLang.INF_CASING), TextFormatting.GREEN, LibLang.getCasingType(stack.getTagCompound().getByte(LibNBT.CASINGTYPE))));
                tooltip.add(String.format("%s- %s: %s%.0f°C", TextFormatting.DARK_GRAY, LibLang.get(LibLang.INF_MP), TextFormatting.BLUE, TileGenerator.MELTING_POINTS[stack.getTagCompound().getByte(LibNBT.CASINGTYPE)]));
                tooltip.add(String.format("%s- %s: %s%.0f RPM", TextFormatting.DARK_GRAY, LibLang.get(LibLang.INF_RPM_CAP), TextFormatting.BLUE, TileGenerator.RPM_CAPS[stack.getTagCompound().getByte(LibNBT.CASINGTYPE)]));
            }
        }
        else
            tooltip.add(LibLang.get(LibLang.INF_NO_TAG));
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
            TileGenerator tile = (TileGenerator)world.getTileEntity(pos);
            tile.readItemTag(stack.getTagCompound());
            return true;
        }
        return false;
    }

    @Override
    public int receiveEnergy(ItemStack stack, int amt, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(ItemStack stack, int amt, boolean simulate) {
        if (stack.getTagCompound() != null) {
            int stored = stack.getTagCompound().getInteger(LibNBT.ENERGY);
            int transferRate = stack.getTagCompound().getInteger(LibNBT.CAPTYPE) * 128 + 128;
            int toTransfer = Math.max(stored - transferRate, 0);
            if (toTransfer > 0 && !simulate)
                stack.getTagCompound().setInteger(LibNBT.ENERGY, stored - toTransfer);
            return toTransfer;
        }
        return 0;
    }

    @Override
    public int getEnergyStored(ItemStack stack) {
        if (stack.getTagCompound() != null)
            stack.getTagCompound().getInteger(LibNBT.ENERGY);
        return 0;
    }

    @Override
    public int getMaxEnergyStored(ItemStack stack) {
        if (stack.getTagCompound() != null)
            stack.getTagCompound().getInteger(LibNBT.ENERGY_MAX);
        return 0;
    }

}
