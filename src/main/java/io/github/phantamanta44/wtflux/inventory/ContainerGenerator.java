package io.github.phantamanta44.wtflux.inventory;

import io.github.phantamanta44.wtflux.tile.TileGenerator;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.Maps;

public abstract class ContainerGenerator extends ContainerMod<TileGenerator> {

	// TODO Finish class
	
	private static final Map<Class<? extends TileEntity>, Class<? extends ContainerGenerator>> contMap = Maps.newHashMap();
	
	static {
		contMap.put(TileGenerator.Furnace.class, Furnace.class);
	}
	
	public ContainerGenerator(InventoryPlayer inv, TileGenerator te) {
		tile = te;
		addPlayerInventory(inv);
	}
	
	public static ContainerGenerator newInstance(InventoryPlayer player, TileEntity te) {
		try {
			return (ContainerGenerator)contMap.get(te.getClass()).getConstructors()[0].newInstance(player, te);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static class Furnace extends ContainerGenerator {

		public Furnace(InventoryPlayer inv, TileGenerator.Furnace te) {
			super(inv, te);
			addSlotToContainer(new Slot(te, 0, 80, 43));
		}

		@Override
		public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
			return null;
		}
	}
	
}
