package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.registry.block.entity.SpikedBedBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Redirect(method = "wakeSleepingPlayers", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"))
    public <T extends LivingEntity> Stream<T> wakeSleepingPlayers(Stream<T> instance, Predicate<? super T> predicate) {
        return instance.filter(LivingEntity::isSleeping).filter(entity -> !(entity.getSleepingPosition().isPresent()
                && entity.getWorld().getBlockEntity(entity.getSleepingPosition().get()) instanceof SpikedBedBlockEntity));
    }
}

