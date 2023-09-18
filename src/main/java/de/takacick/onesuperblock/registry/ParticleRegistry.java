package de.takacick.onesuperblock.registry;

import de.takacick.onesuperblock.OneSuperBlock;
import de.takacick.superitems.registry.particles.RainbowParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleRegistry {

    public static final ParticleType<BlockStateParticleEffect> RAINBOW_BLOCK = FabricParticleTypes.complex(BlockStateParticleEffect.PARAMETERS_FACTORY);
    public static final ParticleType<RainbowParticleEffect> RAINBOW_SONIC_BOOM = FabricParticleTypes.complex(RainbowParticleEffect.FACTORY);
    public static final ParticleType<RainbowParticleEffect> RAINBOW_DUST = FabricParticleTypes.complex(RainbowParticleEffect.FACTORY);
    public static final ParticleType<RainbowParticleEffect> RAINBOW_GLYPH = FabricParticleTypes.complex(RainbowParticleEffect.FACTORY);
    public static final ParticleType<RainbowParticleEffect> RAINBOW_ITEM = FabricParticleTypes.complex(RainbowParticleEffect.FACTORY);

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(OneSuperBlock.MOD_ID, "rainbow_block"), RAINBOW_BLOCK);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(OneSuperBlock.MOD_ID, "rainbow_sonic_boom"), RAINBOW_SONIC_BOOM);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(OneSuperBlock.MOD_ID, "rainbow_dust"), RAINBOW_DUST);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(OneSuperBlock.MOD_ID, "rainbow_glyph"), RAINBOW_GLYPH);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(OneSuperBlock.MOD_ID, "rainbow_item"), RAINBOW_ITEM);
    }
}
