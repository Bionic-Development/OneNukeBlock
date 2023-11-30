package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.access.PlayerListProperties;
import de.takacick.elementalblock.access.PlayerProperties;
import de.takacick.utils.data.BionicTrackedData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ClientLivingEntityMixin extends Entity {

    public ClientLivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onTrackedDataSet", at = @At(value = "RETURN"))
    private void onTrackedDataSet(TrackedData<?> data, CallbackInfo info) {
        if (this instanceof PlayerProperties playerProperties) {
            if (data instanceof BionicTrackedData bionicTrackedData
                    && bionicTrackedData.getIdentifier().equals(new Identifier(OneElementalBlock.MOD_ID, "lava_bionic"))) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.getNetworkHandler() != null) {
                    PlayerListEntry playerListEntry = client.getNetworkHandler()
                            .getPlayerListEntry(uuid);
                    if (playerListEntry instanceof PlayerListProperties playerListProperties) {
                        playerListProperties.setLavaBionic(playerProperties.isLavaBionic());
                    }
                }
            }
        }
    }
}