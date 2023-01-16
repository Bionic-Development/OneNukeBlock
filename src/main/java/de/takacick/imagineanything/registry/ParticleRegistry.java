package de.takacick.imagineanything.registry;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.particles.ColoredGlowParticleEffect;
import de.takacick.imagineanything.registry.particles.GlowParticleEffect;
import de.takacick.imagineanything.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.imagineanything.registry.particles.goop.GoopParticleEffect;
import de.takacick.imagineanything.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final ParticleType<GlowParticleEffect> GLOW = FabricParticleTypes.complex(GlowParticleEffect.FACTORY);
    public static final ParticleType<ColoredGlowParticleEffect> GLOW_TOTEM = FabricParticleTypes.complex(ColoredGlowParticleEffect.FACTORY);
    public static final ParticleType<ColoredGlowParticleEffect> GLOW_SPARK = FabricParticleTypes.complex(ColoredGlowParticleEffect.FACTORY);
    public static final DefaultParticleType POOF = FabricParticleTypes.simple();

    public static final DefaultParticleType THANOS_EXPLOSION = FabricParticleTypes.simple();
    public static final DefaultParticleType THANOS_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final DefaultParticleType TELEKINESIS_EXPLOSION = FabricParticleTypes.simple();
    public static final DefaultParticleType TELEKINESIS_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final DefaultParticleType IRON_MAN_EXPLOSION = FabricParticleTypes.simple();
    public static final DefaultParticleType IRON_MAN_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final DefaultParticleType FART = FabricParticleTypes.simple();
    public static final DefaultParticleType GIANT_VIBRATION = FabricParticleTypes.simple();

    public static final Identifier POOF_SOUND = new Identifier(ImagineAnything.MOD_ID + ":poof");
    public static final SoundEvent POOF_EVENT = new SoundEvent(POOF_SOUND);
    public static final Identifier FART_SOUND = new Identifier(ImagineAnything.MOD_ID + ":fart");
    public static final SoundEvent FART_EVENT = new SoundEvent(FART_SOUND);
    public static final Identifier BEDROCK_SPEAKER_SOUND = new Identifier(ImagineAnything.MOD_ID + ":bedrock_speaker");
    public static final SoundEvent BEDROCK_SPEAKER_EVENT = new SoundEvent(BEDROCK_SPEAKER_SOUND);

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "glow"), GLOW);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "glow_totem"), GLOW_TOTEM);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "glow_spark"), GLOW_SPARK);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "poof"), POOF);

        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "thanos_explosion"), THANOS_EXPLOSION);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "thanos_explosion_emitter"), THANOS_EXPLOSION_EMITTER);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "telekinesis_explosion"), TELEKINESIS_EXPLOSION);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "telekinesis_explosion_emitter"), TELEKINESIS_EXPLOSION_EMITTER);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "iron_man_explosion"), IRON_MAN_EXPLOSION);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "iron_man_explosion_emitter"), IRON_MAN_EXPLOSION_EMITTER);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "fart"), FART);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImagineAnything.MOD_ID, "giant_vibration"), GIANT_VIBRATION);

        Registry.register(Registry.SOUND_EVENT, POOF_SOUND, POOF_EVENT);
        Registry.register(Registry.SOUND_EVENT, FART_SOUND, FART_EVENT);
        Registry.register(Registry.SOUND_EVENT, BEDROCK_SPEAKER_SOUND, BEDROCK_SPEAKER_EVENT);
    }
}
