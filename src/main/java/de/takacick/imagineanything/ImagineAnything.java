package de.takacick.imagineanything;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.imagineanything.access.LivingProperties;
import de.takacick.imagineanything.access.PlayerProperties;
import de.takacick.imagineanything.network.screenhandler.ImagineScreenHandler;
import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.imagineanything.registry.ParticleRegistry;
import de.takacick.imagineanything.registry.entity.custom.HologramItemEntity;
import de.takacick.imagineanything.registry.entity.projectiles.IronManLaserBulletEntity;
import de.takacick.utils.BionicUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class ImagineAnything implements ModInitializer {

    public static final String MOD_ID = "imagineanything";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "imagineanything"), () -> new ItemStack(ItemRegistry.HEAD));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "imagineanything");

    private static final Queue<ItemStack> imagineQueue = new LinkedList<>();

    @Override
    public void onInitialize() {
        ItemRegistry.register();
        EntityRegistry.register();
        ParticleRegistry.register();

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, registrationEnvironment) -> {
            LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("imaginequeue").requires(source -> source.hasPermissionLevel(2)).executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                if (playerEntity == null) {
                    return 0;
                }

                SimpleInventory simpleInventory = new SimpleInventory(54);
                AtomicInteger slot = new AtomicInteger();

                imagineQueue.forEach(stack -> {
                    if (slot.get() >= 54) {
                        return;
                    }

                    simpleInventory.setStack(slot.getAndIncrement(), stack);
                });

                playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> ImagineScreenHandler.createGeneric9x6(syncId, inventory, simpleInventory), Text.of("Imagine Queue: ")));
                return 1;
            }).build();
            dispatcher.getRoot().addChild(rootNode);

            rootNode = CommandManager.literal("holoitem")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(context -> {
                        if (context.getSource().getPlayer() == null) {
                            return 0;
                        }

                        PlayerEntity playerEntity = context.getSource().getPlayer();
                        if (!playerEntity.getMainHandStack().isEmpty()) {
                            HologramItemEntity hologramItemEntity = new HologramItemEntity(playerEntity.world, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), playerEntity.getMainHandStack());
                            playerEntity.getEntityWorld().spawnEntity(hologramItemEntity);

                            context.getSource().sendFeedback(Text.of("Summoned Holoitem!"), false);
                        } else {
                            context.getSource().sendFeedback(Text.of("§cPlease hold an item in your hand!"), false);
                        }
                        return 1;
                    }).build();
            dispatcher.getRoot().addChild(rootNode);
        }));

        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            server.getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                Entity entity = serverPlayerEntity.getEntityWorld().getEntityById(((PlayerProperties) serverPlayerEntity).getHolding());
                if (entity != null && entity.isAlive()) {
                    entity.fallDistance = 0;
                    entity.setVelocity(0, 0, 0);
                    Vec3d vec3d = getRotationVector(serverPlayerEntity.getPitch(), serverPlayerEntity.getYaw()).multiply(((PlayerProperties) serverPlayerEntity).getDistance());
                    entity.teleport(serverPlayerEntity.getX() + vec3d.getX(),
                            serverPlayerEntity.getBodyY(0.55) + vec3d.getY(), serverPlayerEntity.getZ() + vec3d.getZ());
                } else {
                    ((PlayerProperties) serverPlayerEntity).setHolding(null);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "headremoval"), (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (!player.getMainHandStack().isEmpty() || ((PlayerProperties) player).removedHead()) {
                    return;
                }
                GameProfile gameProfile = player.getGameProfile();

                ItemStack head = ItemRegistry.HEAD.getDefaultStack();

                ((PlayerProperties) player).setRemovedHead(true);

                head.getOrCreateNbt().put("SkullOwner", NbtHelper.writeGameProfile(new NbtCompound(), gameProfile));
                player.equipStack(EquipmentSlot.MAINHAND, head);
                BionicUtils.sendEntityStatus(player.getWorld(), player, IDENTIFIER, 1);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "ironmanlaser"), (server, player, handler, buf, responseSender) -> {
            if (player.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
                int ticks = buf.readInt();
                server.execute(() -> {
                    if (ticks > 0) {
                        ((PlayerProperties) player).setIronManLaser(true);
                        ((PlayerProperties) player).setIronManAbility("Iron Man Laser");
                    } else {
                        ((PlayerProperties) player).setIronManLaser(false);
                    }
                });
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "ironmanlaserbullet"), (server, player, handler, buf, responseSender) -> {
            if (player.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
                server.execute(() -> {
                    World world = player.getWorld();
                    IronManLaserBulletEntity ironManLaserBulletEntity = new IronManLaserBulletEntity(world, player);
                    ironManLaserBulletEntity.setPosition(player.getX(), player.getEyeY(), player.getZ());
                    ironManLaserBulletEntity.setProperties(player, player.getPitch(), player.getYaw(), 0.0F, 3f, 1.0F);
                    world.spawnEntity(ironManLaserBulletEntity);
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.NEUTRAL, 1f, 3f);

                    ((PlayerProperties) player).setIronManAbility("Iron Man Laser Bullet");

                });
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "ironmanforcefield"), (server, player, handler, buf, responseSender) -> {
            if (player.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
                server.execute(() -> {
                    ((PlayerProperties) player).setIronManForcefield(!((PlayerProperties) player).hasIronManForcefield());
                    if (((PlayerProperties) player).hasIronManForcefield()) {
                        BionicUtils.sendEntityStatus(player.getWorld(), player, IDENTIFIER, 13);
                        ((PlayerProperties) player).setIronManAbility("Iron Man Forcefield");
                    } else {
                        BionicUtils.sendEntityStatus(player.getWorld(), player, IDENTIFIER, 14);
                    }
                });
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "telekinesisflight"), (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ((PlayerProperties) player).setTelekinesisFlight(!((PlayerProperties) player).hasTelekinesisFlight());

                player.getAbilities().flying = ((PlayerProperties) player).hasTelekinesisFlight();
                player.getAbilities().allowFlying = ((PlayerProperties) player).hasTelekinesisFlight();
                player.sendAbilitiesUpdate();

                BionicUtils.sendEntityStatus(player.getWorld(), player, ImagineAnything.IDENTIFIER, 6);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "telekinesiscontrol"), (server, player, handler, buf, responseSender) -> {
            int id = buf.readInt();

            server.execute(() -> {

                ServerWorld world = player.getWorld();

                if (id == -2) {
                    Entity entity = world.getEntityById(((PlayerProperties) player).getHolding());
                    ((PlayerProperties) player).setHolding(null);
                    if (entity instanceof LivingEntity livingEntity) {
                        ((LivingProperties) livingEntity).setHolder(null);
                        ((LivingProperties) livingEntity).setThrown();
                        livingEntity.fallDistance = 4;
                        livingEntity.setVelocity(player.getRotationVector().multiply(2.3));
                        livingEntity.velocityModified = true;
                        livingEntity.velocityDirty = true;
                        BionicUtils.sendEntityStatus(player.getWorld(), livingEntity, ImagineAnything.IDENTIFIER, 8);
                    }
                } else {
                    Entity entity = world.getEntityById(id);

                    if (entity instanceof LivingEntity livingEntity) {
                        ((PlayerProperties) player).setHolding(livingEntity);
                        ((LivingProperties) livingEntity).setHolder(player);

                        BionicUtils.sendEntityStatus(player.getWorld(), livingEntity, ImagineAnything.IDENTIFIER, 7);
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "finishedthought"), (server, player, handler, buf, responseSender) -> {

            server.execute(() -> {
                ItemStack itemStack = player.getMainHandStack().isOf(ItemRegistry.THOUGHT) ? player.getMainHandStack() : player.getOffHandStack();
                if (itemStack.isOf(ItemRegistry.THOUGHT)) {
                    itemStack.getOrCreateNbt().putBoolean("enchanted", true);
                }
            });
        });
    }

    public static List<BlockPos> generateSphere(BlockPos centerBlock, int radius, boolean hollow) {

        List<BlockPos> circleBlockRegistry = new ArrayList<BlockPos>();

        int bx = centerBlock.getX();
        int by = centerBlock.getY();
        int bz = centerBlock.getZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        circleBlockRegistry.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return circleBlockRegistry;
    }

    public static Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float) Math.PI / 180);
        float g = -yaw * ((float) Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }

    public static Queue<ItemStack> getImagineQueue() {
        return imagineQueue;
    }
}
