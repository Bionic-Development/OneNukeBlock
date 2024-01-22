package de.takacick.illegalwars.mixin;

import de.takacick.illegalwars.registry.block.entity.CommandBlockPressurePlateBlockEntity;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.c2s.play.UpdateCommandBlockC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.*;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.CommandBlockExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandler
        implements ServerPlayPacketListener,
        PlayerAssociatedNetworkHandler,
        TickablePacketListener {

    @Shadow
    public ServerPlayerEntity player;

    public ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @Inject(method = "onUpdateCommandBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/c2s/play/UpdateCommandBlockC2SPacket;getPos()Lnet/minecraft/util/math/BlockPos;", shift = At.Shift.BEFORE), cancellable = true)
    private void onUpdateCommandBlock(UpdateCommandBlockC2SPacket packet, CallbackInfo info) {
        BlockPos blockPos = packet.getPos();
        if (this.player.getWorld().getBlockEntity(blockPos) instanceof CommandBlockPressurePlateBlockEntity commandBlockEntity) {
            CommandBlockExecutor commandBlockExecutor = commandBlockEntity.getCommandExecutor();

            String string = packet.getCommand();
            boolean bl = packet.shouldTrackOutput();
            if (commandBlockExecutor != null) {
                commandBlockExecutor.setCommand(string);
                commandBlockExecutor.setTrackOutput(bl);
                if (!bl) {
                    commandBlockExecutor.setLastOutput(null);
                }

                    commandBlockEntity.updateCommandBlock();

                commandBlockExecutor.markDirty();
                if (!StringHelper.isEmpty(string)) {
                    this.player.sendMessage(Text.translatable("advMode.setCommand.success", string));
                }
                info.cancel();
            }
        }

    }
}
