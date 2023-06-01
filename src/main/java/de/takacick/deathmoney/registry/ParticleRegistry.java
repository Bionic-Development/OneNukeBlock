package de.takacick.deathmoney.registry;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.particles.ColoredParticleEffect;
import de.takacick.deathmoney.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.deathmoney.registry.particles.goop.GoopParticleEffect;
import de.takacick.deathmoney.registry.particles.goop.GoopStringParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleRegistry {

    public static final ParticleType<GoopDropParticleEffect> GOOP_DROP =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "goop_drop"),
                    FabricParticleTypes.complex(new GoopDropParticleEffect.Factory()));
    public static final ParticleType<GoopParticleEffect> GOOP =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "goop"),
                    FabricParticleTypes.complex(new GoopParticleEffect.Factory()));
    public static final ParticleType<GoopStringParticleEffect> GOOP_STRING =
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "goop_string"),
                    FabricParticleTypes.complex(new GoopStringParticleEffect.Factory()));

    public static final DefaultParticleType SMOKE = FabricParticleTypes.simple();

    public static final ParticleType<ColoredParticleEffect> COLORED_GLOW_SPARK = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_FLASH = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final ParticleType<ColoredParticleEffect> COLORED_POOF = FabricParticleTypes.complex(ColoredParticleEffect.FACTORY);
    public static final DefaultParticleType HEART = FabricParticleTypes.simple();
    public static final DefaultParticleType DEATH_PORTAL = FabricParticleTypes.simple();
    public static final DefaultParticleType DEATH_SOUL = FabricParticleTypes.simple();
    public static final DefaultParticleType BLACK_MATTER_TOTEM = FabricParticleTypes.simple();
    public static final DefaultParticleType BLACK_MATTER_PORTAL = FabricParticleTypes.simple();
    public static final DefaultParticleType BLACK_MATTER_EXPLOSION = FabricParticleTypes.simple();

    public static final SoundEvent DEATH_SHOP_EMERGE = new SoundEvent(new Identifier(DeathMoney.MOD_ID, "death_shop_emerge"));

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "smoke"), SMOKE);

        Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "colored_glow_spark"), COLORED_GLOW_SPARK);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "colored_flash"), COLORED_FLASH);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "colored_poof"), COLORED_POOF);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "heart"), HEART);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "death_portal"), DEATH_PORTAL);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "death_soul"), DEATH_SOUL);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "black_matter_totem"), BLACK_MATTER_TOTEM);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "black_matter_portal"), BLACK_MATTER_PORTAL);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(DeathMoney.MOD_ID, "black_matter_explosion"), BLACK_MATTER_EXPLOSION);

        Registry.register(Registry.SOUND_EVENT, new Identifier(DeathMoney.MOD_ID, "death_shop_emerge"), DEATH_SHOP_EMERGE);
    }
}
