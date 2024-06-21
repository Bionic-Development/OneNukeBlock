package de.takacick.onescaryblock.utils.datatracker;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class OneScaryBlockDataTracker {
    public static final TrackedDataHandler<BloodBorderSuitHelper> BLOOD_BARRIER_SUIT = new TrackedDataHandler<>() {
        public void write(PacketByteBuf packetByteBuf, BloodBorderSuitHelper bloodBorderSuitHelper) {
            packetByteBuf.writeNbt(bloodBorderSuitHelper.writeNbt(new NbtCompound()));
        }

        public BloodBorderSuitHelper read(PacketByteBuf packetByteBuf) {
            NbtCompound nbt = packetByteBuf.readNbt();
            return BloodBorderSuitHelper.fromNbt(nbt);
        }

        public BloodBorderSuitHelper copy(BloodBorderSuitHelper bloodBorderSuitHelper) {
            return BloodBorderSuitHelper.copy(bloodBorderSuitHelper);
        }
    };
    public static final TrackedDataHandler<SoulFragmentHelper> SOUL_FRAGMENT = new TrackedDataHandler<>() {
        public void write(PacketByteBuf packetByteBuf, SoulFragmentHelper soulFragmentHelper) {
            packetByteBuf.writeNbt(soulFragmentHelper.writeNbt(new NbtCompound()));
        }

        public SoulFragmentHelper read(PacketByteBuf packetByteBuf) {
            NbtCompound nbt = packetByteBuf.readNbt();
            return SoulFragmentHelper.fromNbt(nbt);
        }

        public SoulFragmentHelper copy(SoulFragmentHelper soulFragmentHelper) {
            return SoulFragmentHelper.copy(soulFragmentHelper);
        }
    };
    public static final TrackedDataHandler<HerobrineLightningDamageHelper> HEROBRINE_LIGHTNING = new TrackedDataHandler<>() {
        public void write(PacketByteBuf packetByteBuf, HerobrineLightningDamageHelper herobrineLightningDamageHelper) {
            packetByteBuf.writeNbt(herobrineLightningDamageHelper.writeNbt(new NbtCompound()));
        }

        public HerobrineLightningDamageHelper read(PacketByteBuf packetByteBuf) {
            NbtCompound nbt = packetByteBuf.readNbt();
            return HerobrineLightningDamageHelper.fromNbt(nbt);
        }

        public HerobrineLightningDamageHelper copy(HerobrineLightningDamageHelper herobrineLightningDamageHelper) {
            return HerobrineLightningDamageHelper.copy(herobrineLightningDamageHelper);
        }
    };

    public static void register() {
        TrackedDataHandlerRegistry.register(BLOOD_BARRIER_SUIT);
        TrackedDataHandlerRegistry.register(SOUL_FRAGMENT);
        TrackedDataHandlerRegistry.register(HEROBRINE_LIGHTNING);
    }
}
