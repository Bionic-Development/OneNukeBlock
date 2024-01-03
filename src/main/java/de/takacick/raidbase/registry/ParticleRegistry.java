package de.takacick.raidbase.registry;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.registry.particles.ColoredParticleEffect;
import de.takacick.raidbase.registry.particles.ShockwaveParticleEffect;
import de.takacick.raidbase.registry.particles.TargetParticleEffect;
import de.takacick.raidbase.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.raidbase.registry.particles.goop.GoopParticleEffect;
import de.takacick.raidbase.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final ParticleType<ColoredParticleEffect> LASER_DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ShockwaveParticleEffect> BAN_SHOCKWAVE = FabricParticleTypes.complex(ShockwaveParticleEffect.FACTORY);
    public static final DefaultParticleType GLITCHED = FabricParticleTypes.simple();
    public static final DefaultParticleType LIGHTNING = FabricParticleTypes.simple();
    public static final DefaultParticleType QUICKSAND_DUST = FabricParticleTypes.simple();
    public static final DefaultParticleType SLIME = FabricParticleTypes.simple();
    public static final ParticleType<TargetParticleEffect> HACK_TARGET = FabricParticleTypes.complex(TargetParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> TRANSPARENT_DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "laser_dust"), LASER_DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "ban_shockwave"), BAN_SHOCKWAVE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "glitched"), GLITCHED);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "lightning"), LIGHTNING);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "quicksand_dust"), QUICKSAND_DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "slime"), SLIME);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "hack_target"), HACK_TARGET);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "dust"), DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RaidBase.MOD_ID, "transparent_dust"), TRANSPARENT_DUST);
    }
}
