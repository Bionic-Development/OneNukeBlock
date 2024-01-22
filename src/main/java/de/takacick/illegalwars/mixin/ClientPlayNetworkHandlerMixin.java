package de.takacick.illegalwars.mixin;

import de.takacick.illegalwars.client.screen.CommandBlockPressurePlateScreen;
import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.illegalwars.registry.block.entity.CommandBlockPressurePlateBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler
        implements TickablePacketListener,
        ClientPlayPacketListener {

    protected ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "onBlockEntityUpdate", at = @At("TAIL"))
    private void onUpdateCommandBlock(BlockEntityUpdateS2CPacket packet, CallbackInfo info) {
        if (packet.getBlockEntityType().equals(EntityRegistry.COMMAND_BLOCK_PRESSURE_PLATE)) {
            BlockPos blockPos = packet.getPos();
            this.client.world.getBlockEntity(blockPos, packet.getBlockEntityType()).ifPresent(blockEntity -> {
                if (blockEntity instanceof CommandBlockPressurePlateBlockEntity && this.client.currentScreen instanceof CommandBlockPressurePlateScreen pressurePlateScreen) {
                    pressurePlateScreen.updateCommandBlock();
                }
            });
        }
    }
}
