package de.takacick.onedeathblock;

import de.takacick.onedeathblock.access.PlayerProperties;
import de.takacick.onedeathblock.registry.EntityRegistry;
import de.takacick.onedeathblock.registry.ItemRegistry;
import de.takacick.onedeathblock.registry.ParticleRegistry;
import de.takacick.onedeathblock.registry.entity.living.SuperbrineEntity;
import de.takacick.onedeathblock.server.oneblock.OneBlockHandler;
import de.takacick.utils.BionicUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OneDeathBlock implements ModInitializer {

    public static final String MOD_ID = "onedeathblock";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "onedeathblock"), () -> new ItemStack(ItemRegistry.DEATH_BLOCK));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "onedeathblock");

    @Override
    public void onInitialize() {
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        new OneBlockHandler();

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ItemStack itemStack = player.getStackInHand(hand);

            if (!world.isClient) {
                if (itemStack.isOf(Items.IRON_INGOT)) {
                    BlockState blockState = world.getBlockState(hitResult.getBlockPos());

                    if (blockState.isOf(Blocks.CACTUS)) {

                        if (itemStack.getCount() == 1) {
                            player.setStackInHand(hand, ItemRegistry.SPIKY_IRON.getDefaultStack());
                        } else {
                            itemStack.decrement(1);
                            player.getInventory().offerOrDrop(ItemRegistry.SPIKY_IRON.getDefaultStack());
                        }

                        world.playSound(null, hitResult.getBlockPos(), SoundEvents.ENTITY_BEE_STING, SoundCategory.BLOCKS, 1f, 1.6f);
                        player.damage(DamageSource.CACTUS, 1f);

                        return ActionResult.SUCCESS;
                    }
                }
            }

            return ActionResult.PASS;
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "explosiveplacing"), (server, player, handler, buf, responseSender) -> {
            if (player instanceof PlayerProperties playerProperties) {
                server.execute(() -> {
                    playerProperties.setExplosivePlacing(!playerProperties.hasExplosivePlacing());
                    ServerWorld world = player.getWorld();

                    BionicUtils.sendEntityStatus(world, player, OneDeathBlock.IDENTIFIER, 5);

                    if (playerProperties.hasExplosivePlacing()) {
                        world.playSoundFromEntity(null, player, SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.BLOCKS, 1f, 1.2f);
                        world.playSoundFromEntity(null, player, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 0.25f, 1.6f);
                    } else {
                        world.playSoundFromEntity(null, player, SoundEvents.ENTITY_CREEPER_DEATH, SoundCategory.BLOCKS, 1f, 1.2f);
                        world.playSoundFromEntity(null, player, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 0.25f, 1.6f);
                    }
                });
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "superbrinesummon"), (server, player, handler, buf, responseSender) -> {
            if (player.hasPermissionLevel(2)) {
                server.execute(() -> {
                    World world = player.getWorld();
                    HitResult hitResult = player.raycast(150, 0, true);
                    if (!hitResult.getType().equals(HitResult.Type.MISS)) {
                        BlockPos blockPos = new BlockPos(hitResult.getPos().getX(), hitResult.getPos().getY(), hitResult.getPos().getZ());
                        if (!world.isClient) {
                            SuperbrineEntity superbrineEntity = new SuperbrineEntity(EntityRegistry.SUPERBRINE, world);
                            superbrineEntity.setPos(blockPos.getX() + 0.5, blockPos.getY() + 0.001, blockPos.getZ());
                            superbrineEntity.refreshPositionAndAngles(superbrineEntity.getX(), superbrineEntity.getY(), superbrineEntity.getZ(), player.getYaw() - 180, 0);
                            superbrineEntity.setHeadYaw(superbrineEntity.getYaw());
                            superbrineEntity.setBodyYaw(superbrineEntity.getYaw());
                            superbrineEntity.playSpawnEffects();
                            world.spawnEntity(superbrineEntity);

                            world.playSoundFromEntity(null, superbrineEntity, SoundEvents.ITEM_TRIDENT_THUNDER, SoundCategory.AMBIENT, 3f, 1f);

                            for (int i = 0; i < 5; i++) {
                                LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                                lightningEntity.setPos(superbrineEntity.getX() + world.getRandom().nextGaussian() * 1, superbrineEntity.getY() + world.getRandom().nextGaussian() * 1, superbrineEntity.getZ() + world.getRandom().nextGaussian() * 1);
                                world.spawnEntity(lightningEntity);
                            }
                        }
                    }
                });
            }
        });
    }

    public static void updateEntityHealth(LivingEntity livingEntity, double maxHealth, boolean heal) {
        livingEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        livingEntity.setHealth(heal ? livingEntity.getMaxHealth() : Math.min(livingEntity.getMaxHealth(), livingEntity.getHealth()));

        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeDouble(maxHealth);
        packetByteBuf.writeInt((int) Math.ceil(maxHealth / 2d));
        packetByteBuf.writeFloat(livingEntity.getHealth());

        if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(OneDeathBlock.MOD_ID, "healthupdate"), packetByteBuf);
        }
    }
}
