package de.takacick.immortalmobs.registry.item;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.ImmortalMobsClient;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ImmortalOrb extends Item implements ImmortalItem {

    private final HashMap<UUID, BlockPos> lastLocation = new HashMap<>();

    public ImmortalOrb(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (!world.isClient) {

            if (user.isSneaking()) {
                useAbility((ServerWorld) world, user);
            } else {
                HitResult hitResult = user.raycast(200, 0, true);

                if (!hitResult.getType().equals(HitResult.Type.MISS)) {
                    for (int i = 0; i < 32; ++i) {
                        ((ServerPlayerEntity) user).getWorld().spawnParticles(ParticleRegistry.IMMORTAL_PORTAL, user.getX(), user.getY() + user.getRandom().nextDouble() * 2.0D, user.getZ(), 1, 0, 0.0D, 0, user.getRandom().nextGaussian());
                    }
                    BionicUtils.sendEntityStatus((ServerWorld) world, user, ImmortalMobs.IDENTIFIER, 5);

                    BlockPos blockPos = new BlockPos(hitResult.getPos().getX(), hitResult.getPos().getY(), hitResult.getPos().getZ());
                    user.teleport((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 1, (double) blockPos.getZ() + 0.5);
                    world.playSound(null, blockPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 1);
                    for (int i = 0; i < 32; ++i) {
                        ((ServerPlayerEntity) user).getWorld().spawnParticles(ParticleRegistry.IMMORTAL_PORTAL, user.getX(), user.getY() + user.getRandom().nextDouble() * 2.0D, user.getZ(), 1, 0, 0.0D, 0, user.getRandom().nextGaussian());
                    }
                    BionicUtils.sendEntityStatus((ServerWorld) world, user, ImmortalMobs.IDENTIFIER, 5);
                }
            }
        } else if (user.isSneaking()) {
            ImmortalMobsClient.MOVED = true;
            ImmortalMobsClient.TELEPORTED = true;
        }

        return super.use(world, user, hand);
    }


    public void useAbility(ServerWorld world, PlayerEntity user) {
        ServerWorld targetWorld = null;
        BlockPos blockPos = BlockPos.ORIGIN;
        BlockState blockState;
        ChunkPos chunkPos;

        BionicUtils.sendEntityStatus(world, user, ImmortalMobs.IDENTIFIER, 3);

        if (world.getRegistryKey().equals(World.OVERWORLD)) {
            this.lastLocation.put(user.getUuid(), user.getBlockPos());
            targetWorld = user.getServer().getWorld(World.NETHER);
            blockPos = user.getBlockPos();
            targetWorld.setBlockState(blockPos, Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ()), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() + 1, blockPos.getZ()), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ() - 1), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() + 1, blockPos.getZ() - 1), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ() - 1), Blocks.AIR.getDefaultState());
            blockState = targetWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ()));
            if (blockState.isAir() || blockState.getBlock().equals(Blocks.LAVA)) {
                targetWorld.setBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ()), Blocks.NETHERRACK.getDefaultState());
            }

            if (targetWorld.getBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ())).isAir() || targetWorld.getBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ())).getBlock().equals(Blocks.LAVA)) {
                targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ()), Blocks.NETHERRACK.getDefaultState());
            }

            if (targetWorld.getBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ() - 1)).isAir() || targetWorld.getBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ() - 1)).getBlock().equals(Blocks.LAVA)) {
                targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ() - 1), Blocks.NETHERRACK.getDefaultState());
            }

            if (targetWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ() - 1)).isAir() || targetWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ() - 1)).getBlock().equals(Blocks.LAVA)) {
                targetWorld.setBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ() - 1), Blocks.NETHERRACK.getDefaultState());
            }

            chunkPos = new ChunkPos(blockPos);
            targetWorld.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, user.getId());
            user.stopRiding();
            if (user.isSleeping()) {
                user.wakeUp(true, true);
            }

        } else if (world.getRegistryKey().equals(World.NETHER)) {
            this.lastLocation.put(user.getUuid(), user.getBlockPos());
            this.tpEnd(user);
            return;
        } else if (world.getRegistryKey().equals(World.END)) {
            RegistryKey<World> type = World.OVERWORLD;
            targetWorld = user.getServer().getWorld(type);
            blockPos = user.getBlockPos();
            if (this.lastLocation.containsKey(user.getUuid())) {
                blockPos = (BlockPos) this.lastLocation.get(user.getUuid());
            }

            targetWorld.setBlockState(blockPos, Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ()), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() + 1, blockPos.getZ()), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ() - 1), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() + 1, blockPos.getZ() - 1), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1), Blocks.AIR.getDefaultState());
            targetWorld.setBlockState(new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ() - 1), Blocks.AIR.getDefaultState());
            blockState = targetWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ()));
            if (blockState.isAir() || blockState.getBlock().equals(Blocks.LAVA)) {
                targetWorld.setBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ()), type.equals(World.NETHER) ? Blocks.NETHERRACK.getDefaultState() : Blocks.STONE.getDefaultState());
            }

            if (targetWorld.getBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ())).isAir() || targetWorld.getBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ())).getBlock().equals(Blocks.LAVA)) {
                targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ()), type.equals(World.NETHER) ? Blocks.NETHERRACK.getDefaultState() : Blocks.STONE.getDefaultState());
            }

            if (targetWorld.getBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ() - 1)).isAir() || targetWorld.getBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ() - 1)).getBlock().equals(Blocks.LAVA)) {
                targetWorld.setBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ() - 1), type.equals(World.NETHER) ? Blocks.NETHERRACK.getDefaultState() : Blocks.STONE.getDefaultState());
            }

            if (targetWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ() - 1)).isAir() || targetWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ() - 1)).getBlock().equals(Blocks.LAVA)) {
                targetWorld.setBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ() - 1), type.equals(World.NETHER) ? Blocks.NETHERRACK.getDefaultState() : Blocks.STONE.getDefaultState());
            }

            chunkPos = new ChunkPos(blockPos);
            targetWorld.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, user.getId());
            user.stopRiding();
            if (user.isSleeping()) {
                user.wakeUp(true, true);
            }
        }

        ServerWorld finalTargetWorld = targetWorld;
        ((ServerPlayerEntity) user).teleport(targetWorld, blockPos.getX(), (double) blockPos.getY(), (double) blockPos.getZ(), user.getHeadYaw(), 0.0F);
        BionicUtils.sendEntityStatus(finalTargetWorld, user, ImmortalMobs.IDENTIFIER, 4);
    }

    public void tpEnd(PlayerEntity player) {
        RegistryKey<World> registryKey = World.END;
        ServerWorld serverWorld = ((ServerWorld) player.getEntityWorld()).getServer().getWorld(registryKey);
        if (serverWorld == null) {
            return;
        }

        player.moveToWorld(serverWorld);
        BionicUtils.sendEntityStatus(serverWorld, player, ImmortalMobs.IDENTIFIER, 4);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eUses the power of §dImmortality §eto"));
        tooltip.add(Text.of("§eteleport you"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
