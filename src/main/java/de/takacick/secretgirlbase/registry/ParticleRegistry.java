package de.takacick.secretgirlbase.registry;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.registry.particles.ColoredParticleEffect;
import de.takacick.secretgirlbase.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.secretgirlbase.registry.particles.goop.GoopParticleEffect;
import de.takacick.secretgirlbase.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretGirlBase.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretGirlBase.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretGirlBase.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final ParticleType<ColoredParticleEffect> COLORED_FIREWORK = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_BUBBLE_DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> GROW = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> SHRINK = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType MAGIC_FLOWER = FabricParticleTypes.simple();
    public static final DefaultParticleType MAGIC_SPARK = FabricParticleTypes.simple();

    public static final SoundEvent MAGIC_VANISH = SoundEvent.of(new Identifier(SecretGirlBase.MOD_ID, "magic_vanish"));
    public static final SoundEvent SHRINK_SOUND = SoundEvent.of(new Identifier(SecretGirlBase.MOD_ID, "shrink"));
    public static final SoundEvent GROW_SOUND = SoundEvent.of(new Identifier(SecretGirlBase.MOD_ID, "grow"));

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretGirlBase.MOD_ID, "colored_firework"), COLORED_FIREWORK);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretGirlBase.MOD_ID, "colored_dust"), COLORED_DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretGirlBase.MOD_ID, "magic_flower"), MAGIC_FLOWER);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretGirlBase.MOD_ID, "magic_spark"), MAGIC_SPARK);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretGirlBase.MOD_ID, "colored_bubble_dust"), COLORED_BUBBLE_DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretGirlBase.MOD_ID, "grow"), GROW);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SecretGirlBase.MOD_ID, "shrink"), SHRINK);

        Registry.register(Registries.SOUND_EVENT, MAGIC_VANISH.getId(), MAGIC_VANISH);
        Registry.register(Registries.SOUND_EVENT, SHRINK_SOUND.getId(), SHRINK_SOUND);
        Registry.register(Registries.SOUND_EVENT, GROW_SOUND.getId(), GROW_SOUND);
    }
}
