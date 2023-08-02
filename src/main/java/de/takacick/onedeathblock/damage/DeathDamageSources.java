package de.takacick.onedeathblock.damage;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.random.Random;

import java.util.function.Function;

public class DeathDamageSources {

    public static final DeathDamageSource TNT_EXPLOSION = new DeathDamageSource("tntExplosion", (r) -> r.nextBetween(20, 40));
    public static final DeathDamageSource SPIKY_IRON = new DeathDamageSource("spikyIron", (r) -> 1);
    public static final DeathDamageSource SPIKY_IRON_PICKAXE = new DeathDamageSource("spikyIronPickaxe", (r) -> 1);
    public static final DeathDamageSource DEATH_BLOCK = new DeathDamageSource("deathBlock", (r) -> 1);
    public static final DeathDamageSource SPIKED_BED = new DeathDamageSource("spikedBed", (r) -> 1);
    public static final DeathDamageSource LEGO_CARPET = new DeathDamageSource("legoCarpet", (r) -> r.nextBetween(2, 10));
    public static final DeathDamageSource SPIKY_IRON_ARMOR = new DeathDamageSource("spikyIronArmor", (r) -> r.nextBetween(5, 20));
    public static final DeathDamageSource ELECTRICUTIONER_DOOR = new DeathDamageSource("electricutionerDoor", (r) -> r.nextBetween(20, 40));

    public static class DeathDamageSource extends DamageSource {

        private final Function<Random, Integer> deaths;

        public DeathDamageSource(String name, Function<Random, Integer> deaths) {
            super(name);
            this.deaths = deaths;
        }

        public int getDeaths(Random random) {
            return deaths.apply(random);
        }
    }
}
