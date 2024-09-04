package de.takacick.onenukeblock.utils.data.item;

import com.mojang.serialization.Codec;
import de.takacick.onenukeblock.utils.data.AttachmentTypes;
import de.takacick.utils.data.codec.NbtCodecs;
import de.takacick.utils.item.animation.ItemAnimationHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.MathHelper;

public class BangMaceItemHelper extends ItemAnimationHelper {

    public static final Codec<BangMaceItemHelper> CODEC = NbtCodecs.createCodec(BangMaceItemHelper::from);
    public static final PacketCodec<RegistryByteBuf, BangMaceItemHelper> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    private boolean dirty;

    public BangMaceItemHelper() {
        super(-1);
    }

    public void tick(LivingEntity livingEntity, ItemStack itemStack) {
        var animation = livingEntity == null ? null : livingEntity.getAttached(AttachmentTypes.BANG_MACE);
        if (animation != null && animation.isFused() && itemStack.equals(livingEntity.getActiveItem())) {
            this.running = true;
            this.tick = (int) animation.getFuse(1f);
            this.prevTick = (int) animation.getFuse(0f);
        } else {
            this.prevTick = 0;
            this.tick = 0;
            this.running = false;
        }

        super.tick(livingEntity, itemStack);
    }

    public float getTick(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevTick, this.tick);
    }

    public boolean isFused() {
        return this.running;
    }

    @Override
    public void pauseAnimation() {
        this.prevTick = tick;
        if (this.tick > 0) {
            this.tick = 0;
            this.prevTick = 0;
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


    public boolean isDirty() {
        return this.dirty;
    }

    public BangMaceItemHelper copy() {
        BangMaceItemHelper helper = new BangMaceItemHelper();
        helper.tick = this.tick;
        helper.prevTick = this.prevTick;
        helper.dirty = this.dirty;

        return helper;
    }

    public static BangMaceItemHelper from(NbtCompound nbt) {
        BangMaceItemHelper itemHelper = new BangMaceItemHelper();
        itemHelper.readNbt(nbt);
        return itemHelper;
    }
}
