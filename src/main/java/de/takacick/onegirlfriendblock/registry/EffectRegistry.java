package de.takacick.onegirlfriendblock.registry;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.statuseffects.Bleeding;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EffectRegistry {

    public static final StatusEffect BLEEDING = new Bleeding();

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(OneGirlfriendBlock.MOD_ID, "bleeding"), BLEEDING);
    }
}