package de.takacick.illegalwars.registry.block;

import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.illegalwars.registry.block.entity.CyberWardenSecurityTrialSpawnerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrialSpawnerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Spawner;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CyberWardenSecurityTrialSpawner extends TrialSpawnerBlock {

    public static EntityType<?> ENTITY_TYPE = EntityRegistry.CYBER_WARDEN_SECURITY;

    public CyberWardenSecurityTrialSpawner(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CyberWardenSecurityTrialSpawnerBlockEntity(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof Spawner spawner) {
                spawner.setEntityType(ENTITY_TYPE, world.getRandom());
            }
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World w, BlockState blockSate, BlockEntityType<T> type) {
        BlockEntityTicker<T> blockEntityTicker;
        if (w instanceof ServerWorld serverWorld) {
            blockEntityTicker = validateTicker(type, EntityRegistry.CYBER_WARDEN_SECURITY_TRIAL_SPAWNER, (world, pos, state, blockEntity) -> blockEntity.getSpawner().tickServer(serverWorld, pos));
        } else {
            blockEntityTicker = validateTicker(type, EntityRegistry.CYBER_WARDEN_SECURITY_TRIAL_SPAWNER, (world, pos, state, blockEntity) -> blockEntity.getSpawner().tickClient(world, pos));
        }
        return blockEntityTicker;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {

    }
}
