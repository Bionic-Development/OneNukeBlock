package de.takacick.onenukeblock.mixin.nuclearwater;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AnimalModel.class)
public interface AnimalModelAccessor {

    @Invoker("getHeadParts")
    Iterable<ModelPart> invokeGetHeadParts();

    @Invoker("getBodyParts")
    Iterable<ModelPart> invokeGetBodyParts();

}
