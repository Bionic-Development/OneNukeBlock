package de.takacick.onescaryblock.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.Predicate;

public class EntityUtils {

    public static <T extends Entity> Optional<T> getNearbyEntity(Class<T> entityClass, World world, Vec3d pos, double range, Predicate<T> predicate) {
        return world.getEntitiesByClass(entityClass, new Box(pos, pos).expand(range * range), entity -> true)
                .stream()
                .filter(predicate)
                .min((o1, o2) -> (int) (o1.getPos().distanceTo(pos) - o2.getPos()
                        .distanceTo(pos)));
    }


}
