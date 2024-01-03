package de.takacick.raidbase.mixin;

import de.takacick.raidbase.access.CameraProperties;
import de.takacick.raidbase.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(Camera.class)
@Implements({@Interface(iface = CameraProperties.class, prefix = "raidbase$")})
public abstract class CameraMixin {

    @Shadow
    private Vec3d pos;

    @Shadow
    public abstract Camera.Projection getProjection();

    @Shadow
    private BlockView area;

    @Unique
    private boolean raidbase$insideGlitchyQuicksand = false;

    @Inject(method = "getSubmersionType", at = @At("RETURN"), cancellable = true)
    private void getSubmersionType(CallbackInfoReturnable<CameraSubmersionType> info) {
        this.raidbase$insideGlitchyQuicksand = false;
        if (info.getReturnValue().equals(CameraSubmersionType.NONE) && this.area != null) {
            Camera.Projection projection = this.getProjection();
            List<Vec3d> list = Arrays.asList(projection.getPosition(0, 0), projection.getBottomRight(), projection.getTopRight(), projection.getBottomLeft(), projection.getTopLeft());
            for (Vec3d vec3d : list) {
                Vec3d vec3d2 = this.pos.add(vec3d);
                BlockPos blockPos = BlockPos.ofFloored(vec3d2);
                BlockState blockState = this.area.getBlockState(blockPos);
                if (!blockState.isOf(ItemRegistry.GLITCHY_QUICKSAND)) continue;
                info.setReturnValue(CameraSubmersionType.POWDER_SNOW);
                this.raidbase$insideGlitchyQuicksand = true;
                break;
            }
        }
    }

    public boolean raidbase$isInsideGlitchyQuicksand() {
        return this.raidbase$insideGlitchyQuicksand;
    }

}