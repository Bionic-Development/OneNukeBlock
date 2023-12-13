package de.takacick.upgradebody.server.datatracker;

import de.takacick.upgradebody.registry.bodypart.BodyPartManager;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTrackerHandleRegistry;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class BodyPartTracker {

    public static final TrackedDataHandler<BodyPartManager> BODY_PARTS = new TrackedDataHandler<>() {
        public void write(PacketByteBuf packetByteBuf, BodyPartManager bodyPartManager) {
            packetByteBuf.writeNbt(BodyPartManager.toNbt(bodyPartManager, new NbtCompound()));
        }

        public BodyPartManager read(PacketByteBuf packetByteBuf) {
            NbtCompound nbt = packetByteBuf.readNbt();
            return BodyPartManager.fromNbt(nbt);
        }

        public BodyPartManager copy(BodyPartManager bodyParts) {
            return new BodyPartManager(bodyParts.getBodyParts(), bodyParts.isUpgrading());
        }
    };

    public static void register() {
        BionicDataTrackerHandleRegistry.register(new Identifier(BionicUtils.MOD_ID, "body_parts"), BODY_PARTS);

    }
}
