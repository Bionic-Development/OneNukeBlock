package de.takacick.upgradebody.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.upgradebody.access.EntityProperties;
import de.takacick.upgradebody.registry.EntityRegistry;
import de.takacick.upgradebody.registry.entity.custom.EmeraldShopPortalEntity;
import de.takacick.upgradebody.registry.entity.custom.ShopItemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class EmeraldShopCommand {

    public static EmeraldShopPortalEntity EMERALD_SHOP_PORTAL;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("emeraldshop").requires((source) -> {
                    return source.hasPermissionLevel(2);
                }).then(CommandManager.literal("set")
                        .executes(context -> {
                            PlayerEntity playerEntity = context.getSource().getPlayer();
                            if (playerEntity == null) {
                                return 0;
                            }

                            ((EntityProperties) playerEntity).setEmeraldShopPortalCooldown(10);

                            if (EMERALD_SHOP_PORTAL != null) {
                                EMERALD_SHOP_PORTAL.discard();
                            }

                            EmeraldShopPortalEntity emeraldShopPortalEntity = new EmeraldShopPortalEntity(EntityRegistry.EMERALD_SHOP_PORTAL, playerEntity.getWorld());
                            emeraldShopPortalEntity.setPos(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ());
                            emeraldShopPortalEntity.setYaw(-playerEntity.getYaw());
                            emeraldShopPortalEntity.setAnimationProgress(emeraldShopPortalEntity.getMaxAnimationProgress());
                            playerEntity.getWorld().spawnEntity(emeraldShopPortalEntity);

                            EMERALD_SHOP_PORTAL = emeraldShopPortalEntity;
                            context.getSource().sendFeedback(() -> Text.of("Set Emerald Shop Portal to your location!"), false);
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
                                        context.getSource().sendFeedback(() -> Text.of("§cPlease hold an item in your hand!"), false);
                                        return 0;
                                    }

                                    ShopItemEntity shopItemEntity = new ShopItemEntity(playerEntity.getWorld(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), playerEntity.getMainHandStack().copy());
                                    shopItemEntity.setPrice(price);
                                    playerEntity.getEntityWorld().spawnEntity(shopItemEntity);

                                    context.getSource().sendFeedback(() -> Text.of("Summoned new Shop Item!"), false);

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
                                            entity -> !entity.isSpectator(), 8 * 8);

                                    if (entityHitResult != null && entityHitResult.getEntity() != null) {
                                        Entity entity = entityHitResult.getEntity();
                                        if (entity instanceof ShopItemEntity shopItemEntity) {
                                            shopItemEntity.setOffset(y);
                                            context.getSource().sendFeedback(() -> Text.of("Set the y offset to §b" + y), false);
                                        } else {
                                            context.getSource().sendFeedback(() -> Text.of("§cPlease look at an Shop Item!"), false);
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
                                            entity -> !entity.isSpectator(), 8 * 8);

                                    if (entityHitResult != null && entityHitResult.getEntity() != null) {
                                        Entity entity = entityHitResult.getEntity();
                                        if (entity instanceof ShopItemEntity shopItemEntity) {
                                            shopItemEntity.setScale(scale);
                                            context.getSource().sendFeedback(() -> Text.of("Set the scale to §b" + scale), false);
                                        } else {
                                            context.getSource().sendFeedback(() -> Text.of("§cPlease look at an Shop Item!"), false);
                                        }
                                    }

                                    return 1;
                                }))
                ).build();

        dispatcher.getRoot().addChild(rootNode);
    }
}
