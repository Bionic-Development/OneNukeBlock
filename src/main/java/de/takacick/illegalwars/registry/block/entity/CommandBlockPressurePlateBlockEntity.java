package de.takacick.illegalwars.registry.block.entity;

import de.takacick.illegalwars.registry.EntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.CommandBlockExecutor;

public class CommandBlockPressurePlateBlockEntity extends BlockEntity {

    private final CommandBlockExecutor commandExecutor = new CommandBlockExecutor() {

        @Override
        public void setCommand(String command) {
            super.setCommand(command);
            this.markDirty();
        }

        @Override
        public ServerWorld getWorld() {
            return (ServerWorld) CommandBlockPressurePlateBlockEntity.this.world;
        }

        @Override
        public void markDirty() {
            BlockState blockState = this.getWorld().getBlockState(CommandBlockPressurePlateBlockEntity.this.getPos());
            this.getWorld().updateListeners(CommandBlockPressurePlateBlockEntity.this.getPos(), blockState, blockState, Block.NOTIFY_ALL);
        }

        @Override
        public Vec3d getPos() {
            return Vec3d.ofCenter(CommandBlockPressurePlateBlockEntity.this.pos);
        }

        @Override
        public ServerCommandSource getSource() {
            return new ServerCommandSource(this, Vec3d.ofCenter(CommandBlockPressurePlateBlockEntity.this.pos), new Vec2f(0.0f, 0.0f), this.getWorld(), 2, this.getCustomName().getString(), this.getCustomName(), this.getWorld().getServer(), null);
        }

        @Override
        public boolean isEditable() {
            return !CommandBlockPressurePlateBlockEntity.this.isRemoved();
        }
    };

    public CommandBlockPressurePlateBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.COMMAND_BLOCK_PRESSURE_PLATE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        this.commandExecutor.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.commandExecutor.readNbt(nbt);
    }

    @Override
    public boolean copyItemDataRequiresOperator() {
        return true;
    }

    public CommandBlockExecutor getCommandExecutor() {
        return this.commandExecutor;
    }

    public void updateCommandBlock() {

    }
}
