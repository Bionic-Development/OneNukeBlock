package de.takacick.onescaryblock.registry.block.entity;

import de.takacick.onescaryblock.OneScaryBlockClient;
import de.takacick.onescaryblock.registry.EntityRegistry;
import de.takacick.onescaryblock.registry.ParticleRegistry;
import de.takacick.onescaryblock.utils.ConsumedItem;
import de.takacick.onescaryblock.utils.oneblock.OneBlockServerState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ScaryOneBlockBlockEntity extends BlockEntity {

    private final List<ConsumedItem> consumedItems = new ArrayList<>();
    private int age = 0;
    private boolean ritual = false;
    private int ritualTicks = 0;
    private final int maxRitualTicks = 140;

    public ScaryOneBlockBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.SCARY_ONE_BLOCK_BLOCK_ENTITY, blockPos, blockState);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ScaryOneBlockBlockEntity blockEntity) {
        blockEntity.age++;

        blockEntity.consumedItems.forEach(consumedItem -> {
            consumedItem.tick(world, pos);
        });

        blockEntity.consumedItems.removeIf(ConsumedItem::shouldRemove);

        if (blockEntity.isRitualRunning()) {
            blockEntity.ritualTicks++;
            float progress = blockEntity.getRitualProgress(0.5f);
            if (world.isClient) {
                for (int i = 0; i <= 128 * (progress) + 1; i++) {
                    double angle = world.getRandom().nextDouble() * 360;
                    double x = (world.getRandom().nextDouble() * 8.8f * Math.cos(angle));
                    double z = (world.getRandom().nextDouble() * 8.8f * Math.sin(angle));

                    world.addParticle(ParticleRegistry.FALLING_BLOOD, pos.getX() + x, pos.getY() + 5 + world.getRandom().nextInt(15), pos.getZ() + z, 0, 0, 0);
                }

                if (blockEntity.ritualTicks % (5 - (int) (progress * 4)) == 0 || progress >= 1f) {
                    world.playSound(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.HOSTILE, 3f, 1f, false);
                }

                if (blockEntity.ritualTicks % 5 == 0 && progress > 0.35 || progress >= 1f) {
                    double angle = world.getRandom().nextDouble() * 360;
                    double x = (world.getRandom().nextDouble() * 8.8f * Math.cos(angle));
                    double z = (world.getRandom().nextDouble() * 8.8f * Math.sin(angle));

                    world.playSound(pos.getX() + x, pos.getY(), pos.getZ() + z, SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.HOSTILE, 3f, 0.5f, false);
                }
            }

            if (progress >= 1f) {
                if (world.isClient) {
                    for (int i = 0; i <= 10; i++) {
                        double angle = world.getRandom().nextDouble() * 360;
                        double x = (world.getRandom().nextDouble() * 10.8f * Math.cos(angle));
                        double z = (world.getRandom().nextDouble() * 10.8f * Math.sin(angle));

                        world.addParticle(ParticleRegistry.SMOKE,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                x, 6 * world.getRandom().nextDouble(), z);
                    }
                }

                Vec3d position = pos.toCenterPos();
                List<Entity> targets = world.getOtherEntities(null, new Box(pos.add(0, 0, 0)).expand(15, 15, 15), entity -> !entity.isSpectator()).stream().toList();
                targets.forEach(entity -> {
                    Vec3d vec3d = position.subtract(entity.getPos()).normalize().multiply(0.175, 0.175, 0.175);
                    entity.addVelocity(vec3d.getX(), vec3d.getY(), vec3d.getZ());

                    if (entity.getPos().distanceTo(position) <= 0.55d) {
                        entity.setVelocity(entity.getVelocity().multiply(0.2));
                        if (entity instanceof ServerPlayerEntity player) {
                            player.changeGameMode(GameMode.SPECTATOR);
                        } else if (!entity.isPlayer()) {
                            entity.discard();
                        } else {
                            OneScaryBlockClient.setMojangOverlay(entity);
                        }
                    }
                });

                if (!world.isClient) {
                    if (targets.stream().noneMatch(Entity::isPlayer)) {
                        OneBlockServerState.getServerState(world.getServer()).ifPresent(oneBlockServerState -> {
                            oneBlockServerState.setScaryOneBlockRitual(false);
                        });
                        blockEntity.ritual = false;
                        blockEntity.ritualTicks = 0;
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
            }
        }

        world.updateListeners(blockEntity.getPos(), blockEntity.getCachedState(),
                blockEntity.getCachedState(), Block.NOTIFY_ALL);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (!getConsumedItems().isEmpty()) {
            NbtList nbtList = new NbtList();

            getConsumedItems().forEach(consumedItem -> {
                nbtList.add(consumedItem.writeNbt(new NbtCompound()));
            });

            nbt.put("consumedItems", nbtList);
        }

        nbt.putBoolean("ritualRunning", this.ritual);
        nbt.putInt("ritualTicks", this.ritualTicks);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        nbt.getList("consumedItems", NbtElement.COMPOUND_TYPE).forEach(nbtElement -> {
            if (nbtElement instanceof NbtCompound nbtCompound) {
                ConsumedItem consumedItem = new ConsumedItem(ItemStack.EMPTY);
                consumedItem.readNbt(nbtCompound);

                getConsumedItems().stream().filter(item -> item.getUuid().equals(consumedItem.getUuid())).findFirst().ifPresentOrElse(consumedItem1 -> {
                    consumedItem1.readNbt(nbtCompound);
                }, () -> {
                    getConsumedItems().add(consumedItem);
                });
            }
        });
        this.ritual = nbt.getBoolean("ritualRunning");

        if (nbt.contains("ritualTicks", NbtElement.INT_TYPE)) {
            int ritualTicks = nbt.getInt("ritualTicks");
            if (MathHelper.abs(ritualTicks - this.ritualTicks) > 1) {
                this.ritualTicks = ritualTicks;
            }
        }

        if (!this.ritual) {
            this.ritualTicks = 0;
        }
    }

    public void setRitual(boolean ritual) {
        this.ritual = ritual;
    }

    public boolean isRitualRunning() {
        return this.ritual;
    }

    public int getAge() {
        return this.age;
    }

    public List<ConsumedItem> getConsumedItems() {
        return this.consumedItems;
    }

    public float getRitualTicks(float tickDelta) {
        return this.ritualTicks + tickDelta;
    }

    public float getRitualProgress(float tickDelta) {
        return Math.min((this.ritualTicks + tickDelta) / maxRitualTicks, 1f);
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
}

