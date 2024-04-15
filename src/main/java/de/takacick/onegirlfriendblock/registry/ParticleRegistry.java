package de.takacick.onegirlfriendblock.registry;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.particles.ColoredParticleEffect;
import de.takacick.onegirlfriendblock.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.onegirlfriendblock.registry.particles.goop.GoopParticleEffect;
import de.takacick.onegirlfriendblock.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final DefaultParticleType FALLING_BLOOD = FabricParticleTypes.simple();
    public static final DefaultParticleType BLOOD_SPLASH = FabricParticleTypes.simple();
    public static final ParticleType<ColoredParticleEffect> COLORED_FIREWORK = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_BUBBLE_DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_POOF = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType HEART = FabricParticleTypes.simple();
    public static final DefaultParticleType LIPSTICK_SWEEP = FabricParticleTypes.simple();
    public static final DefaultParticleType LIPSTICK = FabricParticleTypes.simple();
    public static final DefaultParticleType FEATHER = FabricParticleTypes.simple();

    public static final SoundEvent BLOOD_DROP = SoundEvent.of(new Identifier(OneGirlfriendBlock.MOD_ID, "blood_drop"));

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "falling_blood"), FALLING_BLOOD);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "blood_splash"), BLOOD_SPLASH);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "colored_firework"), COLORED_FIREWORK);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "colored_dust"), COLORED_DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "colored_bubble_dust"), COLORED_BUBBLE_DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "colored_poof"), COLORED_POOF);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "heart"), HEART);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "lipstick_sweep"), LIPSTICK_SWEEP);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "lipstick"), LIPSTICK);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneGirlfriendBlock.MOD_ID, "feather"), FEATHER);

        Registry.register(Registries.SOUND_EVENT, BLOOD_DROP.getId(), BLOOD_DROP);
    }
}
