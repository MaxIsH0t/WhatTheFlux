package io.github.phantamanta44.wtflux.item.block;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.phantamanta44.wtflux.lib.LibLang;
import io.github.phantamanta44.wtflux.lib.LibNBT;
import io.github.phantamanta44.wtflux.tile.TileGenerator;
import io.github.phantamanta44.wtflux.util.KeyBindUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemBlockGenerator extends ItemBlockWithMetadataAndName implements IEnergyContainerItem {

    public ItemBlockGenerator(Block block) {
        super(block);
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean flag) {
        if (stack.stackTagCompound != null) {
            info.add(String.format("%s: %s%s / %s RF", LibLang.get(LibLang.INF_CHARGE), EnumChatFormatting.AQUA, stack.stackTagCompound.getInteger(LibNBT.ENERGY), stack.stackTagCompound.getInteger(LibNBT.ENERGY_MAX)));
            KeyBinding bind = Minecraft.getMinecraft().gameSettings.keyBindSneak;
            if (!Keyboard.isKeyDown(bind.getKeyCode()))
                info.add(String.format("%s" + LibLang.get(LibLang.INF_EXPAND), EnumChatFormatting.DARK_GRAY, EnumChatFormatting.BLUE + KeyBindUtil.getName(bind) + EnumChatFormatting.DARK_GRAY));
            else {
                info.add(String.format("%s: %s%s", LibLang.get(LibLang.INF_GEN), EnumChatFormatting.GREEN, LibLang.getGenType(stack.stackTagCompound.getByte(LibNBT.GENTYPE))));
                info.add(String.format("%s: %s%s", LibLang.get(LibLang.INF_DYN), EnumChatFormatting.GREEN, LibLang.getDynType(stack.stackTagCompound.getByte(LibNBT.DYNTYPE))));
                info.add(String.format("%s: %s%s", LibLang.get(LibLang.INF_CAP), EnumChatFormatting.GREEN, LibLang.getCapType(stack.stackTagCompound.getByte(LibNBT.CAPTYPE))));
                info.add(String.format("%s: %s%s", LibLang.get(LibLang.INF_CASING), EnumChatFormatting.GREEN, LibLang.getCasingType(stack.getTagCompound().getByte(LibNBT.CASINGTYPE))));
                info.add(String.format("%s- %s: %s%.0f°C", EnumChatFormatting.DARK_GRAY, LibLang.get(LibLang.INF_MP), EnumChatFormatting.BLUE, TileGenerator.MELTING_POINTS[stack.stackTagCompound.getByte(LibNBT.CASINGTYPE)]));
                info.add(String.format("%s- %s: %s%.0f RPM", EnumChatFormatting.DARK_GRAY, LibLang.get(LibLang.INF_RPM_CAP), EnumChatFormatting.BLUE, TileGenerator.RPM_CAPS[stack.stackTagCompound.getByte(LibNBT.CASINGTYPE)]));
            }
        }
        else
            info.add(LibLang.get(LibLang.INF_NO_TAG));
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float subX, float subY, float subZ, int meta) {
        if (super.placeBlockAt(stack, player, world, x, y, z, side, subX, subY, subZ, meta)) {
            TileGenerator tile = (TileGenerator)world.getTileEntity(x, y, z);
            tile.readItemTag(stack.stackTagCompound);
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
        if (stack.stackTagCompound != null) {
            int stored = stack.stackTagCompound.getInteger(LibNBT.ENERGY);
            int transferRate = stack.stackTagCompound.getInteger(LibNBT.CAPTYPE) * 128 + 128;
            int toTransfer = Math.max(stored - transferRate, 0);
            if (toTransfer > 0 && !simulate)
                stack.stackTagCompound.setInteger(LibNBT.ENERGY, stored - toTransfer);
            return toTransfer;
        }
        return 0;
    }

    @Override
    public int getEnergyStored(ItemStack stack) {
        if (stack.stackTagCompound != null)
            stack.stackTagCompound.getInteger(LibNBT.ENERGY);
        return 0;
    }

    @Override
    public int getMaxEnergyStored(ItemStack stack) {
        if (stack.stackTagCompound != null)
            stack.stackTagCompound.getInteger(LibNBT.ENERGY_MAX);
        return 0;
    }

}
