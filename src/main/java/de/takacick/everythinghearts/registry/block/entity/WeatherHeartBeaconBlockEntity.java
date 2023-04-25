package de.takacick.everythinghearts.registry.block.entity;

import com.google.common.collect.Lists;
import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.LivingProperties;
import de.takacick.everythinghearts.access.ServerWorldProperties;
import de.takacick.everythinghearts.registry.EntityRegistry;
import de.takacick.everythinghearts.registry.ParticleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class WeatherHeartBeaconBlockEntity extends BlockEntity {

    public boolean activated = false;
    public boolean toggled = false;
    public int heartLevel = 1;
    public int age = 0;
    public float rotation = 0;
    public float prevRotation = 0;

    List<BeamSegment> beamSegments = Lists.newArrayList();
    int level;
    private List<BeamSegment> field_19178 = Lists.newArrayList();
    public int minY;
    public float rainGradientPrev;
    public float rainGradient;

    public WeatherHeartBeaconBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.WEATHER_HEART_BEACON, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, WeatherHeartBeaconBlockEntity blockEntity) {

        if (!world.isClient) {
            blockEntity.rainGradientPrev = blockEntity.rainGradient;
            if (blockEntity.activated) {
                blockEntity.rainGradient += 0.01f;
            } else {
                blockEntity.rainGradient -= 0.01f;
            }
            blockEntity.rainGradient = MathHelper.clamp(blockEntity.rainGradient, 0.0f, 1.0f);

            ((ServerWorldProperties) world).setHeartLevel(blockEntity.heartLevel);

            if (!(blockEntity.rainGradient <= 0 && blockEntity.rainGradientPrev <= 0)) {
                PacketByteBuf packetByteBuf = PacketByteBufs.create();
                packetByteBuf.writeFloat(blockEntity.rainGradient);
                ((ServerWorldProperties) world).setHeartRaining(true);

                world.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    ServerPlayNetworking.send(serverPlayerEntity, new Identifier(EverythingHearts.MOD_ID, "heartrain"), packetByteBuf);
                });
            } else {
                ((ServerWorldProperties) world).setHeartRaining(false);
            }
        } else {
            if (blockEntity.activated) {
                blockEntity.age++;
            }
        }

        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        BlockPos blockPos2;
        if (blockEntity.minY < j) {
            blockPos2 = pos;
            blockEntity.field_19178 = Lists.newArrayList();
            blockEntity.minY = pos.getY() - 1;
        } else {
            blockPos2 = new BlockPos(i, blockEntity.minY + 1, k);
        }

        BeamSegment beamSegment = blockEntity.field_19178.isEmpty() ? null : blockEntity.field_19178.get(blockEntity.field_19178.size() - 1);
        int l = world.getTopY(Heightmap.Type.WORLD_SURFACE, i, k);

        int n;
        for (n = 0; n < 10 && blockPos2.getY() <= l; ++n) {
            BlockState blockState = world.getBlockState(blockPos2);
            Block block = blockState.getBlock();
            if (block instanceof Stainable) {
                float[] fs = ((Stainable) block).getColor().getColorComponents();
                if (blockEntity.field_19178.size() <= 1) {
                    beamSegment = new BeamSegment(fs);
                    blockEntity.field_19178.add(beamSegment);
                } else if (beamSegment != null) {
                    if (Arrays.equals(fs, beamSegment.color)) {
                        beamSegment.increaseHeight();
                    } else {
                        beamSegment = new BeamSegment(new float[]{(beamSegment.color[0] + fs[0]) / 2.0F, (beamSegment.color[1] + fs[1]) / 2.0F, (beamSegment.color[2] + fs[2]) / 2.0F});
                        blockEntity.field_19178.add(beamSegment);
                    }
                }
            } else {
                if (beamSegment == null || blockState.getOpacity(world, blockPos2) >= 15 && !blockState.isOf(Blocks.BEDROCK)) {
                    blockEntity.field_19178.clear();
                    blockEntity.minY = l;
                    break;
                }

                beamSegment.increaseHeight();
            }

            blockPos2 = blockPos2.up();
            ++blockEntity.minY;
        }

        n = blockEntity.level;
        if (world.getTime() % 80L == 0L) {
            if (!blockEntity.beamSegments.isEmpty() && blockEntity.activated) {
                playSound(world, pos, SoundEvents.BLOCK_BEACON_AMBIENT);
            }
        }

        if (world.getTime() % 2L == 0) {
            if (!blockEntity.beamSegments.isEmpty() && blockEntity.activated) {
                applyDamage(world, pos);
            }
        }

        if (blockEntity.minY >= l) {
            blockEntity.minY = world.getBottomY() - 1;
            boolean bl = n > 0;
            blockEntity.beamSegments = blockEntity.field_19178;
            if (!world.isClient) {
                if (!bl && !blockEntity.activated && blockEntity.toggled) {
                    blockEntity.activated = true;
                    playSound(world, pos, SoundEvents.BLOCK_BEACON_ACTIVATE);
                    var var17 = world.getNonSpectatingEntities(ServerPlayerEntity.class, (new Box(i, j, k, i, (j - 4), k)).expand(10.0D, 5.0D, 10.0D)).iterator();

                    while (var17.hasNext()) {
                        ServerPlayerEntity serverPlayerEntity = var17.next();
                        Criteria.CONSTRUCT_BEACON.trigger(serverPlayerEntity, blockEntity.level);
                    }
                    blockEntity.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
                } else if ((bl || !blockEntity.toggled) && blockEntity.activated) {
                    playSound(world, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE);
                    blockEntity.activated = false;
                    blockEntity.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
                }
            }
        }
    }

    private static void applyDamage(World world, BlockPos pos) {
        if (!world.isClient) {

            Box box = (new Box(pos).expand(-0.3)).stretch(0.0D, world.getHeight(), 0.0D);
            List<Entity> list = world.getNonSpectatingEntities(Entity.class, box);
            Iterator<Entity> var11 = list.iterator();

            while (var11.hasNext()) {
                Entity entity = var11.next();
                if (entity instanceof LivingProperties livingProperties) {
                    livingProperties.tryToHeartInfect(null);
                }
            }
        } else {
            Box box = (new Box(pos).expand(0)).stretch(0.0D, world.getHeight(), 0.0D);

            for (double y = box.minY; y < box.maxY; y++) {
                if (world.getRandom().nextDouble() <= 0.2) {
                    world.addParticle(ParticleRegistry.HEART, true,
                            pos.getX() + 0.5,
                            y + 0.5,
                            pos.getZ() + 0.5,
                            world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15);
                }
            }
        }
    }

    public static void playSound(World world, BlockPos pos, SoundEvent sound) {
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void markRemoved() {
        playSound(this.world, this.pos, SoundEvents.BLOCK_BEACON_DEACTIVATE);
        super.markRemoved();
    }

    public List<BeamSegment> getBeamSegments() {
        return this.beamSegments;
    }

    public void setWorld(World world) {
        super.setWorld(world);
        this.minY = world.getBottomY() - 1;
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        tag.putBoolean("activated", activated);
        tag.putBoolean("toggled", toggled);
        super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        activated = tag.getBoolean("activated");
        toggled = tag.getBoolean("toggled");
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public static class BeamSegment {
        final float[] color;
        private int height;

        public BeamSegment(float[] color) {
            this.color = color;
            this.height = 1;
        }

        protected void increaseHeight() {
            ++this.height;
        }

        public float[] getColor() {
            return this.color;
        }

        public int getHeight() {
            return this.height;
        }
    }
}
