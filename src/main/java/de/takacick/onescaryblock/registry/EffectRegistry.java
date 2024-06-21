package de.takacick.onescaryblock.registry;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.registry.statuseffects.Bleeding;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EffectRegistry {

    public static final StatusEffect BLEEDING = new Bleeding();

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(OneScaryBlock.MOD_ID, "bleeding"), BLEEDING);
    }
}