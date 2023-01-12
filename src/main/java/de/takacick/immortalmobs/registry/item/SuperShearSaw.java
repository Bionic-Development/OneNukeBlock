package de.takacick.immortalmobs.registry.item;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.access.PlayerProperties;
import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.entity.custom.ImmortalItemEntity;
import de.takacick.immortalmobs.registry.entity.living.ImmortalSheepEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class SuperShearSaw extends Item {

    public SuperShearSaw(Settings settings) {
        super(settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            Vec3d vec3d = user.getPos().add(0, user.getHeight() / 2, 0);

            if (remainingUseTicks % 1 == 0) {
                world.playSound(null, new BlockPos(user.getX(), user.getY(), user.getZ()), SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }

            AtomicBoolean foundSheep = new AtomicBoolean();

            world.getOtherEntities(user, new Box(vec3d, vec3d.add(user.getRotationVector().multiply(2.5)))).forEach(entity -> {
                if (entity instanceof ImmortalSheepEntity immortalSheepEntity) {
                    BionicUtils.sendEntityStatus((ServerWorld) immortalSheepEntity.getWorld(), immortalSheepEntity, ImmortalMobs.IDENTIFIER, 2);
                    foundSheep.set(true);

                    if (getMaxUseTime(stack) - remainingUseTicks > 20) {
                        BionicUtils.sendEntityStatus((ServerWorld) immortalSheepEntity.getWorld(), immortalSheepEntity, ImmortalMobs.IDENTIFIER, 6);
                        SheepEntity sheepEntity = new SheepEntity(EntityType.SHEEP, world);
                        UUID uuid = sheepEntity.getUuid();
                        Integer id = sheepEntity.getId();
                        sheepEntity.copyFrom(immortalSheepEntity);
                        sheepEntity.setUuid(uuid);
                        sheepEntity.setId(id);
                        sheepEntity.setColor(SheepEntity.generateDefaultColor(world.getRandom()));
                        world.spawnEntity(sheepEntity);

                        for (int i = 0; i < 2; i++) {
                            ImmortalItemEntity itemEntity = new ImmortalItemEntity(world, entity.getX(), entity.getY(), entity.getZ(),
                                    ItemRegistry.IMMORTAL_WOOL_ITEM.getDefaultStack(),
                                    world.getRandom().nextGaussian() * 0.1,
                                    0.6, world.getRandom().nextGaussian() * 0.1);
                            itemEntity.setGlowing(true);
                            itemEntity.setToDefaultPickupDelay();
                            world.spawnEntity(itemEntity);
                            world.playSoundFromEntity(null, itemEntity, SoundEvents.ENCHANT_THORNS_HIT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        }
                        immortalSheepEntity.discard();
                    }
                }
            });

            if (user instanceof PlayerProperties playerProperties && !foundSheep.get()) {
                playerProperties.setItemUseTime(0);
            }
        }
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        BionicUtils.sendEntityStatus((ServerWorld) target.getWorld(), target, ImmortalMobs.IDENTIFIER, 2);

        return super.postHit(stack, target, attacker);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }
}
