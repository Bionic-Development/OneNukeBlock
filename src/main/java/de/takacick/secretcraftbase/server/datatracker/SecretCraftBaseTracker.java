package de.takacick.secretcraftbase.server.datatracker;

import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTrackerHandleRegistry;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SecretCraftBaseTracker {

    public static final TrackedDataHandler<BezierCurve> BEZIER_CURVE = new TrackedDataHandler<>() {
        public void write(PacketByteBuf packetByteBuf, BezierCurve colorManager) {
            packetByteBuf.writeNbt(colorManager.writeNbt(new NbtCompound()));
        }

        public BezierCurve read(PacketByteBuf packetByteBuf) {
            NbtCompound nbt = packetByteBuf.readNbt();
            return BezierCurve.fromNbt(nbt);
        }

        public BezierCurve copy(BezierCurve bezierCurve) {
            return BezierCurve.fromNbt(bezierCurve.writeNbt(new NbtCompound()));
        }
    };
    public static final TrackedDataHandler<SchematicBox> SCHEMATIC_BOX = new TrackedDataHandler<>() {
        public void write(PacketByteBuf packetByteBuf, SchematicBox schematicBox) {
            packetByteBuf.writeNbt(schematicBox.writeNbt(new NbtCompound()));
        }

        public SchematicBox read(PacketByteBuf packetByteBuf) {
            NbtCompound nbt = packetByteBuf.readNbt();
            return SchematicBox.fromNbt(nbt);
        }

        public SchematicBox copy(SchematicBox schematicBox) {
            return SchematicBox.fromNbt(schematicBox.writeNbt(new NbtCompound()));
        }
    };

    public static void register() {
        BionicDataTrackerHandleRegistry.register(new Identifier(BionicUtils.MOD_ID, "bezier_curve_handler"), BEZIER_CURVE);
        BionicDataTrackerHandleRegistry.register(new Identifier(BionicUtils.MOD_ID, "schematic_box_handler"), SCHEMATIC_BOX);
    }
}
