package de.takacick.upgradebody.registry.bodypart.upgrades;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.registry.ParticleRegistry;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class KillerDriller extends BodyPart {

    public KillerDriller() {
        super("Killer Driller", new Identifier(UpgradeBody.MOD_ID, "killer_driller"));
    }

    @Override
    public double getHeight() {
        return 0.5 * 0.9375f;
    }

    @Override
    public double getWidth() {
        return 0.5;
    }

    @Override
    public int getHeightIndex() {
        return 0;
    }

    @Override
    public boolean affectModelOrdering() {
        return true;
    }

    @Override
    public String getInheritModelPart() {
        return "head";
    }

    @Override
    public float getHealth() {
        return 6f;
    }

    @Override
    public void onEquip(PlayerEntity playerEntity) {
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, -1, 0, false, false, true));
        super.onEquip(playerEntity);
    }

    @Override
    public void tick(PlayerEntity playerEntity) {
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, -1, 0, false, false, true));
        super.tick(playerEntity);
    }

    @Override
    public void onDequip(PlayerEntity playerEntity) {
        playerEntity.removeStatusEffect(StatusEffects.HASTE);
        super.onDequip(playerEntity);
    }

    public static boolean killerDrilling(World world, PlayerEntity user) {
        if (destroyBlocks(user.getBoundingBox(), world, user)) {

            if (world.isClient) {
                float tridentEntity = user.getYaw();
                float f = user.getPitch();
                float g = -MathHelper.sin(tridentEntity * 0.017453292F) * MathHelper.cos(f * 0.017453292F);
                float h = -MathHelper.sin(f * 0.017453292F);
                float k2 = MathHelper.cos(tridentEntity * 0.017453292F) * MathHelper.cos(f * 0.017453292F);
                float l2 = MathHelper.sqrt(g * g + h * h + k2 * k2);
                float m2 = 1.0F;
                user.setVelocity((g * (m2 / l2)) * 1.3, (h * (m2 / l2)) * 1.3, (k2 * (m2 / l2)) * 1.3);
            }
            user.useRiptide(10);
            user.noClip = true;
            boolean flag1 = BlockPos.findClosest(user.getBlockPos(), 5, 5, (block) -> {
                return world.getBlockState(block).getBlock().getHardness() > 50.0F;
            }).isPresent();
            boolean flag2 = BlockPos.findClosest(user.getBlockPos(), 5, 5, (block) -> {
                return world.getBlockState(block).getBlock().getHardness() < 0.0F;
            }).isPresent();
            boolean flag3;
            if (flag1) {
                flag3 = world.getBlockState(BlockPos.findClosest(user.getBlockPos(), 5, 5, (block) -> {
                    return world.getBlockState(block).getBlock().getHardness() > 50.0F;
                }).get()).blocksMovement();
                if (flag3) {
                    user.noClip = false;
                    return true;
                }
            }

            if (flag2) {
                flag3 = world.getBlockState(BlockPos.findClosest(user.getBlockPos(), 5, 5, (block) -> {
                    return world.getBlockState(block).getBlock().getHardness() < 0.0F;
                }).get()).blocksMovement();
                if (flag3) {
                    user.noClip = false;
                }
            }
            return true;
        }

        return false;
    }

    public static boolean destroyBlocks(Box box, World world, LivingEntity entity) {
        box = box.offset(entity.getRotationVector());
        int i = MathHelper.floor(box.minX - 0.75D);
        int j = MathHelper.floor(box.minY - 0.75D);
        int k = MathHelper.floor(box.minZ - 0.75D);
        int l = MathHelper.floor(box.maxX + 0.75D);
        int m = MathHelper.floor(box.maxY + 0.75D);
        int n = MathHelper.floor(box.maxZ + 0.75D);
        Random rand = new Random();
        boolean bl2 = false;

        int level = 0;

        for (int o = i; o <= l; ++o) {
            for (int p = j; p <= m; ++p) {
                for (int q = k; q <= n; ++q) {
                    BlockPos blockPos = new BlockPos(o, p, q);
                    BlockState blockState = world.getBlockState(blockPos);
                    if (blockState.blocksMovement()) {
                        if ((!(blockState.getBlock().getHardness() > 50.0F) && !(blockState.getBlock().getHardness() < 0.0F)) && !(blockState.getBlock() instanceof FluidBlock)) {
                            bl2 = true;
                            level++;
                            if (!world.isClient()) {


                                world.breakBlock(blockPos, false, entity);
                            } else {
                                if (world.getRandom().nextDouble() <= 0.1) {
                                    for (int b = 0; b < 3; ++b) {
                                        double x = blockPos.getX() + 0.5;
                                        double y = blockPos.getY() + 0.5;
                                        double z = blockPos.getZ() + 0.5;

                                        double d = world.getRandom().nextGaussian() * 0.4;
                                        double e = world.getRandom().nextGaussian() * 0.4;
                                        double f = world.getRandom().nextGaussian() * 0.4;

                                        world.addParticle(ParticleRegistry.XP_TOTEM,
                                                true, x + d, y + e, z + f, d * 0.25, e * 0.25, f * 0.25);
                                    }

                                    world.playSound(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1f, 1f, true);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
            serverPlayerEntity.addExperienceLevels(level);
        }
        if (bl2) {
            BlockPos o = new BlockPos(i + rand.nextInt(l - i + 1), j + rand.nextInt(m - j + 1), k + rand.nextInt(n - k + 1));

            world.addParticle(ParticleRegistry.XP_EXPLOSION, (double) o.getX() + 0.5D, (double) o.getY() + 2.0D, (double) o.getZ() + 0.5D,
                    0, 0, 0);
        }

        return bl2;
    }
}
