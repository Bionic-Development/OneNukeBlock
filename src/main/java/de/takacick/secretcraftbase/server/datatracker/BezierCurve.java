package de.takacick.secretcraftbase.server.datatracker;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

public class BezierCurve {

    private Vec3d start;
    private Vec3d pos;
    private Vec3d end;

    public BezierCurve() {
        this.start = new Vec3d(0, 0, 0);
        this.pos = new Vec3d(0, 0, 0);
        this.end = new Vec3d(0, 0, 0);
    }

    public BezierCurve(Vec3d start, Vec3d end, boolean placing) {
        this.start = start;
        this.end = end;

        Random random = Random.create();

        Vec3d rot = rotateAroundYAxis(end.subtract(start).normalize(), random.nextBoolean() ? -90 : 90);

        Vec3d pos1 = end.subtract(start)
                .multiply((float) (0.5 + random.nextGaussian() * 0.1))
                .add(start)
                .add(rot.multiply(2 + random.nextDouble() * 3))
                .add((float) (random.nextGaussian() * 0.5), (float) (random.nextGaussian() * 3.5), (float) (random.nextGaussian() * 0.5));

        Vec3d compare = placing ? start : end;

        if (pos1.getY() < compare.getY() + 3) {
            pos1 = pos1.add(0, compare.getY() - pos1.getY() + 2 + random.nextDouble() * 4, 0);
        }
        double x = pos1.getX() - compare.getX();
        double z = pos1.getZ() - compare.getZ();
        if (Math.abs(x) < 1 && Math.abs(z) < 1) {
            pos1 = pos1.add(x, 0, z);
        }

        this.pos = pos1;
    }

    public Vec3d getStart() {
        return start;
    }

    public Vec3d getEnd() {
        return end;
    }

    public Vec3d getPos(float progress) {
        progress = MathHelper.clamp(progress, 0f, 1f);
        double oneMinusT = 1.0 - progress;
        double oneMinusTSquared = oneMinusT * oneMinusT;
        double tSquared = progress * progress;

        double x = oneMinusTSquared * start.x + 2 * oneMinusT * progress * pos.x + tSquared * end.x;
        double y = oneMinusTSquared * start.y + 2 * oneMinusT * progress * pos.y + tSquared * end.y;
        double z = oneMinusTSquared * start.z + 2 * oneMinusT * progress * pos.z + tSquared * end.z;

        return new Vec3d(x, y, z);
    }

    public NbtCompound writeNbt(NbtCompound nbtCompound) {

        nbtCompound.put("start", writeVec3d(this.start));
        nbtCompound.put("pos", writeVec3d(this.pos));
        nbtCompound.put("end", writeVec3d(this.end));

        return nbtCompound;
    }

    public void readNbt(NbtCompound nbtCompound) {
        this.start = readVec3d(nbtCompound.getCompound("start"));
        this.pos = readVec3d(nbtCompound.getCompound("pos"));
        this.end = readVec3d(nbtCompound.getCompound("end"));
    }

    public static BezierCurve fromNbt(NbtCompound nbtCompound) {
        BezierCurve bezierCurve = new BezierCurve();
        bezierCurve.readNbt(nbtCompound);
        return bezierCurve;
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

    private static Vec3d rotateAroundYAxis(Vec3d vec3d, float angleDegrees) {
        Vector3f vec3f = vec3d.toVector3f();
        vec3f.rotateY(angleDegrees);

        return new Vec3d(vec3f.x(), vec3d.getY(), vec3f.z());
    }
}
