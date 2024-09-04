package de.takacick.onenukeblock.registry;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.statuseffects.Bleeding;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class EffectRegistry {

    public static final RegistryEntry<StatusEffect> BLEEDING = register("bleeding", new Bleeding());

    public static void register() {

    }

    private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(OneNukeBlock.MOD_ID, id), statusEffect);
    }


}