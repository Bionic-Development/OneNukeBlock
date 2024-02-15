package de.takacick.secretgirlbase.mixin.leadcuffs;

import de.takacick.secretgirlbase.access.LeadCuffProperties;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    @Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
    public void shouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValue()) {
            return;
        }

        if (entity instanceof LeadCuffProperties leadCuffProperties) {
            if (leadCuffProperties.getLeadCuffedOwner() != null) {
                info.setReturnValue(frustum.isVisible(leadCuffProperties.getLeadCuffedOwner().getVisibilityBoundingBox()));
            }

            if (info.getReturnValue()) {
                return;
            }

            if (leadCuffProperties.getLeadCuffedTarget() != null) {
                info.setReturnValue(frustum.isVisible(leadCuffProperties.getLeadCuffedTarget().getVisibilityBoundingBox()));
            }
        }
    }
}