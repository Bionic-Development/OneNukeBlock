package de.takacick.illegalwars.registry;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.statuseffects.Bleeding;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EffectRegistry {

    public static final StatusEffect BLEEDING = new Bleeding();

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(IllegalWars.MOD_ID, "bleeding"), BLEEDING);
    }
}