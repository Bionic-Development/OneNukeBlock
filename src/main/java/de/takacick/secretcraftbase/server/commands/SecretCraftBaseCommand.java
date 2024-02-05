package de.takacick.secretcraftbase.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.utils.BionicUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.UUID;

public class SecretCraftBaseCommand {

    public static final ServerBossBar progressBar = new ServerBossBar(Text.of("§e§lBase §c§lHunt"), BossBar.Color.GREEN, BossBar.Style.PROGRESS);
    public static int maxTicks = 100;
    public static int ticks = 0;
    public static boolean running;
    public static UUID lastPlayer;

    private static World WORLD;
    private static Vec3d POS;
    private static float YAW;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            if (running) {
                ticks++;

                progressBar.setPercent(Math.min((float) ticks / (float) maxTicks, 1f));

                if (progressBar.getPercent() >= 1f) {
                    running = false;
                    TitleFadeS2CPacket titleFadeS2CPacket = new TitleFadeS2CPacket(5, 60, 5);
                    TitleS2CPacket titleS2CPacket = new TitleS2CPacket(Text.of("§cTIME'S UP!"));
                    SubtitleS2CPacket subtitleS2CPacket = new SubtitleS2CPacket(Text.of("§a[§eBase §cHunt§e]"));
                    server.getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                        if (!serverPlayerEntity.isSpectator()) {
                            BionicUtils.sendEntityStatus(serverPlayerEntity.getWorld(), serverPlayerEntity, SecretCraftBase.IDENTIFIER, 6);
                        }
                        if (lastPlayer != null && serverPlayerEntity.getUuid().equals(lastPlayer)) {
                            serverPlayerEntity.changeGameMode(GameMode.SURVIVAL);
                        }

                        serverPlayerEntity.networkHandler.sendPacket(titleFadeS2CPacket);
                        serverPlayerEntity.networkHandler.sendPacket(titleS2CPacket);
                        serverPlayerEntity.networkHandler.sendPacket(subtitleS2CPacket);
                    });
                }
            }

            progressBar.setVisible(running);

            server.getPlayerManager().getPlayerList().forEach(progressBar::addPlayer);
        });

        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("secretcraftbase")
                .requires(source -> {
                    return source.hasPermissionLevel(2);
                }).then(CommandManager.literal("basehunt")
                        .then(CommandManager.literal("duration")
                                .then(CommandManager.argument("seconds", IntegerArgumentType.integer(0)).executes(context -> {
                                    int seconds = IntegerArgumentType.getInteger(context, "seconds");
                                    maxTicks = seconds * 20;
                                    ticks = MathHelper.clamp(ticks, 0, maxTicks);
                                    progressBar.setPercent((float) ticks / (float) maxTicks);

                                    context.getSource().sendFeedback(() -> Text.literal("Set Base Hunt Timer duration to " + seconds + " seconds"), false);

                                    return 1;
                                }))
                        ).then(CommandManager.literal("progress")
                                .then(CommandManager.argument("seconds", IntegerArgumentType.integer(0)).executes(context -> {
                                    ticks = MathHelper.clamp(IntegerArgumentType.getInteger(context, "seconds") * 20, 0, maxTicks);
                                    progressBar.setPercent((float) ticks / (float) maxTicks);

                                    context.getSource().sendFeedback(() -> Text.literal("Set Base Hunt Timer progress to " + IntegerArgumentType.getInteger(context, "seconds") + " seconds"), false);

                                    return 1;
                                }))
                        ).then(CommandManager.literal("stop").executes(context -> {
                            running = false;
                            ticks = 0;
                            progressBar.setVisible(false);
                            progressBar.setPercent(0f);
                            lastPlayer = null;

                            context.getSource().sendFeedback(() -> Text.literal("Stopped Base Hunt Timer"), false);

                            return 1;
                        }))
                ).then(CommandManager.literal("pigportal").executes(context -> {
                            if (context.getSource().getPlayer() == null) {
                                return 0;
                            }

                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            WORLD = playerEntity.getWorld();
                            POS = playerEntity.getPos();
                            YAW = playerEntity.getYaw();
                            context.getSource().sendFeedback(() -> Text.of("Set Pig Portal location to your current position"), false);

                            return 1;
                        })
                ).build();
        dispatcher.getRoot().addChild(rootNode);
    }

    public static void teleport(Entity entity) {
        if (WORLD == null || POS == null) {
            return;
        }
        World world = entity.getWorld();
        world.playSound(null, entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, entity.getSoundCategory(), 1f, 1f);
        world.playSound(null, entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_PIG_AMBIENT, entity.getSoundCategory(), 1f, 1f);
        world.sendEntityStatus(entity, EntityStatuses.ADD_PORTAL_PARTICLES);

        if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
            serverPlayerEntity.teleport((ServerWorld) WORLD, POS.getX(), POS.getY() + 0.01, POS.getZ(), YAW, 0);
            WORLD.sendEntityStatus(serverPlayerEntity, EntityStatuses.ADD_PORTAL_PARTICLES);
            WORLD.playSound(null, serverPlayerEntity.getX(), serverPlayerEntity.getBodyY(0.5), serverPlayerEntity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, entity.getSoundCategory(), 1f, 1f);
            WORLD.playSound(null, serverPlayerEntity.getX(), serverPlayerEntity.getBodyY(0.5), serverPlayerEntity.getZ(), SoundEvents.ENTITY_PIG_AMBIENT, entity.getSoundCategory(), 0.3f, 1f);
        } else {
            if (entity.getWorld().equals(WORLD)) {
                Entity entity1 = entity.moveToWorld((ServerWorld) WORLD);
                if (entity1 != null) {
                    WORLD.sendEntityStatus(entity1, EntityStatuses.ADD_PORTAL_PARTICLES);
                    WORLD.playSound(null, entity1.getX(), entity1.getBodyY(0.5), entity1.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, entity.getSoundCategory(), 1f, 1f);
                    WORLD.playSound(null, entity1.getX(), entity1.getBodyY(0.5), entity1.getZ(), SoundEvents.ENTITY_PIG_AMBIENT, entity.getSoundCategory(), 1f, 1f);
                }
            }
            entity.requestTeleport(POS.getX(), POS.getY() + 0.01, POS.getZ());
        }
    }
}
