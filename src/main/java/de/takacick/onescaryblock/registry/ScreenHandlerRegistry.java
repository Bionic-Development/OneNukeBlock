package de.takacick.onescaryblock.registry;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.registry.inventory.ScaryOneBlockScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlerRegistry {
    public static final ScreenHandlerType<ScaryOneBlockScreenHandler> SCARY_ONE_BLOCk = net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.registerSimple(new Identifier(OneScaryBlock.MOD_ID, "scaryoneblock"), ScaryOneBlockScreenHandler::new);

    public static void register() {

    }
}
