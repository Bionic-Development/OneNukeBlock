package de.takacick.deathmoney.mixin;

import com.mojang.authlib.GameProfile;
import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.registry.ItemRegistry;
import de.takacick.deathmoney.registry.screen.DeathNoteScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    @Shadow
    @Final
    protected MinecraftClient client;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    private void dropSelectedItem(CallbackInfoReturnable<Boolean> info) {
        if (this instanceof PlayerProperties playerProperties) {
            if (playerProperties.getHeartRemovalState().isRunning()) {
                info.setReturnValue(false);
            }
        }
    }


    @Inject(method = "useBook", at = @At("HEAD"), cancellable = true)
    public void useBook(ItemStack book, Hand hand, CallbackInfo info) {
        if (book.isOf(ItemRegistry.DEATH_NOTE)) {
            this.client.setScreen(new DeathNoteScreen(this, book, hand));
            info.cancel();
        }
    }
}