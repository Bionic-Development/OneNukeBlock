package de.takacick.illegalwars.mixin.warden;

import com.mojang.datafixers.util.Either;
import net.minecraft.entity.Entity;
import net.minecraft.world.event.EntityPositionSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(EntityPositionSource.class)
public interface EntityPositionSourceAccessor {

    @Accessor
    Either<Entity, Either<UUID, Integer>> getSource();

}