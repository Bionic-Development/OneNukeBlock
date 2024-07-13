package de.takacick.onegirlboyblock.utils.data.item;

import de.takacick.onegirlboyblock.registry.entity.projectiles.TetrisEntity;
import de.takacick.utils.item.animation.ItemAnimationHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.Random;

public class BitCannonItemHelper extends ItemAnimationHelper {

    private TetrisEntity.Variant variant = TetrisEntity.Variant.values()[Random.create().nextInt(TetrisEntity.Variant.values().length)];
    private boolean dirty;

    public BitCannonItemHelper() {
        super(25);
    }

    public void tick(LivingEntity livingEntity, ItemStack itemStack) {
        this.prevTick = tick;
        boolean shouldRun = livingEntity != null && livingEntity.isUsingItem()
                && livingEntity.getActiveItem().equals(itemStack);

        if (shouldRun) {
            this.running = true;
            this.tick = Math.min(this.tick + 1, getMaxTicks());

            if (this.tick >= getMaxTicks()) {
                if (!livingEntity.getWorld().isClient) {
                    this.tick = 0;
                    this.prevTick = 0;
                    setDirty(true);
                }
            }
        } else {
            this.prevTick = 0;
            this.tick = 0;
            this.running = false;
        }

        super.tick(livingEntity, itemStack);
    }

    @Override
    public void pauseAnimation() {
        this.prevTick = tick;
        if (this.tick > 0) {
            this.tick = (int) (this.tick * 0.01f);
        }

        super.pauseAnimation();
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public void setPrevTick(int prevTick) {
        this.prevTick = prevTick;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setVariant(TetrisEntity.Variant variant) {
        this.variant = variant;
    }

    public TetrisEntity.Variant getVariant() {
        return this.variant;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {

        nbt.putInt("variant", this.variant.getId());

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        this.variant = TetrisEntity.Variant.byId(nbt.getInt("variant"));

        super.readNbt(nbt);
    }

    public BitCannonItemHelper copy() {
        BitCannonItemHelper cannonItemHelper = new BitCannonItemHelper();
        cannonItemHelper.tick = this.tick;
        cannonItemHelper.prevTick = this.prevTick;
        cannonItemHelper.dirty = this.dirty;

        return cannonItemHelper;
    }

    public static BitCannonItemHelper from(NbtCompound nbt) {
        BitCannonItemHelper itemHelper = new BitCannonItemHelper();
        itemHelper.readNbt(nbt);
        return itemHelper;
    }
}
