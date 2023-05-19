package de.takacick.heartmoney.registry.block.entity;

import de.takacick.heartmoney.registry.EntityRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.heartmoney.registry.entity.projectiles.HeartwarmingNukeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class HeartwarmingNukeBlockEntity extends BlockEntity {

    public boolean enabled = false;
    public int tick = 0;
    public int lastSecond = 0;

    protected HeartwarmingNukeBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public HeartwarmingNukeBlockEntity(BlockPos pos, BlockState state) {
        this(EntityRegistry.HEARTWARMING_NUKE_BLOCK, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, HeartwarmingNukeBlockEntity blockEntity) {
        if (blockEntity.enabled) {

            int seconds = blockEntity.tick / 20 + 1;

            world.syncWorldEvent(456789129, pos, 2);

            if (blockEntity.tick > 0) {
                blockEntity.tick--;

                TitleFadeS2CPacket titleFadeS2CPacket = new TitleFadeS2CPacket(0, 10, 0);
                TitleS2CPacket titleS2CPacket = new TitleS2CPacket(Text.of("§c" + seconds));
                SubtitleS2CPacket subtitleS2CPacket = new SubtitleS2CPacket(Text.of(""));

                world.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    serverPlayerEntity.networkHandler.sendPacket(titleFadeS2CPacket);
                    serverPlayerEntity.networkHandler.sendPacket(titleS2CPacket);
                    serverPlayerEntity.networkHandler.sendPacket(subtitleS2CPacket);
                });
                if (seconds != blockEntity.lastSecond) {
                    world.playSound(null, pos, SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.BLOCKS, 1, 3 - seconds);
                }
            } else {
                TitleFadeS2CPacket titleFadeS2CPacket = new TitleFadeS2CPacket(0, 0, 0);
                TitleS2CPacket titleS2CPacket = new TitleS2CPacket(Text.of(""));
                SubtitleS2CPacket subtitleS2CPacket = new SubtitleS2CPacket(Text.of(""));

                world.getOtherEntities(null, new Box(pos).expand(20)).forEach(entity -> {
                    if (entity instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) entity).networkHandler.sendPacket(titleFadeS2CPacket);
                        ((ServerPlayerEntity) entity).networkHandler.sendPacket(titleS2CPacket);
                        ((ServerPlayerEntity) entity).networkHandler.sendPacket(subtitleS2CPacket);
                    }
                });

                for (int x = 0; x < 15; x++) {
                    ((ServerWorld) world).spawnParticles(ParticleRegistry.HEART_EXPLOSION_EMITTER, pos.getX() + world.getRandom().nextDouble(),
                            pos.getY() + world.getRandom().nextDouble(),
                            pos.getZ() + world.getRandom().nextDouble(), 1,
                            0,
                            0,
                            0, 0.2);
                }

                world.setBlockState(pos, Blocks.AIR.getDefaultState());

                HeartwarmingNukeEntity heartwarmingNukeEntity = new HeartwarmingNukeEntity(EntityRegistry.HEARTWARMING_NUKE, world);
                heartwarmingNukeEntity.noClip = true;
                heartwarmingNukeEntity.setProperties(null, -90, 0, 0.0F, 0.8F , 0.8F);
                heartwarmingNukeEntity.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                world.spawnEntity(heartwarmingNukeEntity);

                world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 3, 1);
                world.playSound(null, pos, SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.BLOCKS, 3, 1);
            }

            blockEntity.lastSecond = seconds;
        }
    }

    public void setEnabled(boolean enabled) {
        if (!this.enabled) {
            this.enabled = enabled;
            this.tick = 99;
        }
    }
}

