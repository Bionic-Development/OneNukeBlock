package de.takacick.heartmoney.registry;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.particles.ColoredParticleEffect;
import de.takacick.heartmoney.registry.particles.ShockwaveParticleEffect;
import de.takacick.heartmoney.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.heartmoney.registry.particles.goop.GoopParticleEffect;
import de.takacick.heartmoney.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final ParticleType<ColoredParticleEffect> COLORED_GLOW_SPARK = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_GLOW_HEART = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_FLASH = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_POOF = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType HEART = FabricParticleTypes.simple();
    public static final ParticleType<ShockwaveParticleEffect> HEART_SHOCKWAVE = FabricParticleTypes.complex(ShockwaveParticleEffect.FACTORY);
    public static final DefaultParticleType HEART_SWEEP_ATTACK = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_EXPLOSION = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_TOTEM = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_POOF = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_SONIC_BOOM = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_PORTAL = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_SOUL = FabricParticleTypes.simple();
    public static final DefaultParticleType ANGEL_DUST = FabricParticleTypes.simple();
    public static final ParticleType<ShockwaveParticleEffect> ANGEL_SHOCKWAVE = FabricParticleTypes.complex(ShockwaveParticleEffect.FACTORY);
    public static final DefaultParticleType BLOOD_SWEEP_ATTACK = FabricParticleTypes.simple();
    public static final DefaultParticleType BLOOD_PORTAL = FabricParticleTypes.simple();
    public static final DefaultParticleType HEART_FIRE = FabricParticleTypes.simple();

    public static final SoundEvent UWU = new SoundEvent(new Identifier(HeartMoney.MOD_ID, "uwu"));

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "colored_glow_spark"), COLORED_GLOW_SPARK);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "colored_glow_heart"), COLORED_GLOW_HEART);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "colored_flash"), COLORED_FLASH);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "colored_poof"), COLORED_POOF);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "heart"), HEART);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "heart_shockwave"), HEART_SHOCKWAVE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "heart_sweep_attack"), HEART_SWEEP_ATTACK);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "heart_explosion_emitter"), HEART_EXPLOSION_EMITTER);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "heart_explosion"), HEART_EXPLOSION);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "heart_totem"), HEART_TOTEM);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "heart_poof"), HEART_POOF);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "heart_sonic_boom"), HEART_SONIC_BOOM);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "heart_portal"), HEART_PORTAL);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "heart_soul"), HEART_SOUL);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "angel_dust"), ANGEL_DUST);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "angel_shockwave"), ANGEL_SHOCKWAVE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "blood_sweep_attack"), BLOOD_SWEEP_ATTACK);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "blood_portal"), BLOOD_PORTAL);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(HeartMoney.MOD_ID, "heart_fire"), HEART_FIRE);

        Registry.register(Registry.SOUND_EVENT, new Identifier(HeartMoney.MOD_ID, "uwu"), UWU);
    }
}
