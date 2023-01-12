package de.takacick.immortalmobs.registry.entity.living.model;

import de.takacick.immortalmobs.registry.entity.living.ImmortalSheepEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;

@Environment(value = EnvType.CLIENT)
public class ImmortalSheepEntityModel<T extends ImmortalSheepEntity>
        extends QuadrupedEntityModel<T> {
    private float headPitchModifier;

    public ImmortalSheepEntityModel(ModelPart root) {
        super(root, false, 8.0f, 4.0f, 2.0f, 2.0f, 24);
    }

    @Override
    public void animateModel(T sheepEntity, float f, float g, float h) {
        super.animateModel(sheepEntity, f, g, h);
        this.head.pivotY = 6.0f + sheepEntity.getNeckAngle(h) * 9.0f;
        this.headPitchModifier = sheepEntity.getHeadAngle(h);
    }

    @Override
    public void setAngles(T sheepEntity, float f, float g, float h, float i, float j) {
        super.setAngles(sheepEntity, f, g, h, i, j);
        this.head.pitch = this.headPitchModifier;
    }
}

