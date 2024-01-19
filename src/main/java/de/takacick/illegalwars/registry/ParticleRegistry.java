package de.takacick.illegalwars.registry;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.particles.ColoredParticleEffect;
import de.takacick.illegalwars.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.illegalwars.registry.particles.goop.GoopParticleEffect;
import de.takacick.illegalwars.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final ParticleType<ColoredParticleEffect> DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "dust"), DUST);
    }
}
