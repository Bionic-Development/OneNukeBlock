package de.takacick.onenukeblock.registry.particles.goop;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.Vec3d;

public class Vec3dPacketCodec {

    public static final PacketCodec<ByteBuf, Vec3d> CODEC = new PacketCodec<>() {

        @Override
        public Vec3d decode(ByteBuf byteBuf) {
            return readVector3f(byteBuf);
        }

        @Override
        public void encode(ByteBuf byteBuf, Vec3d vector3f) {
            writeVector3f(byteBuf, vector3f);
        }
    };

    private static Vec3d readVector3f(ByteBuf buf) {
        return new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    private static void writeVector3f(ByteBuf buf, Vec3d vector) {
        buf.writeDouble(vector.getX());
        buf.writeDouble(vector.getY());
        buf.writeDouble(vector.getZ());
    }
}