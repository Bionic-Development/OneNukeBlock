package de.takacick.onenukeblock;

import de.takacick.onenukeblock.registry.EffectRegistry;
import de.takacick.onenukeblock.registry.EntityRegistry;
import de.takacick.onenukeblock.registry.ItemRegistry;
import de.takacick.onenukeblock.registry.ParticleRegistry;
import de.takacick.onenukeblock.registry.block.NukeOneBlock;
import de.takacick.onenukeblock.utils.data.AttachmentTypes;
import de.takacick.onenukeblock.utils.data.ItemDataComponents;
import de.takacick.onenukeblock.utils.network.BangMaceBoostExplosionPacket;
import de.takacick.onenukeblock.utils.network.BangMaceExplosionPacket;
import de.takacick.onenukeblock.utils.network.UseNukeBlockPacket;
import de.takacick.onenukeblock.utils.nuketimer.NukeTimerServerState;
import de.takacick.onenukeblock.utils.oneblock.OneBlockServerState;
import de.takacick.onenukeblock.utils.oneblock.server.OneBlockCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class OneNukeBlock implements ModInitializer {

    public static final String MOD_ID = "onenukeblock";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(MOD_ID, "onenukeblock"));
    public static final Identifier IDENTIFIER = Identifier.of(MOD_ID, "onenukeblock");

    @Override
    public void onInitialize() {
        ItemDataComponents.register();
        AttachmentTypes.register();
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();
        EffectRegistry.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            OneBlockCommand.register("OneNukeBlock", ItemRegistry.ONE_NUKE_BLOCK, 0xFFFF00, dispatcher, registryAccess, server -> OneBlockServerState.getServerState(server).map(OneBlockServerState::getOneBlock));
        });

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            OneBlockServerState.getServerState(server).ifPresent(oneBlockServerState -> oneBlockServerState.tick(server));
            NukeTimerServerState.getServerState(server).ifPresent(serverState -> serverState.tick(server));
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            OneBlockServerState.getServerState(server).ifPresent(serverState -> {

            });
            NukeTimerServerState.getServerState(server).ifPresent(serverState -> {

            });
        });

        PayloadTypeRegistry.playS2C().register(BangMaceBoostExplosionPacket.PACKET_ID, BangMaceBoostExplosionPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(BangMaceExplosionPacket.PACKET_ID, BangMaceExplosionPacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(UseNukeBlockPacket.PACKET_ID, UseNukeBlockPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(UseNukeBlockPacket.PACKET_ID, (payload, context) -> {
            MinecraftServer server = context.server();
            PlayerEntity player = context.player();
            server.execute(() -> {
                BlockPos blockPos = payload.blockPos();
                World world = player.getWorld();

                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.getBlock() instanceof NukeOneBlock) {
                    player.openHandledScreen(blockState.createScreenHandlerFactory(world, blockPos));
                }
            });
        });

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.onenukeblock"))
                .icon(ItemRegistry.ONE_NUKE_BLOCK_ITEM::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.ONE_NUKE_BLOCK_ITEM);
                    entries.add(ItemRegistry.KABOOM_MINER);
                    entries.add(ItemRegistry.SKYLAND_TNT_ITEM);
                    entries.add(ItemRegistry.BLADED_TNT_ITEM);
                    entries.add(ItemRegistry.EXPLOSIVE_GUMMY_BEAR);
                    entries.add(ItemRegistry.BANG_MACE);
                    entries.add(ItemRegistry.DIAMOND_HAZMAT_HELMET);
                    entries.add(ItemRegistry.DIAMOND_HAZMAT_CHESTPLATE);
                    entries.add(ItemRegistry.DIAMOND_HAZMAT_LEGGINGS);
                    entries.add(ItemRegistry.DIAMOND_HAZMAT_BOOTS);
                    entries.add(ItemRegistry.NUCLEAR_WATER_BUCKET);
                }).build());
    }

    public static List<BlockPos> generateSphere(BlockPos centerBlock, int radius, boolean hollow) {

        List<BlockPos> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getX();
        int by = centerBlock.getY();
        int bz = centerBlock.getZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                if (y <= 0) {
                    continue;
                }
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        circleBlocks.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return circleBlocks;
    }

    public static List<BlockPos> generateCircle(BlockPos centerBlock, int radius, boolean hollow) {

        List<BlockPos> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getX();
        int by = centerBlock.getY();
        int bz = centerBlock.getZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int z = bz - radius; z <= bz + radius; z++) {
                double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)));
                if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                    circleBlocks.add(new BlockPos(x, by, z));
                }
            }
        }

        return circleBlocks;
    }
}
