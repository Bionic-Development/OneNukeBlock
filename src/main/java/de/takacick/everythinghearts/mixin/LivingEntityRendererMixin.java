package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.LivingProperties;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Redirect(method = "getRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;"))
    private Identifier getRenderLayer(LivingEntityRenderer instance, Entity entity) {

        Identifier identifier = instance.getTexture(entity);
        if (((LivingProperties) entity).isHeartInfected() && EverythingHearts.HEART_TOUCH_ENTITIES.containsValue(entity.getType()) && Registry.ENTITY_TYPE.getId(entity.getType()).getNamespace().equals("minecraft")) {
            return new Identifier("everythinghearts", identifier.getPath());
        }

        return identifier;
    }


}
