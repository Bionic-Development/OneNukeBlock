package de.takacick.deathmoney.damage;

import net.minecraft.entity.damage.DamageSource;

public class DeathDamageSources {

    public static final DeathDamageSource CAKE_EXPLOSION = new DeathDamageSource("cakeExplosion", 15);
    public static final DeathDamageSource SWEET_BERRY_SUIT = new DeathDamageSource("sweetBerryBush", 1);
    public static final DeathDamageSource FIRE_SUIT = new DeathDamageSource("inFire", 35);
    public static final DeathDamageSource CACTUS_ONESIE = new DeathDamageSource("cactus", 12);
    public static final DeathDamageSource GRASS_TOUCH = new DeathDamageSource("grassTouch", 75);
    public static final DeathDamageSource METEOR_EXPLOSION = new DeathDamageSource("meteorExplosion", 250);
    public static final DeathDamageSource LITTLE_WITHER = new DeathDamageSource("littleWither", 3);
    public static final DeathDamageSource DEATH_DROP = new DeathDamageSource("deathDrop", 65);
    public static final DeathDamageSource DANGEROUS_BLOCK = new DeathDamageSource("dangerousBlock", 2);
    public static final DeathDamageSource EARTHS_FANGS = new DeathDamageSource("earthsFangs", 850);
    public static final DeathDamageSource TNT_NUKE = new DeathDamageSource("tntNukes", 1500);
    public static final DeathDamageSource BLOOD_FLOOD = new DeathDamageSource("bloodFlood", 1200);
    public static final DeathDamageSource TITAN_PUNCH = new DeathDamageSource("titanPunch", 165);
    public static final DeathDamageSource GIRLFRIEND_PUNCH = new DeathDamageSource("girlfriendPunch", 125);

    public static class DeathDamageSource extends DamageSource {

        private final int deaths;

        public DeathDamageSource(String name, int deaths) {
            super(name);
            this.deaths = deaths;
        }

        public int getDeaths() {
            return deaths;
        }
    }
}
