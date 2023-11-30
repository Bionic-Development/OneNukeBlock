package de.takacick.elementalblock.registry;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.particles.ColoredParticleEffect;
import de.takacick.elementalblock.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.elementalblock.registry.particles.goop.GoopParticleEffect;
import de.takacick.elementalblock.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final ParticleType<ColoredParticleEffect> DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_GLOW_SPARK = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> FALLING_WATER = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> WATER_SPLASH = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType WATER_SLIME = FabricParticleTypes.simple();
    public static final DefaultParticleType WATER_POOF = FabricParticleTypes.simple();
    public static final DefaultParticleType CLOUD = FabricParticleTypes.simple();
    public static final DefaultParticleType MAGMA = FabricParticleTypes.simple();
    public static final DefaultParticleType LAVA_TOTEM = FabricParticleTypes.simple();
    public static final DefaultParticleType MAGIC_LAVA = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "dust"), DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "falling_water"), FALLING_WATER);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "water_splash"), WATER_SPLASH);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "water_slime"), WATER_SLIME);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "water_poof"), WATER_POOF);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "cloud"), CLOUD);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "colored_glow_spark"), COLORED_GLOW_SPARK);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "magma"), MAGMA);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "lava_totem"), LAVA_TOTEM);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "magic_lava"), MAGIC_LAVA);
    }
}
