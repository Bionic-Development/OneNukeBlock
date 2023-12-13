package de.takacick.upgradebody.registry;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.registry.particles.ColoredParticleEffect;
import de.takacick.upgradebody.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.upgradebody.registry.particles.goop.GoopParticleEffect;
import de.takacick.upgradebody.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final ParticleType<ColoredParticleEffect> DUST = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType ENERGY_EXPLOSION = FabricParticleTypes.simple();
    public static final DefaultParticleType ENERGY_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final DefaultParticleType UPGRADE_PORTAL = FabricParticleTypes.simple();
    public static final DefaultParticleType XP_TOTEM = FabricParticleTypes.simple();
    public static final DefaultParticleType XP_EXPLOSION = FabricParticleTypes.simple();
    public static final DefaultParticleType XP_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final DefaultParticleType PLAYER_SLIME = FabricParticleTypes.simple();
    public static final DefaultParticleType XP_FLASH = FabricParticleTypes.simple();
    public static final DefaultParticleType FALLING_BLOOD = FabricParticleTypes.simple();
    public static final DefaultParticleType BLOOD_SPLASH = FabricParticleTypes.simple();
    public static final DefaultParticleType ENERGY_PORTAL = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "dust"), DUST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "energy_explosion"), ENERGY_EXPLOSION);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "energy_explosion_emitter"), ENERGY_EXPLOSION_EMITTER);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "upgrade_portal"), UPGRADE_PORTAL);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "xp_totem"), XP_TOTEM);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "xp_explosion"), XP_EXPLOSION);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "xp_explosion_emitter"), XP_EXPLOSION_EMITTER);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "player_slime"), PLAYER_SLIME);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "xp_flash"), XP_FLASH);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "falling_blood"), FALLING_BLOOD);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "blood_splash"), BLOOD_SPLASH);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(UpgradeBody.MOD_ID, "energy_portal"), ENERGY_PORTAL);
    }
}
