package io.github.phantamanta44.wtflux.gui;

import io.github.phantamanta44.wtflux.gui.component.GCTextInput;
import io.github.phantamanta44.wtflux.inventory.ContainerDummy;
import io.github.phantamanta44.wtflux.lib.LibResource;
import io.github.phantamanta44.wtflux.tile.TileSensor;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiSensor extends GuiContainerMod {

    public GuiSensor(InventoryPlayer ipl, TileSensor tile) {
        super(new ContainerDummy(ipl, tile));
        resLoc = LibResource.TEX_GUI_SENSOR;
        invName = tile.getGuiName();
        GCTextInput textInput = new GCTextInput(43, 32, 8, tile.getConfigValidator(), tile.getConfigCallback());
        comps.add(textInput);
        textInput.setValue(tile.getConfigValue());
    }

}
