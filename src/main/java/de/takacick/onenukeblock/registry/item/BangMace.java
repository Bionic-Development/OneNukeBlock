package de.takacick.onenukeblock.registry.item;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.utils.data.AttachmentTypes;
import de.takacick.onenukeblock.utils.data.ItemDataComponents;
import de.takacick.onenukeblock.utils.data.item.BangMaceItemHelper;
import de.takacick.onenukeblock.utils.explosion.BangMaceExplosion;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class BangMace extends MaceItem implements HandheldItem {

    public BangMace(Settings settings) {
        super(settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            user.getAttachedOrCreate(AttachmentTypes.BANG_MACE);
        }

        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);

        if (!world.isClient) {
            user.getAttachedOrCreate(AttachmentTypes.BANG_MACE);
        }
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public ItemStack getDefaultStack() {
        return super.getDefaultStack();
    }

    @Override
    public void tickInventory(LivingEntity entity, ItemStack itemStack) {
        BangMaceItemHelper itemAnimationHelper = itemStack.getOrDefault(ItemDataComponents.BANG_MACE, new BangMaceItemHelper());

        if (itemStack.get(ItemDataComponents.BANG_MACE) == null) {
            itemStack.set(ItemDataComponents.BANG_MACE, itemAnimationHelper);
        }

        if (itemAnimationHelper != null) {
            itemAnimationHelper.tick(entity, itemStack);
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        ServerPlayerEntity serverPlayerEntity;
        if (attacker instanceof ServerPlayerEntity && MaceItem.shouldDealAdditionalDamage(serverPlayerEntity = (ServerPlayerEntity) attacker)) {
            ServerWorld serverWorld = (ServerWorld) attacker.getWorld();
            if (serverPlayerEntity.shouldIgnoreFallDamageFromCurrentExplosion() && serverPlayerEntity.currentExplosionImpactPos != null) {
                if (serverPlayerEntity.currentExplosionImpactPos.y > serverPlayerEntity.getPos().y) {
                    serverPlayerEntity.currentExplosionImpactPos = serverPlayerEntity.getPos();
                }
            } else {
                serverPlayerEntity.currentExplosionImpactPos = serverPlayerEntity.getPos();
            }
            serverPlayerEntity.setIgnoreFallDamageFromCurrentExplosion(true);
            serverPlayerEntity.setVelocity(serverPlayerEntity.getVelocity().withAxis(Direction.Axis.Y, 0.01f));
            serverPlayerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayerEntity));
            if (target.isOnGround()) {
                serverPlayerEntity.setSpawnExtraParticlesOnFall(true);
                SoundEvent soundEvent = serverPlayerEntity.fallDistance > 5.0f ? SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY : SoundEvents.ITEM_MACE_SMASH_GROUND;
                serverWorld.playSound(null, serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), soundEvent, serverPlayerEntity.getSoundCategory(), 1.0f, 1.0f);
            } else {
                serverWorld.playSound(null, serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), SoundEvents.ITEM_MACE_SMASH_AIR, serverPlayerEntity.getSoundCategory(), 1.0f, 1.0f);
            }
            serverPlayerEntity.currentExplosionImpactPos = target.getPos().add(0, -2, 0);
            knockbackNearbyEntities(serverWorld, serverPlayerEntity, target);
        }
        return true;
    }

    public static void knockbackNearbyEntities(World world, PlayerEntity player, Entity attacked) {
        EventHandler.sendWorldStatus(world, attacked.getBlockPos().toCenterPos(), OneNukeBlock.IDENTIFIER, 1, 1000);

        BangMaceExplosion.createExplosion((ServerWorld) world, player, null, null,
                attacked.getX(), attacked.getBodyY(0.1), attacked.getZ(),
                6f, false, true,
                ParticleTypes.EXPLOSION_EMITTER, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE);

        BlockPos center = attacked.getSteppingPos();
        Vec3d centerPos = Vec3d.ofCenter(center);

        for (int i = 4; i < 14; i++) {
            float velocity = ((float) (i - 6) * 0.05f) + 0.20f;
            int finalW = i;
            OneNukeBlock.generateCircle(center, i, true).forEach(blockPos -> {
                if (finalW <= 12 || world.getRandom().nextDouble() <= Math.max(1f - (finalW - 6f) / 9f, 0.2)) {
                    for (int y = -2; y <= 2; y++) {
                        BlockState blockState = world.getBlockState(blockPos.add(0, y, 0));

                        if (!blockState.isAir() && !world.getBlockState(blockPos.add(0, y, 0).up()).isSolid()) {
                            Vec3d vec3d = centerPos.subtract(blockPos.add(0, y, 0).toCenterPos()).multiply(1, 0, 1);

                            FallingBlockEntity fallingBlockEntity = spawnFromBlock(world, blockPos.add(0, y, 0), blockState);
                            fallingBlockEntity.setVelocity(vec3d.getX() * 0, velocity, vec3d.getZ() * 0);
                            world.spawnEntity(fallingBlockEntity);

                            world.getOtherEntities(attacked, new Box(blockPos.add(0, 2, 0)).expand(0, 2, 0)).forEach(entity -> {
                                if (!(entity instanceof FallingBlockEntity) && entity.getVelocity().getY() <= 0.5) {
                                    entity.addVelocity(0, velocity * 2, 0);

                                    if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                                        serverPlayerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayerEntity));
                                    }
                                }
                            });
                            break;
                        }
                    }
                }
            });
        }

        world.getEntitiesByClass(LivingEntity.class, attacked.getBoundingBox().expand(10), getKnockbackPredicate(player, attacked)).forEach(entity -> {
            if (!entity.equals(attacked)) {
                Vec3d vec3d = entity.getPos().subtract(attacked.getPos()).multiply(0.5);
                double d = getKnockback(player, entity, vec3d);
                Vec3d vec3d2 = vec3d.normalize().multiply(Math.min(d, 1.5));
                entity.addVelocity(vec3d2.x, 1.9, vec3d2.z);

                entity.damage(world.getDamageSources().generic(), 7 * (float) Math.min(d, 0.5));

                if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                    serverPlayerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayerEntity));
                }
            }
        });
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        tooltip.add(Text.of("§7Hits hard, §cexplodes harder§7."));

        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public boolean showRightArm(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return livingEntity.isUsingItem() && livingEntity.getActiveItem().getItem().equals(stack.getItem());
    }

    @Override
    public boolean showLeftArm(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return livingEntity.isUsingItem() && livingEntity.getActiveItem().equals(stack);
    }

    @Override
    public boolean allowHandTransformation(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return livingEntity.isUsingItem() && livingEntity.getActiveItem().equals(stack);
    }

    @Override
    public boolean shouldRender(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return livingEntity.isUsingItem() && livingEntity.getActiveItem().equals(stack);
    }

    @Override
    public boolean keepFirstPersonPitch(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return livingEntity.isUsingItem() && livingEntity.getActiveItem().equals(stack);
    }

    @Override
    public boolean allowVanillaUsageAnimation() {
        return false;
    }

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 6.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, -3.4f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
    }

    public static FallingBlockEntity spawnFromBlock(World world, BlockPos pos, BlockState state) {
        FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, (double) pos.getX() + 0.5, pos.getY() + 0.9, (double) pos.getZ() + 0.5, state.contains(Properties.WATERLOGGED) ? (BlockState) state.with(Properties.WATERLOGGED, false) : state);
        world.setBlockState(pos, state.getFluidState().getBlockState(), Block.NOTIFY_ALL);
        return fallingBlockEntity;
    }
}
