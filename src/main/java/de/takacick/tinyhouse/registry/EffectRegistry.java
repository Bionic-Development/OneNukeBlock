package de.takacick.tinyhouse.registry;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.registry.statuseffects.Bleeding;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EffectRegistry {

    public static final StatusEffect BLEEDING = new Bleeding();

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(TinyHouse.MOD_ID, "bleeding"), BLEEDING);
    }
}