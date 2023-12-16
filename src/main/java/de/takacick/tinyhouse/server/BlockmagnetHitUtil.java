package de.takacick.tinyhouse.server;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public final class BlockmagnetHitUtil {

    public static HitResult getCollision(Entity entity, double range) {
        Vec3d vec3d = entity.getRotationVec(0.0f).multiply(range);
        World world = entity.getWorld();
        Vec3d vec3d2 = entity.getEyePos();
        return BlockmagnetHitUtil.getCollision(vec3d2, entity, vec3d, world, range);
    }

    private static HitResult getCollision(Vec3d pos, Entity entity, Vec3d velocity, World world, double range) {
        EntityHitResult hitResult2;
        Vec3d vec3d = pos.add(velocity);
        HitResult hitResult = world.raycast(new RaycastContext(pos, vec3d, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
        if (hitResult.getType() != HitResult.Type.MISS) {
            vec3d = hitResult.getPos();
        }

        HitResult entityRaycast = entityRaycast(entity, range - 0.1,
                0);

        if (entityRaycast != null && entityRaycast.getPos().distanceTo(entity.getCameraPosVec(1))
                < hitResult.getPos().distanceTo(entity.getCameraPosVec(1))) {
            hitResult = entityRaycast;
        }
        return hitResult;
    }

    private static HitResult entityRaycast(Entity entity, double maxDistance, float tickDelta) {
        Vec3d vec3d = entity.getCameraPosVec(tickDelta);
        Vec3d vec3d2 = entity.getRotationVec(tickDelta);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
        return getEntityCollision(entity, vec3d, vec3d3);
    }

    protected static EntityHitResult getEntityCollision(Entity entity, Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(entity.getWorld(), entity, currentPosition, nextPosition, new Box(currentPosition, nextPosition).expand(0.001),
                target -> !target.isSpectator(), 0);
    }

    @Nullable
    public static EntityHitResult getEntityCollision(World world, Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate) {
        return BlockmagnetHitUtil.getEntityCollision(world, entity, min, max, box, predicate, 0.3f);
    }

    @Nullable
    public static EntityHitResult getEntityCollision(World world, Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, float margin) {
        double d = Double.MAX_VALUE;
        Entity entity2 = null;
        for (Entity entity3 : world.getOtherEntities(entity, box, predicate)) {
            double e;
            Box box2 = entity3.getBoundingBox().expand(margin);
            Optional<Vec3d> optional = box2.raycast(min, max);
            if (!optional.isPresent() || !((e = min.squaredDistanceTo(optional.get())) < d)) continue;
            entity2 = entity3;
            d = e;
        }
        if (entity2 == null) {
            return null;
        }
        return new EntityHitResult(entity2);
    }
}

