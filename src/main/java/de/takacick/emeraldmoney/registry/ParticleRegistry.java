package de.takacick.emeraldmoney.registry;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.registry.particles.ColoredParticleEffect;
import de.takacick.emeraldmoney.registry.particles.TargetParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final ParticleType<ColoredParticleEffect> DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> TOTEM_DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType EMERALD_PORTAL = FabricParticleTypes.simple();
    public static final DefaultParticleType EMERALD_TOTEM = FabricParticleTypes.simple();
    public static final DefaultParticleType EMERALD_ITEMS = FabricParticleTypes.simple();
    public static final DefaultParticleType EMERALD_EXPLOSION = FabricParticleTypes.simple();
    public static final DefaultParticleType EMERALD_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final DefaultParticleType EMERALD_POOF = FabricParticleTypes.simple();
    public static final ParticleType<TargetParticleEffect> EMERALD_SPELL = FabricParticleTypes.complex(TargetParticleEffect.FACTORY);

    public static final SoundEvent VILLAGER_EXPLODE = SoundEvent.of(new Identifier(EmeraldMoney.MOD_ID, "villager_explode"));

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(EmeraldMoney.MOD_ID, "dust"), DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(EmeraldMoney.MOD_ID, "totem_dust"), TOTEM_DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(EmeraldMoney.MOD_ID, "emerald_poof"), EMERALD_POOF);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(EmeraldMoney.MOD_ID, "emerald_portal"), EMERALD_PORTAL);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(EmeraldMoney.MOD_ID, "emerald_totem"), EMERALD_TOTEM);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(EmeraldMoney.MOD_ID, "emerald_items"), EMERALD_ITEMS);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(EmeraldMoney.MOD_ID, "emerald_explosion"), EMERALD_EXPLOSION);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(EmeraldMoney.MOD_ID, "emerald_explosion_emitter"), EMERALD_EXPLOSION_EMITTER);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(EmeraldMoney.MOD_ID, "emerald_spell"), EMERALD_SPELL);

        Registry.register(Registries.SOUND_EVENT, VILLAGER_EXPLODE.getId(), VILLAGER_EXPLODE);
    }
}
