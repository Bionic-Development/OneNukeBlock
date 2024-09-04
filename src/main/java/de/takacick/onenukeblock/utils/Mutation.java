package de.takacick.onenukeblock.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class Mutation {

    private final long seed;
    private int age;
    private int prevAge;

    public Mutation(long seed, int age) {
        this.seed = seed;
        this.age = age;
    }

    public void tick(LivingEntity livingEntity, boolean insideNuclearWater, int mutationTick) {
        this.prevAge = this.age;
        if (insideNuclearWater) {
            this.age = Math.min(this.age + 1, 120);
        } else if (mutationTick > 0) {
            this.age = Math.max(this.age - 1, 0);
        }
    }

    public void setPrevAge(int prevAge) {
        this.prevAge = prevAge;
    }

    public int getPrevAge() {
        return this.prevAge;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }

    public float getAge(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevAge, this.age);
    }

    public long getSeed() {
        return this.seed;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Mutation mutation && mutation.seed == this.seed;
    }

    public NbtCompound write(NbtCompound nbtCompound) {
        nbtCompound.putLong("seed", this.seed);
        nbtCompound.putInt("age", this.age);
        nbtCompound.putInt("prevAge", this.prevAge);

        return nbtCompound;
    }

    public static Mutation read(NbtCompound nbtCompound) {
        long seed = nbtCompound.getLong("seed");
        int age = nbtCompound.getInt("age");
        int prevAge = nbtCompound.getInt("prevAge");
        Mutation mutation = new Mutation(seed, age);
        mutation.setPrevAge(prevAge);
        return mutation;
    }
}