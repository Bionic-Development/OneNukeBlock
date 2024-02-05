package de.takacick.secretcraftbase.server.datatracker;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SchematicBox {

    private Vec3d min;
    private Vec3d max;
    private Vec3d center;
    private float sizeProgress;

    public SchematicBox() {
        this(new Vec3d(0, -100, 0), new Vec3d(0, -100, 0));
    }

    public SchematicBox(BlockPos pos1, BlockPos pos2) {
        this(Vec3d.of(pos1), Vec3d.of(pos2));
    }

    public SchematicBox(Vec3d pos1, Vec3d pos2) {
        this.min = new Vec3d(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()))
                .add(-0.5, -0.5, -0.5);
        this.max = new Vec3d(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()))
                .add(0.5, 0.5, 0.5);
        this.center = getCenter(this.min, this.max);
        this.sizeProgress = 0f;
    }

    public Vec3d getMin() {
        return this.min;
    }

    public Vec3d getCenter() {
        return this.center;
    }

    public Vec3d getMax() {
        return this.max;
    }

    public float getSizeProgress() {
        return this.sizeProgress;
    }

    public void setSizeProgress(float sizeProgress) {
        this.sizeProgress = sizeProgress;
    }

    public NbtCompound writeNbt(NbtCompound nbtCompound) {
        nbtCompound.put("min", writeVec3d(this.min));
        nbtCompound.put("max", writeVec3d(this.max));
        nbtCompound.putFloat("sizeProgress", this.sizeProgress);

        return nbtCompound;
    }

    public void readNbt(NbtCompound nbtCompound) {
        this.min = readVec3d(nbtCompound.getCompound("min"));
        this.max = readVec3d(nbtCompound.getCompound("max"));
        this.sizeProgress = nbtCompound.getFloat("sizeProgress");
        this.center = getCenter(this.min, this.max);
    }

    public static SchematicBox fromNbt(NbtCompound nbtCompound) {
        SchematicBox schematicBox = new SchematicBox();
        schematicBox.readNbt(nbtCompound);
        return schematicBox;
    }

    private static NbtCompound writeVec3d(Vec3d vec3d) {
        NbtCompound nbtCompound = new NbtCompound();

        nbtCompound.putDouble("x", vec3d.getX());
        nbtCompound.putDouble("y", vec3d.getY());
        nbtCompound.putDouble("z", vec3d.getZ());

        return nbtCompound;
    }

    private static Vec3d readVec3d(NbtCompound nbtCompound) {
        double x = nbtCompound.getDouble("x");
        double y = nbtCompound.getDouble("y");
        double z = nbtCompound.getDouble("z");

        return new Vec3d(x, y, z);
    }


    private static Vec3d getCenter(Vec3d min, Vec3d max) {
        double x = max.getX() - min.getX();
        double y = max.getY() - min.getY();
        double z = max.getZ() - min.getZ();

        return new Vec3d(
                max.getX() - x / 2,
                max.getY() - y / 2,
                max.getZ() - z / 2
        );
    }
}
