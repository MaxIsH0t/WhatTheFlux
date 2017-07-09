package io.github.phantamanta44.wtflux.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class GuiHandler implements IGuiHandler {

    public static final Map<Class<? extends TileEntity>, Class<? extends Container>> containerMap = new HashMap<>();
    public static final Map<Class<? extends TileEntity>, Class<? extends Gui>> guiMap = new HashMap<>();

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        Class clazz = containerMap.get(tile.getClass());
        try {
            return clazz.getDeclaredConstructors()[0].newInstance(player.inventory, tile);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        Class clazz = guiMap.get(tile.getClass());
        try {
            return clazz.getDeclaredConstructors()[0].newInstance(player.inventory, tile);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }



}