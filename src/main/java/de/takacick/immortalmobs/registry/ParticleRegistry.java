package de.takacick.immortalmobs.registry;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.registry.particles.ImmortalWolfParticleEffect;
import de.takacick.immortalmobs.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.immortalmobs.registry.particles.goop.GoopParticleEffect;
import de.takacick.immortalmobs.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final DefaultParticleType IMMORTAL_EXPLOSION = FabricParticleTypes.simple();
    public static final DefaultParticleType IMMORTAL_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final DefaultParticleType IMMORTAL_SWEEP_ATTACK = FabricParticleTypes.simple();
    public static final ParticleType<ImmortalWolfParticleEffect> IMMORTAL_WOLF = FabricParticleTypes.complex(ImmortalWolfParticleEffect.FACTORY);
    public static final DefaultParticleType IMMORTAL_TOTEM = FabricParticleTypes.simple();
    public static final DefaultParticleType IMMORTAL_GLOW_TOTEM = FabricParticleTypes.simple();
    public static final DefaultParticleType IMMORTAL_POOF = FabricParticleTypes.simple();
    public static final DefaultParticleType IMMORTAL_PORTAL = FabricParticleTypes.simple();
    public static final DefaultParticleType IMMORTAL_FIREWORK = FabricParticleTypes.simple();
    public static final DefaultParticleType IMMORTAL_FLAME = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_explosion"), IMMORTAL_EXPLOSION);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_explosion_emitter"), IMMORTAL_EXPLOSION_EMITTER);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_sweep_attack"), IMMORTAL_SWEEP_ATTACK);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_wolf"), IMMORTAL_WOLF);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_totem"), IMMORTAL_TOTEM);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_glow_totem"), IMMORTAL_GLOW_TOTEM);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_poof"), IMMORTAL_POOF);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_portal"), IMMORTAL_PORTAL);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_firework"), IMMORTAL_FIREWORK);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImmortalMobs.MOD_ID, "immortal_flame"), IMMORTAL_FLAME);
    }
}
