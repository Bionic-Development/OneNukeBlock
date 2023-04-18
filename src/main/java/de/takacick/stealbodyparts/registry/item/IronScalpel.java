package de.takacick.stealbodyparts.registry.item;

import com.mojang.authlib.GameProfile;
import de.takacick.stealbodyparts.access.PlayerProperties;
import de.takacick.stealbodyparts.registry.ItemRegistry;
import de.takacick.stealbodyparts.utils.BodyPart;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IronScalpel extends Item {

    public IronScalpel(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {

        World world = user.getWorld();

        if (entity instanceof PlayerEntity playerEntity && playerEntity instanceof PlayerProperties playerProperties) {
            BodyPart bodyPart = playerProperties.getNextBodyPart();

            if (bodyPart != null) {
                playerProperties.setNextBodyPart(null);

                ItemStack bodyStack = (switch (bodyPart) {
                    case HEAD -> new ItemStack(ItemRegistry.HEAD);
                    case LEFT_ARM -> new ItemStack(ItemRegistry.LEFT_ARM);
                    case LEFT_LEG -> new ItemStack(ItemRegistry.LEFT_LEG);
                    case RIGHT_ARM -> new ItemStack(ItemRegistry.RIGHT_ARM);
                    case RIGHT_LEG -> new ItemStack(ItemRegistry.RIGHT_LEG);
                });

                GameProfile gameProfile = playerEntity.getGameProfile();
                bodyStack.getOrCreateNbt().put("PartOwner", NbtHelper.writeGameProfile(new NbtCompound(), gameProfile));

                Vec3d vec3d = user.getPos().subtract(playerEntity.getPos()).normalize().multiply(0.45);

                double y = (switch (bodyPart) {
                    case HEAD -> playerEntity.getEyeY();
                    case LEFT_ARM, RIGHT_ARM -> playerEntity.getEyeY() - playerEntity.getStandingEyeHeight() * 0.25;
                    case LEFT_LEG, RIGHT_LEG -> playerEntity.getEyeY() - playerEntity.getStandingEyeHeight() * (1 - 0.375);
                });

                ItemEntity itemEntity = new ItemEntity(world, playerEntity.getX(), y, playerEntity.getZ(), bodyStack, vec3d.getX(), 0.1d, vec3d.getZ());
                itemEntity.setPickupDelay(10);
                world.spawnEntity(itemEntity);

                playerProperties.setBodyPart(bodyPart.getIndex(), false);
                ((ServerWorld) world).spawnParticles(ParticleTypes.SWEEP_ATTACK, playerEntity.getX(), y, playerEntity.getZ(), 1, 0, 0, 0, 0);
                world.playSound(null, playerEntity.getX(), y, playerEntity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, playerEntity.getSoundCategory(), 1f, 1f);

                user.swingHand(hand, true);
                return ActionResult.success(true);
            }
        }

        return super.useOnEntity(stack, user, entity, hand);
    }
}
