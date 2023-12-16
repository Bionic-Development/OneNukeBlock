package de.takacick.tinyhouse.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TinyBaseCommand {

    private static World WORLD;
    private static Vec3d POS;
    private static float YAW;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("settinybase")
                .requires(source -> {
                    return source.hasPermissionLevel(2);
                }).executes(context -> {
                            if (context.getSource().getPlayer() == null) {
                                return 0;
                            }

                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            WORLD = playerEntity.getWorld();
                            POS = playerEntity.getPos();
                            YAW = playerEntity.getYaw();
                            context.getSource().sendFeedback(() -> Text.of("Set Tiny Base location to your current position"), false);

                            return 1;
                        }
                ).build();
        dispatcher.getRoot().addChild(rootNode);
    }

    public static void teleport(Entity entity) {
        if (WORLD == null || POS == null) {
            return;
        }
        World world = entity.getWorld();
        world.playSound(null, entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, entity.getSoundCategory(), 1f, 1f);

        if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
            serverPlayerEntity.teleport((ServerWorld) WORLD, POS.getX(), POS.getY() + 0.01, POS.getZ(), YAW, 0);
            WORLD.playSound(null, serverPlayerEntity.getX(), serverPlayerEntity.getBodyY(0.5), serverPlayerEntity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, entity.getSoundCategory(), 1f, 1f);
        } else {
            if (entity.getWorld().equals(WORLD)) {
                Entity entity1 = entity.moveToWorld((ServerWorld) WORLD);
                if (entity1 != null) {
                    WORLD.playSound(null, entity1.getX(), entity1.getBodyY(0.5), entity1.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, entity.getSoundCategory(), 1f, 1f);
                }
            }
            entity.requestTeleport(POS.getX(), POS.getY() + 0.01, POS.getZ());
        }
    }
}
