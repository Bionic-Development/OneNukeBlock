package de.takacick.onenukeblock.utils.explosion;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import de.takacick.onenukeblock.utils.network.BangMaceBoostExplosionPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class BangMaceBoostExplosion extends Explosion {

    public BangMaceBoostExplosion(World world, @Nullable Entity entity, double x, double y, double z, float power, List<BlockPos> affectedBlocks, DestructionType destructionType, ParticleEffect particle, ParticleEffect emitterParticle, RegistryEntry<SoundEvent> soundEvent) {
        super(world, entity, x, y, z, power, affectedBlocks, destructionType, particle, emitterParticle, soundEvent);
    }

    public BangMaceBoostExplosion(World world, @Nullable Entity entity, double x, double y, double z, float power, boolean createFire, DestructionType destructionType, List<BlockPos> affectedBlocks) {
        super(world, entity, x, y, z, power, createFire, destructionType, affectedBlocks);
    }

    public BangMaceBoostExplosion(World world, @Nullable Entity entity, double x, double y, double z, float power, boolean createFire, DestructionType destructionType) {
        super(world, entity, x, y, z, power, createFire, destructionType);
    }

    public BangMaceBoostExplosion(World world, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, DestructionType destructionType, ParticleEffect particle, ParticleEffect emitterParticle, RegistryEntry<SoundEvent> soundEvent) {
        super(world, entity, damageSource, behavior, x, y, z, power, createFire, destructionType, particle, emitterParticle, soundEvent);
    }

    @Override
    public void collectBlocksAndDamageEntities() {
        int l;
        int k;
        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new Vec3d(this.x, this.y, this.z));
        HashSet<BlockPos> set = Sets.newHashSet();
        int i = 16;
        for (int j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                block2:
                for (l = 0; l < 16; ++l) {
                    if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                    double d = (float) j / 15.0f * 2.0f - 1.0f;
                    double e = (float) k / 15.0f * 2.0f - 1.0f;
                    double f = (float) l / 15.0f * 2.0f - 1.0f;
                    double g = Math.sqrt(d * d + e * e + f * f);
                    d /= g;
                    e /= g;
                    f /= g;
                    double m = this.x;
                    double n = this.y;
                    double o = this.z;
                    float p = 0.3f;
                    for (float h = this.power * (0.7f + this.world.random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                        BlockPos blockPos = BlockPos.ofFloored(m, n, o);
                        BlockState blockState = this.world.getBlockState(blockPos);
                        FluidState fluidState = this.world.getFluidState(blockPos);
                        if (!this.world.isInBuildLimit(blockPos)) continue block2;
                        Optional<Float> optional = this.behavior.getBlastResistance(this, this.world, blockPos, blockState, fluidState);
                        if (optional.isPresent()) {
                            h -= (optional.get().floatValue() + 0.3f) * 0.3f;
                        }
                        if (h > 0.0f && this.behavior.canDestroyBlock(this, this.world, blockPos, blockState, h)) {
                            set.add(blockPos);
                        }
                        m += d * (double) 0.3f;
                        n += e * (double) 0.3f;
                        o += f * (double) 0.3f;
                    }
                }
            }
        }
        this.affectedBlocks.addAll(set);
        float q = this.power * 2.0f;
        k = MathHelper.floor(this.x - (double) q - 1.0);
        l = MathHelper.floor(this.x + (double) q + 1.0);
        int r = MathHelper.floor(this.y - (double) q - 1.0);
        int s = MathHelper.floor(this.y + (double) q + 1.0);
        int t = MathHelper.floor(this.z - (double) q - 1.0);
        int u = MathHelper.floor(this.z + (double) q + 1.0);
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);
        for (Entity entity : List.of(this.entity)) {
            PlayerEntity playerEntity;
            double ab;
            double y;
            double x;
            double w;
            double z;
            double v;
            if (entity.isImmuneToExplosion(this) || !((v = Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double) q) <= 1.0) || (z = Math.sqrt((w = entity.getX() - this.x) * w + (x = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y) * x + (y = entity.getZ() - this.z) * y)) == 0.0)
                continue;

            x = entity.getStandingEyeHeight();

            w /= z;
            x /= z;
            y /= z;

            ab = (1.0 - v) * (double) Explosion.getExposure(vec3d, entity) * (double) this.behavior.getKnockbackModifier(entity);
            Vec3d vec3d2 = new Vec3d(w *= ab, Math.max(x *= ab, 1.2), y *= ab).multiply(1.8);
            entity.setVelocity(entity.getVelocity().add(vec3d2));
            if (!(!(entity instanceof PlayerEntity) || (playerEntity = (PlayerEntity) entity).isSpectator() || playerEntity.isCreative() && playerEntity.getAbilities().flying)) {
                this.affectedPlayers.put(playerEntity, vec3d2);
            }
            entity.onExplodedBy(this.entity);
        }
    }

    public void affectWorld(boolean particles) {
        if (this.world.isClient) {
            this.world.playSound(this.x, this.y, this.z, this.soundEvent.value(), SoundCategory.BLOCKS, 4.0f, (1.0f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2f) * 0.7f, false);
        }
        boolean bl = this.shouldDestroy();
        if (particles) {
            ParticleEffect particleEffect = this.power < 2.0f || !bl ? this.particle : this.emitterParticle;
            this.world.addImportantParticle(particleEffect, true, this.x, this.y, this.z, 1.0, 0.0, 0.0);
        }
        if (bl) {
            this.world.getProfiler().push("explosion_blocks");
            ArrayList<Pair<ItemStack, BlockPos>> list = new ArrayList<>();
            Util.shuffle(this.affectedBlocks, this.world.random);
            for (BlockPos blockPos : this.affectedBlocks) {
                this.world.getBlockState(blockPos).onExploded(this.world, blockPos, this, (stack, pos) -> Explosion.tryMergeStack(list, stack, pos));
            }
            for (Pair<ItemStack, BlockPos> pair : list) {
                Block.dropStack(this.world, pair.getSecond(), pair.getFirst());
            }
            this.world.getProfiler().pop();
        }
        if (this.createFire) {
            for (BlockPos blockPos2 : this.affectedBlocks) {
                if (this.random.nextInt(3) != 0 || !this.world.getBlockState(blockPos2).isAir() || !this.world.getBlockState(blockPos2.down()).isOpaqueFullCube(this.world, blockPos2.down()))
                    continue;
                this.world.setBlockState(blockPos2, AbstractFireBlock.getState(this.world, blockPos2));
            }
        }
    }

    public static Explosion createExplosion(ServerWorld world, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, boolean particles, ParticleEffect particle, ParticleEffect emitterParticle, RegistryEntry<SoundEvent> soundEvent) {
        Explosion.DestructionType destructionType = DestructionType.KEEP;
        Explosion explosion = new BangMaceBoostExplosion(world, entity, damageSource, behavior, x, y, z, power, createFire, destructionType, particle, emitterParticle, soundEvent);
        explosion.collectBlocksAndDamageEntities();
        explosion.affectWorld(particles);

        if (!explosion.shouldDestroy()) {
            explosion.clearAffectedBlocks();
        }
        for (ServerPlayerEntity serverPlayerEntity : world.getPlayers()) {
            if (!(serverPlayerEntity.squaredDistanceTo(x, y, z) < 4096.0)) continue;
            var packet = new BangMaceBoostExplosionPacket(x, y, z, power, explosion.getAffectedBlocks(), explosion.getAffectedPlayers().get(serverPlayerEntity), explosion.getDestructionType(), explosion.getParticle(), explosion.getEmitterParticle(), explosion.getSoundEvent());
            ServerPlayNetworking.send(serverPlayerEntity, packet);
        }

        return explosion;
    }
}

