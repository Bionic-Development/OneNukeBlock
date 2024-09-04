package de.takacick.onenukeblock.utils.data.attachments;

import de.takacick.onenukeblock.registry.block.fluid.NuclearWaterFluid;
import de.takacick.onenukeblock.utils.Mutation;
import de.takacick.utils.data.attachment.SynchronizableAttachment;
import de.takacick.utils.data.codec.NbtSerializable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class NuclearMutations extends SynchronizableAttachment implements NbtSerializable {

    private List<Mutation> mutations = new ArrayList<>();
    private int mutationTick = 0;
    private boolean insideNuclearWater = false;

    public NuclearMutations() {

    }

    public void tick(LivingEntity livingEntity) {
        this.insideNuclearWater = livingEntity.getFluidHeight(NuclearWaterFluid.NUCLEAR_WATER) > 0;

        this.mutations.forEach(mutation -> mutation.tick(livingEntity, this.insideNuclearWater, this.mutationTick));
        this.mutations.removeIf(mutation -> mutation.getAge() <= 0);

        if (this.insideNuclearWater) {
            this.mutationTick++;

            if (!livingEntity.getWorld().isClient) {
                boolean canMutate = this.mutations.size() < this.mutationTick / 20;

                if (canMutate && livingEntity.getRandom().nextDouble() <= 0.2 && this.mutations.size() <= 10) {
                    this.mutations.add(new Mutation(livingEntity.getRandom().nextLong(), 0));
                }
            }
        } else {
            this.mutationTick -= 2;
        }

        this.mutationTick = MathHelper.clamp(this.mutationTick, 0, 600);

        setDirty(true);
    }

    public List<Mutation> getMutations() {
        return this.mutations;
    }

    public boolean shouldRemove() {
        return this.mutations.isEmpty() && this.mutationTick <= 0;
    }

    public boolean isInsideNuclearWater() {
        return this.insideNuclearWater;
    }

    public static NuclearMutations from(NbtCompound nbt) {
        NuclearMutations animationHelper = new NuclearMutations();
        animationHelper.readNbt(nbt);
        return animationHelper;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbtCompound) {
        if (!this.mutations.isEmpty()) {
            NbtList list = new NbtList();

            this.mutations.forEach(mutation -> {
                NbtCompound compound = new NbtCompound();
                mutation.write(compound);
                list.add(compound);
            });

            nbtCompound.put("mutations", list);
        }

        nbtCompound.putInt("mutationTick", this.mutationTick);

        return nbtCompound;
    }

    @Override
    public void readNbt(NbtCompound nbtCompound) {
        if (nbtCompound.contains("mutations", NbtElement.LIST_TYPE)) {
            nbtCompound.getList("mutations", NbtElement.COMPOUND_TYPE).forEach(nbtElement -> {
                if (nbtElement instanceof NbtCompound compound) {
                    Mutation mutation = Mutation.read(compound);
                    this.mutations.add(mutation);
                }
            });
        }

        this.mutationTick = nbtCompound.getInt("mutationTick");
    }

    public void read(Object attachment) {
        if (attachment instanceof NuclearMutations nuclearMutations) {

            this.mutationTick = nuclearMutations.mutationTick;

            List<Mutation> mutations = new ArrayList<>(nuclearMutations.mutations);

            mutations.forEach(mutation -> {
                this.mutations.stream().filter(old -> old.equals(mutation)).forEach(old -> {
                    mutation.setPrevAge(old.getPrevAge());
                    if (MathHelper.abs(old.getAge() - mutation.getAge()) <= 1) {
                        mutation.setAge(old.getAge());
                    }
                });
            });

            this.mutations = mutations;
        }
    }
}
