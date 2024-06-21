package de.takacick.onescaryblock.utils;

import de.takacick.onescaryblock.OneScaryBlockClient;
import de.takacick.onescaryblock.registry.EntityRegistry;
import de.takacick.onescaryblock.registry.ItemRegistry;
import de.takacick.onescaryblock.registry.ParticleRegistry;
import de.takacick.onescaryblock.registry.entity.custom.ScaryOneBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class ConsumedItem {

    private static final List<BlockSoundGroup> SOUND_GROUPS = List.of(BlockSoundGroup.BONE, BlockSoundGroup.ROOTED_DIRT, BlockSoundGroup.STONE, BlockSoundGroup.SOUL_SAND, BlockSoundGroup.BASALT);

    private UUID uuid = UUID.randomUUID();
    private ItemStack itemStack;
    private int tick = 8;

    public ConsumedItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void tick(World world, BlockPos blockPos) {
        this.tick--;

        Vec3d pos = blockPos.toCenterPos();

        if (world.isClient) {

            if (this.itemStack.isOf(ItemRegistry.ITEM_303)) {
                if(this.tick == 2){
                    world.playSound(pos.getX() , pos.getY(), pos.getZ() , SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 3f, 0.4f + world.getRandom().nextFloat() * 0.2f, false);
                }
                world.playSound(pos.getX() , pos.getY(), pos.getZ() , SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.HOSTILE, 3f, 0.4f + world.getRandom().nextFloat() * 0.2f, false);
                world.playSound(pos.getX() , pos.getY(), pos.getZ() , SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.HOSTILE, 3f, 0.4f + world.getRandom().nextFloat() * 0.2f, false);

                OneScaryBlockClient.addItemBreakParticles(world, blockPos, ParticleRegistry.ITEM_303);
            } else {
                if (this.tick == 7) {
                    world.playSound(pos.getX() , pos.getY(), pos.getZ() , SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.HOSTILE, 3f, 0.4f + world.getRandom().nextFloat() * 0.2f, false);
                    world.playSound(pos.getX() , pos.getY(), pos.getZ() , SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.HOSTILE, 3f, 0.4f + world.getRandom().nextFloat() * 0.2f, false);
                }
                world.playSound(pos.getX() , pos.getY(), pos.getZ() , SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.HOSTILE, 3f, 0.8f + world.getRandom().nextFloat() * 0.2f, false);
                SoundEvent soundEvent;
                if (this.itemStack.getItem() instanceof BlockItem blockItem) {
                    soundEvent = blockItem.getBlock().getDefaultState().getSoundGroup().getBreakSound();
                } else {
                    soundEvent = SOUND_GROUPS.get(world.getRandom().nextInt(SOUND_GROUPS.size())).getBreakSound();
                }
                for (int i = 0; i < world.getRandom().nextBetween(1, 3); i++) {
                    world.playSound(pos.getX() + world.getRandom().nextGaussian() * 0.25, pos.getY() + world.getRandom().nextGaussian() * 0.25, pos.getZ() + world.getRandom().nextGaussian() * 0.25,
                            soundEvent, SoundCategory.HOSTILE, 3f, 0.8f + world.getRandom().nextFloat() * 0.2f, false);
                }

                OneScaryBlockClient.addItemBreakParticles(world, blockPos, new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
            }
        } else {
            if (this.itemStack.isOf(ItemRegistry.ITEM_303)) {
                world.playSound(null, pos.getX() , pos.getY(), pos.getZ() , SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 3f, 0.4f + world.getRandom().nextFloat() * 0.2f);
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                ScaryOneBlockEntity scaryOneBlockEntity = new ScaryOneBlockEntity(EntityRegistry.SCARY_ONE_BLOCK, world);
                scaryOneBlockEntity.refreshPositionAndAngles(pos.getX(), pos.getY() - 0.5, pos.getZ(), 0f, 0f);
                world.spawnEntity(scaryOneBlockEntity);
            }
        }
    }

    public boolean shouldRemove() {
        return this.tick <= 0;
    }

    public int getTick() {
        return this.tick;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putUuid("uuid", this.uuid);
        nbt.putInt("tick", this.tick);
        nbt.put("itemStack", this.itemStack.writeNbt(new NbtCompound()));

        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.getUuid("uuid") != null) {
            this.uuid = nbt.getUuid("uuid");
        }

        if (nbt.contains("tick", NbtElement.INT_TYPE)) {
            this.tick = nbt.getInt("tick");
        } else {
            this.tick = 0;
        }

        if (nbt.contains("itemStack", NbtElement.COMPOUND_TYPE)) {
            this.itemStack = ItemStack.fromNbt(nbt.getCompound("itemStack"));
        }
    }
}
