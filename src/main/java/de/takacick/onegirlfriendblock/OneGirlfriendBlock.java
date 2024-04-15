package de.takacick.onegirlfriendblock;

import de.takacick.onegirlfriendblock.access.PlayerProperties;
import de.takacick.onegirlfriendblock.registry.EffectRegistry;
import de.takacick.onegirlfriendblock.registry.EntityRegistry;
import de.takacick.onegirlfriendblock.registry.ItemRegistry;
import de.takacick.onegirlfriendblock.registry.ParticleRegistry;
import de.takacick.onegirlfriendblock.registry.block.GirlfriendOneBlock;
import de.takacick.onegirlfriendblock.registry.entity.living.SimpYoinkEntity;
import de.takacick.onegirlfriendblock.server.oneblock.OneBlockHandler;
import de.takacick.onegirlfriendblock.server.oneblock.OneBlockServerState;
import de.takacick.utils.BionicUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class OneGirlfriendBlock implements ModInitializer {

    public static final String MOD_ID = "onegirlfriendblock";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "onegirlfriendblock"));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "onegirlfriendblock");

    @Override
    public void onInitialize() {
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
                .displayName(Text.translatable("itemGroup.onegirlfriendblock"))
                .icon(ItemRegistry.GIRLFRIEND_ONE_BLOCK_ITEM::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.GIRLFRIEND_HEAD);
                    entries.add(ItemRegistry.GIRLFRIEND_RIGHT_ARM);
                    entries.add(ItemRegistry.GIRLFRIEND_TORSO);
                    entries.add(ItemRegistry.GIRLFRIEND_LEFT_ARM);
                    entries.add(ItemRegistry.GIRLFRIEND_LEGS);
                    entries.add(ItemRegistry.GIRLFRIEND);
                    entries.add(ItemRegistry.MAID_SUIT);
                    entries.add(ItemRegistry.LIPSTICK_KATANA);
                    entries.add(ItemRegistry.CUTE_PLUSHY_MINER);
                    entries.add(ItemRegistry.HEART);
                    entries.add(ItemRegistry.FRENCH_FRIES);
                }).build());

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "girlfriendblockyoink"), (server, player, handler, buf, responseSender) -> {
            BlockPos blockPos = buf.readBlockPos();
            Direction direction = Direction.byId(buf.readInt());
            if (player instanceof PlayerProperties playerProperties) {
                server.execute(() -> {
                    World world = player.getWorld();
                    BlockState offset = world.getBlockState(blockPos.add(direction.getVector()));

                    if (playerProperties.hasOneGirlfriendBlock()) {
                        playerProperties.setOneGirlfriendBlock(false);

                        if (offset.isAir() || offset.isReplaceable()) {
                            world.setBlockState(blockPos.add(direction.getVector()), ItemRegistry.GIRLFRIEND_ONE_BLOCK.getDefaultState().with(GirlfriendOneBlock.FACING, player.getHorizontalFacing().getOpposite()));
                            world.playSound(null, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 2f, 1f);
                        }

                    } else if (world.getBlockState(blockPos).isOf(ItemRegistry.GIRLFRIEND_ONE_BLOCK)) {
                        playerProperties.setOneGirlfriendBlock(true);
                        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                        world.playSound(null, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 2f, 1f);
                    }
                });
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "girlfriendblockyoink"), (server, player, handler, buf, responseSender) -> {
            BlockPos blockPos = buf.readBlockPos();
            Direction direction = Direction.byId(buf.readInt());
            if (player instanceof PlayerProperties playerProperties) {
                server.execute(() -> {
                    World world = player.getWorld();
                    BlockState offset = world.getBlockState(blockPos.add(direction.getVector()));

                    if (playerProperties.hasOneGirlfriendBlock()) {
                        playerProperties.setOneGirlfriendBlock(false);

                        if (offset.isAir() || offset.isReplaceable()) {
                            world.setBlockState(blockPos.add(direction.getVector()), ItemRegistry.GIRLFRIEND_ONE_BLOCK.getDefaultState().with(GirlfriendOneBlock.FACING, player.getHorizontalFacing().getOpposite()));
                            world.playSound(null, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 2f, 1f);
                        }

                    } else if (world.getBlockState(blockPos).isOf(ItemRegistry.GIRLFRIEND_ONE_BLOCK)) {
                        playerProperties.setOneGirlfriendBlock(true);
                        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                        world.playSound(null, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 2f, 1f);
                    }
                });
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "girlfriendscaryyoink"), (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                Vec3d vec3d = new Vec3d(player.getRandom().nextGaussian(), 0, player.getRandom().nextGaussian()).normalize().multiply(80);

                World world = player.getWorld();
                SimpYoinkEntity simpYoinkEntity = new SimpYoinkEntity(EntityRegistry.SIMP_YOINK, world);
                simpYoinkEntity.refreshPositionAndAngles(player.getX() + vec3d.getX(), player.getY(), player.getZ() + vec3d.getZ(), 0f, 0f);
                simpYoinkEntity.setYoinkTarget(player);
                world.spawnEntity(simpYoinkEntity);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "swinglipstick"), (server, player, handler, buf, responseSender) -> {
            if (player.getMainHandStack().isOf(ItemRegistry.LIPSTICK_KATANA)) {
                server.execute(() -> {
                    BionicUtils.sendEntityStatus(player.getServerWorld(), player, OneGirlfriendBlock.IDENTIFIER, 2);
                });
            }
        });
    }
}
