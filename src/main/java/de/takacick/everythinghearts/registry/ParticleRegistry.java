package de.takacick.everythinghearts.registry;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.registry.particles.ColoredParticleEffect;
import de.takacick.everythinghearts.registry.particles.HeartUpgradeParticleEffect;
import de.takacick.everythinghearts.registry.particles.ShockwaveParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleRegistry {

    public static final ParticleType<ColoredParticleEffect> COLORED_GLOW_SPARK = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_FLASH = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType HEART = FabricParticleTypes.simple();
    public static final ParticleType<ShockwaveParticleEffect> HEART_SHOCKWAVE = FabricParticleTypes.complex(ShockwaveParticleEffect.FACTORY);
    public static final DefaultParticleType HEART_SWEEP_ATTACK = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_EXPLOSION = FabricParticleTypes.simple();
    public static final ParticleType<HeartUpgradeParticleEffect> HEART_UPGRADE = FabricParticleTypes.complex(HeartUpgradeParticleEffect.FACTORY);
    public static final DefaultParticleType HEART_TOTEM = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_POOF = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_SONIC_BOOM = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_SPLASH = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "colored_glow_spark"), COLORED_GLOW_SPARK);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "colored_flash"), COLORED_FLASH);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "heart"), HEART);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "heart_shockwave"), HEART_SHOCKWAVE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "heart_sweep_attack"), HEART_SWEEP_ATTACK);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "heart_explosion_emitter"), HEART_EXPLOSION_EMITTER);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "heart_explosion"), HEART_EXPLOSION);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "heart_upgrade"), HEART_UPGRADE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "heart_totem"), HEART_TOTEM);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "heart_poof"), HEART_POOF);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "heart_sonic_boom"), HEART_SONIC_BOOM);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(EverythingHearts.MOD_ID, "heart_splash"), HEART_SPLASH);
    }
}
