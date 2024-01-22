package de.takacick.illegalwars.registry;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.particles.ColoredParticleEffect;
import de.takacick.illegalwars.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.illegalwars.registry.particles.goop.GoopParticleEffect;
import de.takacick.illegalwars.registry.particles.goop.GoopStringParticleEffect;
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
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final ParticleType<ColoredParticleEffect> COLORED_EXPLOSION = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_EXPLOSION_EMITTER = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> FLYING_DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_FLASH = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_FIREWORK = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> LASER_DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType FALLING_BLOOD = FabricParticleTypes.simple();
    public static final DefaultParticleType BLOOD_SPLASH = FabricParticleTypes.simple();
    public static final DefaultParticleType POOP = FabricParticleTypes.simple();

    public static final SoundEvent RAT_DEATH = SoundEvent.of(new Identifier(IllegalWars.MOD_ID, "rat_death"));
    public static final SoundEvent RAT_HURT = SoundEvent.of(new Identifier(IllegalWars.MOD_ID, "rat_hurt"));
    public static final SoundEvent RAT_SCREAM = SoundEvent.of(new Identifier(IllegalWars.MOD_ID, "rat_scream"));
    public static final SoundEvent RAT_SQUEAK = SoundEvent.of(new Identifier(IllegalWars.MOD_ID, "rat_squeak"));
    public static final SoundEvent FART = SoundEvent.of(new Identifier(IllegalWars.MOD_ID, "fart"));
    public static final SoundEvent SHARK_BITE = SoundEvent.of(new Identifier(IllegalWars.MOD_ID, "shark_bite"));
    public static final SoundEvent MONEY_REWARD = SoundEvent.of(new Identifier(IllegalWars.MOD_ID, "money_reward"));
    public static final DefaultParticleType DOLLAR = FabricParticleTypes.simple();
    public static final DefaultParticleType COIN = FabricParticleTypes.simple();
    public static final DefaultParticleType FALLING_SLUDGE = FabricParticleTypes.simple();
    public static final DefaultParticleType DRIPPING_SLUDGE = FabricParticleTypes.simple();
    public static final DefaultParticleType LANDING_SLUDGE = FabricParticleTypes.simple();
    public static final DefaultParticleType SLUDGE_BUBBLE = FabricParticleTypes.simple();
    public static final DefaultParticleType SLUDGE_BUBBLE_POP = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "colored_explosion"), COLORED_EXPLOSION);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "colored_explosion_emitter"), COLORED_EXPLOSION_EMITTER);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "dust"), DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "flying_dust"), FLYING_DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "colored_flash"), COLORED_FLASH);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "colored_firework"), COLORED_FIREWORK);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "laser_dust"), LASER_DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "falling_blood"), FALLING_BLOOD);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "blood_splash"), BLOOD_SPLASH);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "poop"), POOP);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "dollar"), DOLLAR);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "coin"), COIN);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "falling_sludge"), FALLING_SLUDGE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "dripping_sludge"), DRIPPING_SLUDGE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "landing_sludge"), LANDING_SLUDGE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "sludge_bubble"), SLUDGE_BUBBLE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IllegalWars.MOD_ID, "sludge_bubble_pop"), SLUDGE_BUBBLE_POP);

        Registry.register(Registries.SOUND_EVENT, RAT_DEATH.getId(), RAT_DEATH);
        Registry.register(Registries.SOUND_EVENT, RAT_HURT.getId(), RAT_HURT);
        Registry.register(Registries.SOUND_EVENT, RAT_SCREAM.getId(), RAT_SCREAM);
        Registry.register(Registries.SOUND_EVENT, RAT_SQUEAK.getId(), RAT_SQUEAK);
        Registry.register(Registries.SOUND_EVENT, FART.getId(), FART);
        Registry.register(Registries.SOUND_EVENT, SHARK_BITE.getId(), SHARK_BITE);
        Registry.register(Registries.SOUND_EVENT, MONEY_REWARD.getId(), MONEY_REWARD);
    }
}
