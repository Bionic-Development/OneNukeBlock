package de.takacick.secretcraftbase.registry.entity.living.brain;

import de.takacick.secretcraftbase.registry.entity.living.SecretPigPoweredPortalEntity;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

public class PigLookControl
        extends LookControl {

    public PigLookControl(MobEntity entity) {
        super(entity);
    }

    public void tick() {
        if (entity instanceof SecretPigPoweredPortalEntity secretPigPoweredPortalEntity) {
            if (secretPigPoweredPortalEntity.isPowered()) {
                this.entity.setPitch(0.0f);
                return;
            }
        }

        if (this.shouldStayHorizontal()) {
            this.entity.setPitch(0.0f);
        }
        if (this.lookAtTimer > 0) {
            --this.lookAtTimer;
            this.getTargetYaw().ifPresent(yaw -> {
                this.entity.headYaw = this.changeAngle(this.entity.headYaw, yaw.floatValue(), this.maxYawChange);
            });
            if (!(entity instanceof SecretPigPoweredPortalEntity secretPigPoweredPortalEntity && secretPigPoweredPortalEntity.isPowered())) {
                this.getTargetPitch().ifPresent(pitch -> this.entity.setPitch(this.changeAngle(this.entity.getPitch(), pitch.floatValue(), this.maxPitchChange)));
            }
        } else {
            this.entity.headYaw = this.changeAngle(this.entity.headYaw, this.entity.bodyYaw, 10.0f);
        }
        this.clampHeadYaw();
    }
}

