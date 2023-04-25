package de.takacick.everythinghearts.mixin;

import com.mojang.authlib.GameProfile;
import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.PlayerProperties;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

    private static final Identifier HEART_BIONIC = new Identifier(EverythingHearts.MOD_ID, "textures/entity/heart_bionic.png");

    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    public void getModel(CallbackInfoReturnable<String> info) {
        if (((PlayerProperties) this).isHeart()) {
            if (getUuid().equals(UUID.fromString("265201db-bdfb-47c5-8a4a-d1c41b83b39b"))) {
                info.setReturnValue("slim");
            }
        }
    }

    @Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
    public void getSkinTexture(CallbackInfoReturnable<Identifier> info) {
        if (((PlayerProperties) this).isHeart()) {
            if (getUuid().equals(UUID.fromString("265201db-bdfb-47c5-8a4a-d1c41b83b39b"))) {
                info.setReturnValue(HEART_BIONIC);
            }
        }
    }
}

