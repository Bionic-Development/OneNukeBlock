package de.takacick.deathmoney.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.deathmoney.access.EntityProperties;
import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.entity.custom.DeathShopPortalEntity;
import de.takacick.deathmoney.registry.entity.custom.ShopItemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class DeathShopCommand {

    public static DeathShopPortalEntity DEATH_SHOP_PORTAL;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("deathshop").requires((source) -> {
                    return source.hasPermissionLevel(2);
                }).then(CommandManager.literal("set")
                        .executes(context -> {
                            if (context.getSource().getPlayer() == null) {
                                return 0;
                            }

                            PlayerEntity playerEntity = context.getSource().getPlayer();
                            if (playerEntity == null) {
                                return 0;
                            }

                            ((EntityProperties) playerEntity).setDeathShopPortalCooldown(10);

                            if (DEATH_SHOP_PORTAL != null) {
                                DEATH_SHOP_PORTAL.discard();
                            }

                            DeathShopPortalEntity deathShopPortalEntity = new DeathShopPortalEntity(EntityRegistry.DEATH_SHOP_PORTAL, playerEntity.getWorld());
                            deathShopPortalEntity.setPos(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ());
                            deathShopPortalEntity.setYaw(-playerEntity.getYaw());
                            deathShopPortalEntity.setShop(true);
                            playerEntity.getWorld().spawnEntity(deathShopPortalEntity);

                            DEATH_SHOP_PORTAL = deathShopPortalEntity;
                            context.getSource().sendFeedback(Text.of("Set Death Shop Portal to your current location!"), false);

                            return 1;
                        })
                ).then(CommandManager.literal("sell")
                        .then(CommandManager.argument("price", IntegerArgumentType.integer(0))
                                .executes((context) -> {
                                    int price = IntegerArgumentType.getInteger(context, "price");
                                    PlayerEntity playerEntity = context.getSource().getPlayer();
                                    if (playerEntity == null) {
                                        return 0;
                                    }

                                    if (playerEntity.getMainHandStack().isEmpty()) {
                                        context.getSource().sendFeedback(Text.of("§cPlease hold an item in your hand!"), false);
                                        return 0;
                                    }

                                    ShopItemEntity shopItemEntity = new ShopItemEntity(playerEntity.world, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), playerEntity.getMainHandStack().copy());
                                    shopItemEntity.setPrice(price);
                                    playerEntity.getEntityWorld().spawnEntity(shopItemEntity);

                                    context.getSource().sendFeedback(Text.of("Summoned new Shop Item!"), false);

                                    return 1;
                                })))
                .then(CommandManager.literal("nameoffset")
                        .then(CommandManager.argument("y", FloatArgumentType.floatArg())
                                .executes((context) -> {
                                    float y = FloatArgumentType.getFloat(context, "y");
                                    PlayerEntity playerEntity = context.getSource().getPlayer();
                                    if (playerEntity == null) {
                                        return 0;
                                    }

                                    Vec3d vec3d = playerEntity.getCameraPosVec(1.0f);
                                    Vec3d vec3d2 = playerEntity.getRotationVec(1.0f);
                                    Vec3d vec3d3 = vec3d.add(vec3d2.x * 8, vec3d2.y * 8, vec3d2.z * 8);
                                    Box box = playerEntity.getBoundingBox().stretch(vec3d2.multiply(8)).expand(1.0, 1.0, 1.0);
                                    EntityHitResult entityHitResult = ProjectileUtil.raycast(playerEntity, vec3d, vec3d3, box,
                                            entity -> !entity.isSpectator() && entity.collides(), 8 * 8);

                                    if (entityHitResult != null && entityHitResult.getEntity() != null) {
                                        Entity entity = entityHitResult.getEntity();
                                        if (entity instanceof ShopItemEntity shopItemEntity) {
                                            shopItemEntity.setOffset(y);
                                            context.getSource().sendFeedback(Text.of("Changed y offset to: §e" + y), false);
                                        } else {
                                            context.getSource().sendFeedback(Text.of("§cPlease look at an Shop Item!"), false);
                                        }
                                    }

                                    return 1;
                                }))
                ).then(CommandManager.literal("itemscale")
                        .then(CommandManager.argument("scale", FloatArgumentType.floatArg())
                                .executes((context) -> {
                                    float scale = FloatArgumentType.getFloat(context, "scale");
                                    PlayerEntity playerEntity = context.getSource().getPlayer();
                                    if (playerEntity == null) {
                                        return 0;
                                    }

                                    Vec3d vec3d = playerEntity.getCameraPosVec(1.0f);
                                    Vec3d vec3d2 = playerEntity.getRotationVec(1.0f);
                                    Vec3d vec3d3 = vec3d.add(vec3d2.x * 8, vec3d2.y * 8, vec3d2.z * 8);
                                    Box box = playerEntity.getBoundingBox().stretch(vec3d2.multiply(8)).expand(1.0, 1.0, 1.0);
                                    EntityHitResult entityHitResult = ProjectileUtil.raycast(playerEntity, vec3d, vec3d3, box,
                                            entity -> !entity.isSpectator() && entity.collides(), 8 * 8);

                                    if (entityHitResult != null && entityHitResult.getEntity() != null) {
                                        Entity entity = entityHitResult.getEntity();
                                        if (entity instanceof ShopItemEntity shopItemEntity) {
                                            shopItemEntity.setScale(scale);
                                            context.getSource().sendFeedback(Text.of("Changed scale to: §e" + scale), false);
                                        } else {
                                            context.getSource().sendFeedback(Text.of("§cPlease look at an Shop Item!"), false);
                                        }
                                    }

                                    return 1;
                                }))
                ).build();

        dispatcher.getRoot().addChild(rootNode);
    }
}
