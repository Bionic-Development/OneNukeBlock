package de.takacick.secretcraftbase.mixin;

import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.registry.entity.living.SecretPigPoweredPortalEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.JumpControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.MobVisibilityCache;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    @Shadow @Final private MobVisibilityCache visibilityCache;

    @Shadow protected abstract void mobTick();

    @Shadow protected MoveControl moveControl;

    @Shadow protected LookControl lookControl;

    @Shadow protected abstract void sendAiDebugData();

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickNewAi", at = @At("HEAD"), cancellable = true)
    public void tickNewAi(CallbackInfo info) {
        if((Object) this instanceof SecretPigPoweredPortalEntity secretPigPoweredPortalEntity &&  secretPigPoweredPortalEntity.isPowered()) {
            ++this.despawnCounter;
            this.getWorld().getProfiler().push("sensing");
            this.visibilityCache.clear();
            this.getWorld().getProfiler().pop();
            this.getWorld().getProfiler().push("mob tick");
            this.mobTick();
            this.getWorld().getProfiler().pop();
            this.getWorld().getProfiler().push("controls");
            this.getWorld().getProfiler().push("move");
            this.moveControl.tick();
            this.getWorld().getProfiler().swap("look");
            this.lookControl.tick();
            this.getWorld().getProfiler().pop();
            this.getWorld().getProfiler().pop();
            this.sendAiDebugData();
            info.cancel();
        }
    }
}