package de.takacick.onegirlboyblock.registry;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.registry.particles.EntityParticleEffect;
import de.takacick.onegirlboyblock.registry.particles.TetrisParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final SimpleParticleType GOLD_NUGGET = FabricParticleTypes.simple();
    public static final SimpleParticleType SUGAR = FabricParticleTypes.simple();
    public static final ParticleType<EntityParticleEffect> GROW = FabricParticleTypes.complex(EntityParticleEffect::createCodec, EntityParticleEffect::createPacketCodec);
    public static final ParticleType<EntityParticleEffect> SHRINK = FabricParticleTypes.complex(EntityParticleEffect::createCodec, EntityParticleEffect::createPacketCodec);
    public static final ParticleType<EntityParticleEffect> GIRL_SIZE = FabricParticleTypes.complex(EntityParticleEffect::createCodec, EntityParticleEffect::createPacketCodec);
    public static final SimpleParticleType GLITTER = FabricParticleTypes.simple();
    public static final SimpleParticleType GLITTER_BLADE = FabricParticleTypes.simple();
    public static final SimpleParticleType GIRL_FLASH = FabricParticleTypes.simple();
    public static final SimpleParticleType GLITTER_SWEEP = FabricParticleTypes.simple();
    public static final SimpleParticleType STAR_GLITTER = FabricParticleTypes.simple();
    public static final SimpleParticleType STAR_MINER_GLITTER = FabricParticleTypes.simple();
    public static final ParticleType<TetrisParticleEffect> TETRIS = FabricParticleTypes.complex(TetrisParticleEffect::createCodec, TetrisParticleEffect::createPacketCodec);
    public static final ParticleType<TetrisParticleEffect> TETRIS_GLITTER = FabricParticleTypes.complex(TetrisParticleEffect::createCodec, TetrisParticleEffect::createPacketCodec);
    public static final ParticleType<TetrisParticleEffect> TETRIS_FLASH = FabricParticleTypes.complex(TetrisParticleEffect::createCodec, TetrisParticleEffect::createPacketCodec);
    public static final SimpleParticleType BUTTERFLY_WINGS_GLITTER = FabricParticleTypes.simple();
    public static final SimpleParticleType BUTTERFLY_GLITTER = FabricParticleTypes.simple();

    public static final SoundEvent BLOOD_DROP = SoundEvent.of(Identifier.of(OneGirlBoyBlock.MOD_ID, "blood_drop"));
    public static final SoundEvent SHRINK_SOUND = SoundEvent.of(Identifier.of(OneGirlBoyBlock.MOD_ID, "shrink"));
    public static final SoundEvent GROW_SOUND = SoundEvent.of(Identifier.of(OneGirlBoyBlock.MOD_ID, "grow"));
    public static final SoundEvent BIT_CANNON = SoundEvent.of(Identifier.of(OneGirlBoyBlock.MOD_ID, "bit_cannon"));
    public static final SoundEvent BIT_HIT = SoundEvent.of(Identifier.of(OneGirlBoyBlock.MOD_ID, "bit_hit"));
    public static final SoundEvent BIT_EXPLOSION = SoundEvent.of(Identifier.of(OneGirlBoyBlock.MOD_ID, "bit_explosion"));

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "gold_nugget"), GOLD_NUGGET);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "sugar"), SUGAR);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "grow"), GROW);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "shrink"), SHRINK);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "girl_size"), GIRL_SIZE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "glitter"), GLITTER);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "glitter_blade"), GLITTER_BLADE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "girl_flash"), GIRL_FLASH);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "glitter_sweep"), GLITTER_SWEEP);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "star_glitter"), STAR_GLITTER);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "star_miner_glitter"), STAR_MINER_GLITTER);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "tetris"), TETRIS);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "tetris_flash"), TETRIS_FLASH);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "tetris_glitter"), TETRIS_GLITTER);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "butterfly_wings_glitter"), BUTTERFLY_WINGS_GLITTER);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "butterfly_glitter"), BUTTERFLY_GLITTER);

        Registry.register(Registries.SOUND_EVENT, BLOOD_DROP.getId(), BLOOD_DROP);
        Registry.register(Registries.SOUND_EVENT, SHRINK_SOUND.getId(), SHRINK_SOUND);
        Registry.register(Registries.SOUND_EVENT, GROW_SOUND.getId(), GROW_SOUND);
        Registry.register(Registries.SOUND_EVENT, BIT_CANNON.getId(), BIT_CANNON);
        Registry.register(Registries.SOUND_EVENT, BIT_HIT.getId(), BIT_HIT);
        Registry.register(Registries.SOUND_EVENT, BIT_EXPLOSION.getId(), BIT_EXPLOSION);
    }
}
