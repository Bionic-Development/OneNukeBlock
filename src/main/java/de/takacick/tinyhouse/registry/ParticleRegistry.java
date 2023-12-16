package de.takacick.tinyhouse.registry;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.registry.particles.ColoredParticleEffect;
import de.takacick.tinyhouse.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.tinyhouse.registry.particles.goop.GoopParticleEffect;
import de.takacick.tinyhouse.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(TinyHouse.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(TinyHouse.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(TinyHouse.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final ParticleType<ColoredParticleEffect> DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType FALLING_BLOOD = FabricParticleTypes.simple();
    public static final DefaultParticleType BLOOD_SPLASH = FabricParticleTypes.simple();
    public static final DefaultParticleType FEATHER = FabricParticleTypes.simple();

    public static final SoundEvent RAT_DEATH = SoundEvent.of(new Identifier(TinyHouse.MOD_ID, "rat_death"));
    public static final SoundEvent RAT_HURT = SoundEvent.of(new Identifier(TinyHouse.MOD_ID, "rat_hurt"));
    public static final SoundEvent ICE_FREEZE = SoundEvent.of(new Identifier(TinyHouse.MOD_ID, "ice_freeze"));

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TinyHouse.MOD_ID, "dust"), DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TinyHouse.MOD_ID, "falling_blood"), FALLING_BLOOD);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TinyHouse.MOD_ID, "blood_splash"), BLOOD_SPLASH);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TinyHouse.MOD_ID, "feather"), FEATHER);

        Registry.register(Registries.SOUND_EVENT, RAT_DEATH.getId(), RAT_DEATH);
        Registry.register(Registries.SOUND_EVENT, RAT_HURT.getId(), RAT_HURT);
        Registry.register(Registries.SOUND_EVENT, ICE_FREEZE.getId(), ICE_FREEZE);
    }
}
