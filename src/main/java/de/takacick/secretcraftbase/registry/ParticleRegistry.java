package de.takacick.secretcraftbase.registry;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.particles.ColoredParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final ParticleType<ColoredParticleEffect> COLORED_FLASH = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_FIREWORK = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> LASER_DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType FALLING_BLOOD = FabricParticleTypes.simple();
    public static final DefaultParticleType BLOOD_SPLASH = FabricParticleTypes.simple();
    public static final DefaultParticleType MAGIC = FabricParticleTypes.simple();
    public static final ParticleType<ColoredParticleEffect> FOLLOW_DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType SPIT = FabricParticleTypes.simple();

    public static final SoundEvent MAGIC_VANISH = SoundEvent.of(new Identifier(SecretCraftBase.MOD_ID, "magic_vanish"));

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretCraftBase.MOD_ID, "colored_flash"), COLORED_FLASH);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretCraftBase.MOD_ID, "colored_firework"), COLORED_FIREWORK);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretCraftBase.MOD_ID, "laser_dust"), LASER_DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretCraftBase.MOD_ID, "falling_blood"), FALLING_BLOOD);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretCraftBase.MOD_ID, "blood_splash"), BLOOD_SPLASH);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretCraftBase.MOD_ID, "magic"), MAGIC);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretCraftBase.MOD_ID, "follow_dust"), FOLLOW_DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretCraftBase.MOD_ID, "spit"), SPIT);

        Registry.register(Registries.SOUND_EVENT, MAGIC_VANISH.getId(), MAGIC_VANISH);
    }
}
