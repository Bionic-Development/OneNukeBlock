package de.takacick.onescaryblock;

import de.takacick.onescaryblock.registry.*;
import de.takacick.onescaryblock.registry.entity.living.Entity303Entity;
import de.takacick.onescaryblock.utils.EntityUtils;
import de.takacick.onescaryblock.utils.datatracker.OneScaryBlockDataTracker;
import de.takacick.onescaryblock.utils.oneblock.OneBlockHandler;
import de.takacick.onescaryblock.utils.oneblock.OneBlockServerState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OneScaryBlock implements ModInitializer {

    public static final String MOD_ID = "onescaryblock";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "onescaryblock"));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "onescaryblock");

    @Override
    public void onInitialize() {
        OneScaryBlockDataTracker.register();
        ScreenHandlerRegistry.register();
        EffectRegistry.register();
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        new OneBlockHandler();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            OneBlockServerState.getServerState(server).ifPresent(oneBlockServerState -> {

            });
        });

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.onescaryblock"))
                .icon(ItemRegistry.SCARY_ONE_BLOCK_ITEM::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.SCARY_ONE_BLOCK_ITEM);
                    entries.add(ItemRegistry.SOUL_PIERCER);
                    entries.add(ItemRegistry.SOUL_FRAGMENT);
                    entries.add(ItemRegistry.PHANTOM_BLOCK_ITEM);
                    entries.add(ItemRegistry.HEROBRINE_LIGHTNING_BOLT);
                    entries.add(ItemRegistry.BLOOD_BORDER_SUIT);
                    entries.add(ItemRegistry.BLOOD_BUCKET);
                    entries.add(ItemRegistry.ITEM_303);
                }).build());

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(OneScaryBlock.MOD_ID, "scaryoneblockopen"), (server, player, handler, buf, responseSender) -> {
            BlockPos blockPos = buf.readBlockPos();
            server.execute(() -> {
                World world = player.getWorld();

                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.isOf(ItemRegistry.SCARY_ONE_BLOCK)) {
                    player.openHandledScreen(blockState.createScreenHandlerFactory(world, blockPos));
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(OneScaryBlock.MOD_ID, "entity303randomteleport"), (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ServerWorld world = player.getServerWorld();

                EntityUtils.getNearbyEntity(Entity303Entity.class, world, player.getPos(), 200, entity303Entity -> true)
                        .ifPresent(entity303Entity -> {
                            for (int i = 0; i < 30; i++) {
                                if (entity303Entity.teleportRandomly()) {
                                    break;
                                }
                            }
                        });
            });
        });

    }
}
