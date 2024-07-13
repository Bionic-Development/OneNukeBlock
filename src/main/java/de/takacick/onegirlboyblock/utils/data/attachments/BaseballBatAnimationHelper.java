package de.takacick.onegirlboyblock.utils.data.attachments;

import de.takacick.onegirlboyblock.registry.ItemRegistry;
import de.takacick.utils.data.attachment.AnimationSyncHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BaseballBatAnimationHelper extends AnimationSyncHelper<LivingEntity> {

    private float rotation = 0f;
    private float prevRotation = 0f;
    private float rotationSpeed = 0.5f;

    public BaseballBatAnimationHelper() {
        super(-1);
    }

    @Override
    public void tick(LivingEntity livingEntity) {
        this.prevTick = this.tick;

        World world = livingEntity.getWorld();
        ItemStack activeStack = livingEntity.getActiveItem();

        if (!world.isClient && livingEntity.isUsingItem() && activeStack.isOf(ItemRegistry.BASEBALL_BAT)) {
            this.running = true;
        } else if (!(livingEntity.getWorld().isClient && this.running)) {
            this.rotationSpeed = 0.5f;
            this.rotation = 0f;
            this.prevRotation = 0f;
            this.running = false;
        }

        if (this.running) {
            this.tick++;
            this.rotationSpeed = MathHelper.clamp(this.rotationSpeed + 0.01f, 0f, 2f);
            this.prevRotation = this.rotation;
            this.rotation += this.rotationSpeed;

            setDirty(true);
        } else {
            this.tick = 0;
            this.prevTick = 0;
            setDirty(true);
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putFloat("rotation", this.rotation);
        nbt.putFloat("prevRotation", this.prevRotation);
        nbt.putFloat("rotationSpeed", this.rotationSpeed);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.rotation = nbt.getFloat("rotation");
        this.prevRotation = nbt.getFloat("prevRotation");
        this.rotationSpeed = nbt.getFloat("rotationSpeed");
        super.readNbt(nbt);
    }

    public float getRotation(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevRotation, this.rotation);
    }

    @Override
    public void sync(AnimationSyncHelper<?> animationHelper) {
        if (animationHelper instanceof BaseballBatAnimationHelper baseballBatAnimationHelper) {
            if (Math.abs(baseballBatAnimationHelper.rotationSpeed - this.rotationSpeed) > 0.2) {
                this.rotationSpeed = baseballBatAnimationHelper.rotationSpeed;
            }

            if (Math.abs(baseballBatAnimationHelper.rotation - this.rotation) > this.rotationSpeed * 2) {
                this.rotation = baseballBatAnimationHelper.rotation;
                this.prevRotation = baseballBatAnimationHelper.prevRotation;
            }
        }
        if (Math.abs(animationHelper.getTick() - this.tick) > 2) {
            this.tick = animationHelper.getTick();
            this.prevTick = animationHelper.getPrevTick();
        }

        super.sync(animationHelper);
    }

    public static BaseballBatAnimationHelper from(NbtCompound nbt) {
        BaseballBatAnimationHelper animationHelper = new BaseballBatAnimationHelper();
        animationHelper.readNbt(nbt);
        return animationHelper;
    }
}
