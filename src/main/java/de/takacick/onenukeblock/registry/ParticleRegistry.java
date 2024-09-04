package de.takacick.onenukeblock.registry;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.onenukeblock.registry.particles.goop.GoopParticleEffect;
import de.takacick.onenukeblock.registry.particles.goop.GoopStringParticleEffect;
import de.takacick.utils.bossbar.BossBarColorRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final BossBar.Color NUKE_BOSSBAR = BossBarColorRegistry.register(Identifier.of(OneNukeBlock.MOD_ID, "nuke_bossbar"));

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(GoopDropParticleEffect.CODEC, GoopDropParticleEffect.PACKET_CODEC));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "goop"),
                    FabricParticleTypes.complex(GoopParticleEffect.CODEC, GoopParticleEffect.PACKET_CODEC));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(GoopStringParticleEffect.CODEC, GoopStringParticleEffect.PACKET_CODEC));

    public static final SimpleParticleType NUCLEAR_BUBBLE = FabricParticleTypes.simple();
    public static final SimpleParticleType NUCLEAR_BUBBLE_COLUMN_UP = FabricParticleTypes.simple();
    public static final SimpleParticleType NUCLEAR_BUBBLE_POP = FabricParticleTypes.simple();
    public static final SimpleParticleType NUCLEAR_SPLASH = FabricParticleTypes.simple();
    public static final SimpleParticleType NUCLEAR_CURRENT_DOWN = FabricParticleTypes.simple();
    public static final SimpleParticleType NUCLEAR_UNDERWATER = FabricParticleTypes.simple();

    public static final SimpleParticleType FALLING_BLOOD = FabricParticleTypes.simple();
    public static final SimpleParticleType BLOOD_SPLASH = FabricParticleTypes.simple();
    public static final SimpleParticleType SMOKE = FabricParticleTypes.simple();

    public static final SoundEvent BLOOD_DROP = SoundEvent.of(Identifier.of(OneNukeBlock.MOD_ID, "blood_drop"));
    public static final SoundEvent NUKE_ALERT = SoundEvent.of(Identifier.of(OneNukeBlock.MOD_ID, "nuke_alert"));

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "falling_blood"), FALLING_BLOOD);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "blood_splash"), BLOOD_SPLASH);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "smoke"), SMOKE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "nuclear_bubble"), NUCLEAR_BUBBLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "nuclear_bubble_column_up"), NUCLEAR_BUBBLE_COLUMN_UP);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "nuclear_bubble_pop"), NUCLEAR_BUBBLE_POP);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "nuclear_splash"), NUCLEAR_SPLASH);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "nuclear_current_down"), NUCLEAR_CURRENT_DOWN);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "nuclear_underwater"), NUCLEAR_UNDERWATER);

        Registry.register(Registries.SOUND_EVENT, BLOOD_DROP.getId(), BLOOD_DROP);
        Registry.register(Registries.SOUND_EVENT, NUKE_ALERT.getId(), NUKE_ALERT);
    }
}
