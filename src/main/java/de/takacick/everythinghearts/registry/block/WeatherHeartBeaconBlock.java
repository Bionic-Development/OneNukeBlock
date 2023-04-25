package de.takacick.everythinghearts.registry.block;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.PlayerProperties;
import de.takacick.everythinghearts.access.ServerWorldProperties;
import de.takacick.everythinghearts.registry.EntityRegistry;
import de.takacick.everythinghearts.registry.ParticleRegistry;
import de.takacick.everythinghearts.registry.block.entity.WeatherHeartBeaconBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WeatherHeartBeaconBlock extends BlockWithEntity implements Stainable {

    public WeatherHeartBeaconBlock(Settings settings) {
        super(settings);
    }

    public DyeColor getColor() {
        return DyeColor.WHITE;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WeatherHeartBeaconBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, EntityRegistry.WEATHER_HEART_BEACON, WeatherHeartBeaconBlockEntity::tick);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof WeatherHeartBeaconBlockEntity weatherHeartBeaconBlockEntity) {
            weatherHeartBeaconBlockEntity.toggled = !weatherHeartBeaconBlockEntity.toggled;

            if (weatherHeartBeaconBlockEntity.toggled) {
                for (int i = 0; i < 20; i++) {
                    ((ServerWorld) world).spawnParticles(ParticleRegistry.HEART,
                            pos.getX() + 0.5d,
                            pos.getY() + 0.9d,
                            pos.getZ() + 0.5d, 1,
                            0.2,
                            0.2,
                            0.2,
                            0.4);
                }

                if (player instanceof PlayerProperties playerProperties) {
                    weatherHeartBeaconBlockEntity.heartLevel = playerProperties.getHeartTouchLevel();
                } else {
                    weatherHeartBeaconBlockEntity.heartLevel = 1;
                }

                player.sendMessage(Text.of("§e[§c❤§e] §aAltering world's weather..."));

                world.playSound(null, pos.add(0, 1, 0), SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.BLOCKS,
                        3f, 2f);
            }

            WeatherHeartBeaconBlockEntity.tick(world, pos, state, weatherHeartBeaconBlockEntity);
            blockEntity.markDirty();
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }

        return ActionResult.CONSUME;
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {

        if (world.getBlockEntity(pos) instanceof WeatherHeartBeaconBlockEntity blockEntity) {
            ((ServerWorldProperties) world).setHeartLevel(1);
            ((ServerWorldProperties) world).setHeartRaining(false);

            if (!(blockEntity.rainGradient <= 0 && blockEntity.rainGradientPrev <= 0)) {
                PacketByteBuf packetByteBuf = PacketByteBufs.create();
                packetByteBuf.writeFloat(0f);

                world.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    ServerPlayNetworking.send(serverPlayerEntity, new Identifier(EverythingHearts.MOD_ID, "heartrain"), packetByteBuf);
                });
            }
        }

        super.onBroken(world, pos, state);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {

        tooltip.add(Text.of("§eUses the power of §clove §eto alter the"));
        tooltip.add(Text.of("§eworld's weather..."));

        super.appendTooltip(stack, world, tooltip, options);
    }
}
