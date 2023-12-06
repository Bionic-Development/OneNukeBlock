package de.takacick.emeraldmoney.server.explosion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.EmeraldMoneyClient;
import de.takacick.emeraldmoney.registry.ParticleRegistry;
import de.takacick.emeraldmoney.registry.entity.projectile.CustomBlockEntity;
import de.takacick.emeraldmoney.registry.entity.projectile.PillagerProjectileEntity;
import de.takacick.emeraldmoney.registry.particles.ColoredParticleEffect;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CreepagerExplosionHandler {

    public static final Identifier IDENTIFIER = new Identifier(EmeraldMoney.MOD_ID, "creeper_explosion");
    private final double x;
    private final double y;
    private final double z;
    private final float radius;
    private final List<BlockPos> affectedBlocks;
    private final float playerVelocityX;
    private final float playerVelocityY;
    private final float playerVelocityZ;

    public CreepagerExplosionHandler(double x, double y, double z, float radius, List<BlockPos> affectedBlocks, @Nullable Vec3d playerVelocity) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.affectedBlocks = Lists.newArrayList(affectedBlocks);
        if (playerVelocity != null) {
            this.playerVelocityX = (float) playerVelocity.x;
            this.playerVelocityY = (float) playerVelocity.y;
            this.playerVelocityZ = (float) playerVelocity.z;
        } else {
            this.playerVelocityX = 0.0f;
            this.playerVelocityY = 0.0f;
            this.playerVelocityZ = 0.0f;
        }
    }

    public CreepagerExplosionHandler(PacketByteBuf buf) {
        this.x = buf.readFloat();
        this.y = buf.readFloat();
        this.z = buf.readFloat();
        this.radius = buf.readFloat();
        int i = MathHelper.floor(this.x);
        int j = MathHelper.floor(this.y);
        int k = MathHelper.floor(this.z);
        this.affectedBlocks = buf.readList(buf2 -> {
            int l = buf2.readByte() + i;
            int m = buf2.readByte() + j;
            int n = buf2.readByte() + k;
            return new BlockPos(l, m, n);
        });
        this.playerVelocityX = buf.readFloat();
        this.playerVelocityY = buf.readFloat();
        this.playerVelocityZ = buf.readFloat();
    }

    public void write(PacketByteBuf buf) {
        buf.writeFloat((float) this.x);
        buf.writeFloat((float) this.y);
        buf.writeFloat((float) this.z);
        buf.writeFloat(this.radius);
        int i = MathHelper.floor(this.x);
        int j = MathHelper.floor(this.y);
        int k = MathHelper.floor(this.z);
        buf.writeCollection(this.affectedBlocks, (buf2, pos) -> {
            int l = pos.getX() - i;
            int m = pos.getY() - j;
            int n = pos.getZ() - k;
            buf2.writeByte(l);
            buf2.writeByte(m);
            buf2.writeByte(n);
        });
        buf.writeFloat(this.playerVelocityX);
        buf.writeFloat(this.playerVelocityY);
        buf.writeFloat(this.playerVelocityZ);
    }

    public float getPlayerVelocityX() {
        return this.playerVelocityX;
    }

    public float getPlayerVelocityY() {
        return this.playerVelocityY;
    }

    public float getPlayerVelocityZ() {
        return this.playerVelocityZ;
    }


    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getRadius() {
        return this.radius;
    }

    public List<BlockPos> getAffectedBlocks() {
        return this.affectedBlocks;
    }

    public static void createExplosion(ServerWorld world, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, net.minecraft.world.explosion.Explosion.DestructionType destructionType) {
        Explosion explosion = new Explosion(world, entity, damageSource, behavior, x, y, z, power, createFire, destructionType);
        if (entity != null) {
            explosion.vec3d = entity.getVelocity().normalize();
        }
        explosion.collectBlocksAndDamageEntities();
        explosion.affectWorld(false);
        if (destructionType != net.minecraft.world.explosion.Explosion.DestructionType.KEEP) {
            for (ServerPlayerEntity serverPlayerEntity : world.getPlayers()) {
                if (!(serverPlayerEntity.squaredDistanceTo(x, y, z) < 80 * 80)) continue;

                PacketByteBuf packetByteBuf = PacketByteBufs.create();
                new CreepagerExplosionHandler(x, y, z, power, explosion.getAffectedBlocks(), explosion.getAffectedPlayers().get(serverPlayerEntity)).write(packetByteBuf);
                ServerPlayNetworking.send(serverPlayerEntity, IDENTIFIER, packetByteBuf);
            }
        }
    }

    public static class Explosion extends net.minecraft.world.explosion.Explosion {
        private static final ExplosionBehavior DEFAULT_BEHAVIOR = new ExplosionBehavior();
        private static final int field_30960 = 16;
        private final boolean createFire;
        private final DestructionType destructionType;
        private final Random random = Random.create();
        private final World world;
        private final double x;
        private final double y;
        private final double z;
        @Nullable
        private final Entity entity;
        private final float power;
        private final DamageSource damageSource;
        private final ExplosionBehavior behavior;
        private final ObjectArrayList<BlockPos> affectedBlocks = new ObjectArrayList();
        private final Map<PlayerEntity, Vec3d> affectedPlayers = Maps.newHashMap();
        private Vec3d vec3d;

        public Explosion(World world, @Nullable Entity entity, double x, double y, double z, float power) {
            this(world, entity, x, y, z, power, false, DestructionType.DESTROY);
        }

        public Explosion(World world, @Nullable Entity entity, double x, double y, double z, float power, List<BlockPos> affectedBlocks) {
            this(world, entity, x, y, z, power, false, DestructionType.DESTROY, affectedBlocks);
        }

        public Explosion(World world, @Nullable Entity entity, double x, double y, double z, float power, boolean createFire, DestructionType destructionType, List<BlockPos> affectedBlocks) {
            this(world, entity, x, y, z, power, createFire, destructionType);
            this.affectedBlocks.addAll(affectedBlocks);
        }

        public Explosion(World world, @Nullable Entity entity, double x, double y, double z, float power, boolean createFire, DestructionType destructionType) {
            this(world, entity, null, null, x, y, z, power, createFire, destructionType);
        }

        public Explosion(World world, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, DestructionType destructionType) {
            super(world, entity, null, null, x, y, z, power, createFire, destructionType);
            this.world = world;
            this.entity = entity;
            this.power = power;
            this.x = x;
            this.y = y;
            this.z = z;
            this.createFire = createFire;
            this.destructionType = destructionType;
            this.damageSource = damageSource == null ? world.getDamageSources().explosion(this) : damageSource;
            this.behavior = behavior == null ? this.chooseBehavior(entity) : behavior;
        }

        private ExplosionBehavior chooseBehavior(@Nullable Entity entity) {
            return entity == null ? DEFAULT_BEHAVIOR : new EntityExplosionBehavior(entity);
        }

        public static float getExposure(Vec3d source, Entity entity) {
            Box box = entity.getBoundingBox();
            double d = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
            double e = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
            double f = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
            double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
            double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
            if (d < 0.0 || e < 0.0 || f < 0.0) {
                return 0.0f;
            }
            int i = 0;
            int j = 0;
            for (double k = 0.0; k <= 1.0; k += d) {
                for (double l = 0.0; l <= 1.0; l += e) {
                    for (double m = 0.0; m <= 1.0; m += f) {
                        double p;
                        double o;
                        double n = MathHelper.lerp(k, box.minX, box.maxX);
                        Vec3d vec3d = new Vec3d(n + g, o = MathHelper.lerp(l, box.minY, box.maxY), (p = MathHelper.lerp(m, box.minZ, box.maxZ)) + h);
                        if (entity.getWorld().raycast(new RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType() == HitResult.Type.MISS) {
                            ++i;
                        }
                        ++j;
                    }
                }
            }
            return (float) i / (float) j;
        }

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
                        for (float h = this.power * (0.9f + this.world.random.nextFloat() * 0.9f); h > 0.0f; h -= 0.22500001f) {
                            BlockPos blockPos = new BlockPos((int) m, (int) n, (int) o);
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

            if (this.destructionType.equals(DestructionType.KEEP)) {
                return;
            }

            float q = this.power * 2.0f;
            k = MathHelper.floor(this.x - (double) q - 1.0);
            l = MathHelper.floor(this.x + (double) q + 1.0);
            int r = MathHelper.floor(this.y - (double) q - 1.0);
            int s = MathHelper.floor(this.y + (double) q + 1.0);
            int t = MathHelper.floor(this.z - (double) q - 1.0);
            int u = MathHelper.floor(this.z + (double) q + 1.0);
            List<Entity> list = this.world.getOtherEntities(this.entity, new Box(k, r, t, l, s, u));
            Vec3d vec3d = new Vec3d(this.x, this.y, this.z);
            for (int v = 0; v < list.size(); ++v) {
                PlayerEntity playerEntity;
                double z;
                double y;
                double x;
                double aa;
                double w;
                Entity entity = list.get(v);
                if (getCausingEntity() != null && getCausingEntity().isTeammate(entity)) {
                    continue;
                }

                if (this.entity instanceof PillagerProjectileEntity pillagerProjectileEntity && entity.equals(pillagerProjectileEntity.getOwner())) {
                    continue;
                }

                if (entity.isImmuneToExplosion() || !((w = Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double) q) <= 1.0) || (aa = Math.sqrt((x = entity.getX() - this.x) * x + (y = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y) * y + (z = entity.getZ() - this.z) * z)) == 0.0)
                    continue;
                x /= aa;
                y /= aa;
                z /= aa;
                double ab = net.minecraft.world.explosion.Explosion.getExposure(vec3d, entity);
                double ac = (1.0 - w) * ab;
                double ad = ac;
                if (entity instanceof LivingEntity) {
                    ad = ProtectionEnchantment.transformExplosionKnockback((LivingEntity) entity, ac);
                }

                entity.damage(this.getDamageSource(), (float)((int)((ac * ac + ac) / 2.0 * 7.0 * (double)q + 1.0)));
                entity.setVelocity(entity.getVelocity().add(x * ad, y * ac, z * ad));
                if (!(entity instanceof PlayerEntity) || (playerEntity = (PlayerEntity) entity).isSpectator() || playerEntity.isCreative() && playerEntity.getAbilities().flying)
                    continue;

                this.affectedPlayers.put(playerEntity, new Vec3d(x * ac, y * ac, z * ac));
            }
        }

        public void affectWorld(boolean particles) {
            boolean bl = this.shouldDestroy();
            if (this.world.isClient) {
                this.world.playSound(this.x, this.y, this.z, ParticleRegistry.VILLAGER_EXPLODE, SoundCategory.BLOCKS, 3.0f, (1.4f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2f) * 0.7f, false);
                this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0f, (1.4f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2f) * 0.7f, false);
            }

            if (particles) {
                for (int i = 0; i < this.power * 10; i++) {
                    double d = random.nextGaussian();
                    double e = random.nextGaussian();
                    double f = random.nextGaussian();

                    this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, EmeraldMoneyClient.getEmeraldColor(random)), true, this.x + d, this.y + e, this.z + f, d * 2, e * 2, f * 2);
                }

                if (this.power < 2.0f || !bl) {
                    this.world.addParticle(ParticleRegistry.EMERALD_EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);
                } else {
                    this.world.addParticle(ParticleRegistry.EMERALD_EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0, 0.0, 0.0);
                }
            }

            if (bl) {
                Vec3d pos = new Vec3d(x, y, z);
                ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList = new ObjectArrayList<>();
                boolean bl2 = this.getCausingEntity() instanceof PlayerEntity;
                Util.shuffle(this.affectedBlocks, this.world.random);
                HashMap<BlockPos, BlockState> blocks = new HashMap<>();
                for (BlockPos blockPos : this.affectedBlocks) {
                    World world;
                    BlockState blockState = this.world.getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    if (blockState.isAir()) continue;
                    BlockPos blockPos2 = blockPos.toImmutable();
                    this.world.getProfiler().push("explosion_blocks");
                    if (block.shouldDropItemsOnExplosion(this) && (world = this.world) instanceof ServerWorld serverWorld) {
                        if (world.getRandom().nextDouble() <= 0.3) {
                            blocks.put(blockPos, blockState);
                        } else {
                            BlockEntity blockEntity = blockState.hasBlockEntity() ? this.world.getBlockEntity(blockPos) : null;
                            LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(serverWorld).add(LootContextParameters.ORIGIN, Vec3d.ofCenter(blockPos)).add(LootContextParameters.TOOL, ItemStack.EMPTY).addOptional(LootContextParameters.BLOCK_ENTITY, blockEntity).addOptional(LootContextParameters.THIS_ENTITY, this.entity);
                            if (this.destructionType == DestructionType.DESTROY_WITH_DECAY) {
                                builder.add(LootContextParameters.EXPLOSION_RADIUS, Float.valueOf(this.power));
                            }
                            blockState.onStacksDropped(serverWorld, blockPos, ItemStack.EMPTY, bl2);
                            blockState.getDroppedStacks(builder).forEach(stack -> tryMergeStack(objectArrayList, stack, blockPos2));
                        }
                    }
                    this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                    block.onDestroyedByExplosion(this.world, blockPos, this);
                    this.world.getProfiler().pop();
                }

                blocks.forEach((blockPos, blockState) -> {
                    CustomBlockEntity customBlockEntity = new CustomBlockEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, this.getCausingEntity());
                    customBlockEntity.setPos(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
                    customBlockEntity.setItemStack(blockState, blockState.getBlock().asItem().getDefaultStack());
                    Vec3d vec3d = customBlockEntity.getPos().subtract(pos).normalize();
                    customBlockEntity.setVelocity(vec3d.getX() + world.getRandom().nextGaussian() * 0.2,
                            0.2f + world.getRandom().nextDouble() * 1.2, vec3d.getZ() + world.getRandom().nextGaussian() * 0.2);
                    customBlockEntity.velocityDirty = true;
                    customBlockEntity.setOwner(this.getCausingEntity());
                    customBlockEntity.explode = false;
                    customBlockEntity.drop = true;
                    world.spawnEntity(customBlockEntity);
                });

                int drops = world.getRandom().nextBetween(64, 128);

                for (int i = 0; i < drops && !this.affectedBlocks.isEmpty(); i++) {
                    BlockPos blockPos = this.affectedBlocks.get(world.getRandom().nextInt(this.affectedBlocks.size()));

                    Vec3d position = Vec3d.ofCenter(blockPos);
                    Vec3d velocity = position.subtract(new Vec3d(x + 0.5, y + 0.5, z + 0.5)).normalize().multiply(0.35);
                    ItemEntity itemEntity = new ItemEntity(world, position.getX(), position.getY(), position.getZ(), Items.EMERALD.getDefaultStack(), velocity.getX(), velocity.getY(), velocity.getZ());
                    world.spawnEntity(itemEntity);
                }

                for (Pair<ItemStack, BlockPos> pair : objectArrayList) {
                    Block.dropStack(this.world, pair.getSecond(), pair.getFirst());
                }
            }
        }

        private static void tryMergeStack(ObjectArrayList<Pair<ItemStack, BlockPos>> stacks, ItemStack stack, BlockPos pos) {
            int i = stacks.size();
            for (int j = 0; j < i; ++j) {
                Pair<ItemStack, BlockPos> pair = stacks.get(j);
                ItemStack itemStack = pair.getFirst();
                if (!ItemEntity.canMerge(itemStack, stack)) continue;
                ItemStack itemStack2 = ItemEntity.merge(itemStack, stack, 16);
                stacks.set(j, Pair.of(itemStack2, pair.getSecond()));
                if (!stack.isEmpty()) continue;
                return;
            }
            stacks.add(Pair.of(stack, pos));
        }

        public DamageSource getDamageSource() {
            return this.damageSource;
        }

        public Map<PlayerEntity, Vec3d> getAffectedPlayers() {
            return this.affectedPlayers;
        }

        @Nullable
        public LivingEntity getCausingEntity() {
            Entity entity;
            if (this.entity == null) {
                return null;
            }
            if (this.entity instanceof TntEntity) {
                return ((TntEntity) this.entity).getOwner();
            }
            if (this.entity instanceof LivingEntity) {
                return (LivingEntity) this.entity;
            }
            if (this.entity instanceof ProjectileEntity && (entity = ((ProjectileEntity) this.entity).getOwner()) instanceof LivingEntity) {
                return (LivingEntity) entity;
            }
            return null;
        }

        public void clearAffectedBlocks() {
            this.affectedBlocks.clear();
        }

        public List<BlockPos> getAffectedBlocks() {
            return this.affectedBlocks;
        }
    }
}

