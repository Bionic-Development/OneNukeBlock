package de.takacick.onegirlboyblock;

import de.takacick.onegirlboyblock.registry.EntityRegistry;
import de.takacick.onegirlboyblock.registry.ItemRegistry;
import de.takacick.onegirlboyblock.registry.ParticleRegistry;
import de.takacick.onegirlboyblock.registry.item.GlitterBlade;
import de.takacick.onegirlboyblock.utils.data.AttachmentTypes;
import de.takacick.onegirlboyblock.utils.data.ItemDataComponents;
import de.takacick.onegirlboyblock.utils.oneblock.OneBlockServerState;
import de.takacick.onegirlboyblock.utils.oneblock.server.OneBlockCommand;
import de.takacick.utils.common.ability.server.AbilityPlayNetworking;
import de.takacick.utils.common.event.EventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class OneGirlBoyBlock implements ModInitializer {

    public static final String MOD_ID = "onegirlboyblock";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(MOD_ID, "onegirlboyblock"));
    public static final Identifier IDENTIFIER = Identifier.of(MOD_ID, "onegirlboyblock");

    @Override
    public void onInitialize() {
        ItemDataComponents.register();
        AttachmentTypes.register();
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            OneBlockCommand.register("OneGirlBlock", ItemRegistry.ONE_GIRL_BLOCK, 0xFF52DC, dispatcher, registryAccess, server -> OneBlockServerState.getServerState(server).map(OneBlockServerState::getGirlOneBlock));
            OneBlockCommand.register("OneBoyBlock", ItemRegistry.ONE_BOY_BLOCK, 0x2B9BFF, dispatcher, registryAccess, server -> OneBlockServerState.getServerState(server).map(OneBlockServerState::getBoyOneBlock));
        });

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            OneBlockServerState.getServerState(server).ifPresent(oneBlockServerState -> oneBlockServerState.tick(server));
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            OneBlockServerState.getServerState(server).ifPresent(oneBlockServerState -> {

            });
        });

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.onegirlboyblock"))
                .icon(ItemRegistry.ONE_GIRL_BLOCK_ITEM::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.ONE_GIRL_BLOCK_ITEM);
                    entries.add(ItemRegistry.ONE_BOY_BLOCK_ITEM);
                    entries.add(ItemRegistry.BASEBALL_BAT);
                    entries.add(ItemRegistry.FOOTBALL_GEAR);
                    entries.add(ItemRegistry.TURBO_BOARD);
                    entries.add(ItemRegistry.BIT_CANNON);
                    entries.add(ItemRegistry.GOLDEN_MEAT);
                    entries.add(ItemRegistry.WEIGHTED_ANVIL_HAMMER);
                    entries.add(ItemRegistry.GLITTER_BLADE);
                    entries.add(ItemRegistry.TIARA);
                    entries.add(ItemRegistry.BUTTERFLY_WINGS);
                    entries.add(ItemRegistry.INFERNO_HAIR_DRYER);
                    entries.add(ItemRegistry.STRAWBERRY_SHORTCAKE);
                    entries.add(ItemRegistry.STAR_MINER);
                }).build());

        AbilityPlayNetworking.register(Identifier.of(OneGirlBoyBlock.MOD_ID, "glitterbladehit"), (abilityPacket, context) -> {
            ServerPlayerEntity player = context.player();
            if (player.getMainHandStack().getItem() instanceof GlitterBlade) {
                context.server().execute(() -> {
                    EventHandler.sendEntityStatus(player.getServerWorld(), player, OneGirlBoyBlock.IDENTIFIER, 3, 0);
                });
            }
        });
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
}
