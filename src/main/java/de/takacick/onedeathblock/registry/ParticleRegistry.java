package de.takacick.onedeathblock.registry;

import de.takacick.onedeathblock.OneDeathBlock;
import de.takacick.onedeathblock.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.onedeathblock.registry.particles.goop.GoopParticleEffect;
import de.takacick.onedeathblock.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(OneDeathBlock.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(OneDeathBlock.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(OneDeathBlock.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final DefaultParticleType SMOKE = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(OneDeathBlock.MOD_ID, "smoke"), SMOKE);
    }
}
