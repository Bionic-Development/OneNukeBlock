package de.takacick.secretgirlbase.mixin;

import de.takacick.secretgirlbase.access.PlayerProperties;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "secretgirlbase$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Unique
    private UUID secretgirlbase$phonePlayer;
    @Unique
    private ItemStack secretgirlbase$smokeDrop;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (this.secretgirlbase$phonePlayer != null) {
            nbt.putUuid("secretgirlbase$phonePlayer", this.secretgirlbase$phonePlayer);
        }

        if (this.secretgirlbase$smokeDrop != null) {
            nbt.put("secretgirlbase$smokeDrop", this.secretgirlbase$smokeDrop.writeNbt(new NbtCompound()));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.containsUuid("secretgirlbase$phonePlayer")) {
            this.secretgirlbase$phonePlayer = nbt.getUuid("secretgirlbase$phonePlayer");
        }

        if (nbt.contains("secretgirlbase$smokeDrop", NbtElement.COMPOUND_TYPE)) {
            this.secretgirlbase$smokeDrop = ItemStack.fromNbt(nbt.getCompound("secretgirlbase$smokeDrop"));
        }
    }

    public void secretgirlbase$setPhonePlayer(PlayerEntity playerEntity) {
        if (playerEntity == null) {
            this.secretgirlbase$phonePlayer = null;
        } else {
            this.secretgirlbase$phonePlayer = playerEntity.getUuid();
        }
    }

    public Optional<UUID> secretgirlbase$getPhonePlayer() {
        return Optional.ofNullable(this.secretgirlbase$phonePlayer);
    }

    public void secretgirlbase$setSmokeDrop(ItemStack smokeDrop) {
        if (smokeDrop == null) {
            this.secretgirlbase$smokeDrop = null;
        } else {
            this.secretgirlbase$smokeDrop = smokeDrop.copy();
        }
    }

    public Optional<ItemStack> secretgirlbase$getSmokeDrop() {
        return Optional.ofNullable(this.secretgirlbase$smokeDrop);
    }
}

