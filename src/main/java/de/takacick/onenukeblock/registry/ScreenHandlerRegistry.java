package de.takacick.onenukeblock.registry;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.inventory.NukeOneBlockScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlerRegistry {

    public static final ScreenHandlerType<NukeOneBlockScreenHandler> NUKE_ONE_BLOCK = register(Identifier.of(OneNukeBlock.MOD_ID, "nukeoneblock"), NukeOneBlockScreenHandler::new);

    public static void register() {

    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(Identifier id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, id, new ScreenHandlerType<T>(factory, FeatureFlags.VANILLA_FEATURES));
    }

}
