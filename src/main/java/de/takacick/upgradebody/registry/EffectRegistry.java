package de.takacick.upgradebody.registry;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.registry.statuseffects.Bleeding;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EffectRegistry {

    public static final StatusEffect BLEEDING = new Bleeding();

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(UpgradeBody.MOD_ID, "bleeding"), BLEEDING);
    }
}