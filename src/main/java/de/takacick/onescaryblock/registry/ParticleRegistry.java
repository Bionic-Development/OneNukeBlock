package de.takacick.onescaryblock.registry;

import de.takacick.onescaryblock.OneScaryBlock;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final DefaultParticleType FALLING_BLOOD = FabricParticleTypes.simple();
    public static final DefaultParticleType BLOOD_SPLASH = FabricParticleTypes.simple();
    public static final DefaultParticleType ITEM_303 = FabricParticleTypes.simple();
    public static final DefaultParticleType SOUL = FabricParticleTypes.simple();
    public static final DefaultParticleType BARRIER = FabricParticleTypes.simple();
    public static final DefaultParticleType HEROBRINE_LIGHTNING = FabricParticleTypes.simple();
    public static final DefaultParticleType SMOKE = FabricParticleTypes.simple();

    public static final SoundEvent BLOOD_DROP = SoundEvent.of(new Identifier(OneScaryBlock.MOD_ID, "blood_drop"));

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneScaryBlock.MOD_ID, "falling_blood"), FALLING_BLOOD);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneScaryBlock.MOD_ID, "blood_splash"), BLOOD_SPLASH);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneScaryBlock.MOD_ID, "item_303"), ITEM_303);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneScaryBlock.MOD_ID, "soul"), SOUL);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneScaryBlock.MOD_ID, "barrier"), BARRIER);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneScaryBlock.MOD_ID, "herobrine_lightning"), HEROBRINE_LIGHTNING);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneScaryBlock.MOD_ID, "smoke"), SMOKE);

        Registry.register(Registries.SOUND_EVENT, BLOOD_DROP.getId(), BLOOD_DROP);
    }
}
