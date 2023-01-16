package de.takacick.imagineanything.registry.item;

import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.entity.custom.CaveConduitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ImaginedCaveExtractor extends Item {

    public ImaginedCaveExtractor(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Vec3d vec3d = user.getRotationVector().multiply(2, 1, 2).add(user.getX(), user.getY(), user.getZ());
        user.getEntityWorld().playSound(null, vec3d.getX(), vec3d.getY(), vec3d.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.NEUTRAL, 1f, 3f);

        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            CaveConduitEntity caveConduitEntity = new CaveConduitEntity(EntityRegistry.CAVE_CONDUIT, world);
            caveConduitEntity.setPos(vec3d.getX(), vec3d.getY() + 3, vec3d.getZ());
            caveConduitEntity.setGlowing(true);
            world.spawnEntity(caveConduitEntity);
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return super.use(world, user, hand);
    }
}
