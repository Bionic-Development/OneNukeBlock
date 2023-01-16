package de.takacick.imagineanything.registry.item;

import com.mojang.authlib.GameProfile;
import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.entity.living.HeadEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

public class HeadItem extends Item {

    public static final String SKULL_OWNER_KEY = "SkullOwner";

    public HeadItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        Vec3d vec3d = user.getRotationVector().multiply(2, 1, 2).add(user.getX(), user.getEyeY(), user.getZ());
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            EntityType<HeadEntity> entityType2 = EntityRegistry.HEAD;
            HeadEntity entity = (HeadEntity) entityType2.spawnFromItemStack((ServerWorld) world, itemStack, user, new BlockPos(vec3d), SpawnReason.SPAWN_EGG, true, false);

            if (entity != null) {
                GameProfile gameProfile = NbtHelper.toGameProfile(itemStack.getOrCreateNbt().getCompound(SKULL_OWNER_KEY));
                entity.setItemStack(itemStack.copy());
                entity.setOwner(user);
                entity.setTamed(true);
                entity.setCustomName(Text.of(gameProfile == null ? "Steve" : gameProfile.getName()));
                itemStack.decrement(1);

                entity.getLookControl().lookAt(user, 100, 100);
                BionicUtils.sendEntityStatus((ServerWorld) entity.getWorld(), entity, ImagineAnything.IDENTIFIER, 3);
            }
        }

        return super.use(world, user, hand);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity user = context.getPlayer();
        if (!(world instanceof ServerWorld)) {
            return ActionResult.SUCCESS;
        } else {
            ItemStack itemStack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            Direction direction = context.getSide();
            BlockState blockState = world.getBlockState(blockPos);

            BlockPos blockPos3;
            if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
                blockPos3 = blockPos;
            } else {
                blockPos3 = blockPos.offset(direction);
            }

            EntityType<HeadEntity> entityType2 = EntityRegistry.HEAD;
            HeadEntity entity = (HeadEntity) entityType2.spawnFromItemStack((ServerWorld) world, itemStack, context.getPlayer(), blockPos3, SpawnReason.SPAWN_EGG, true, false);

            if (entity != null) {
                GameProfile gameProfile = NbtHelper.toGameProfile(itemStack.getOrCreateNbt().getCompound(SKULL_OWNER_KEY));
                entity.setItemStack(itemStack.copy());
                entity.setOwner(user);
                entity.setTamed(true);
                entity.setCustomName(Text.of(gameProfile == null ? "Steve" : gameProfile.getName()));
                entity.getLookControl().lookAt(user, 100, 100);
                itemStack.decrement(1);
                BionicUtils.sendEntityStatus((ServerWorld) entity.getWorld(), entity, ImagineAnything.IDENTIFIER, 3);
            }

            return ActionResult.CONSUME;
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        if (stack.isOf(Items.PLAYER_HEAD) && stack.hasNbt()) {
            NbtCompound nbtCompound2;
            String string = null;
            NbtCompound nbtCompound = stack.getNbt();
            if (nbtCompound.contains(SKULL_OWNER_KEY, NbtElement.STRING_TYPE)) {
                string = nbtCompound.getString(SKULL_OWNER_KEY);
            } else if (nbtCompound.contains(SKULL_OWNER_KEY, NbtElement.COMPOUND_TYPE) && (nbtCompound2 = nbtCompound.getCompound(SKULL_OWNER_KEY)).contains("Name", NbtElement.STRING_TYPE)) {
                string = nbtCompound2.getString("Name");
            }
            if (string != null) {
                return Text.translatable(this.getTranslationKey() + ".named", string);
            }
        }
        return super.getName(stack);
    }

    @Override
    public void postProcessNbt(NbtCompound nbt) {
        super.postProcessNbt(nbt);
        if (nbt.contains(SKULL_OWNER_KEY, NbtElement.STRING_TYPE) && !StringUtils.isBlank(nbt.getString(SKULL_OWNER_KEY))) {
            GameProfile gameProfile = new GameProfile(null, nbt.getString(SKULL_OWNER_KEY));
            SkullBlockEntity.loadProperties(gameProfile, profile -> nbt.put(SKULL_OWNER_KEY, NbtHelper.writeGameProfile(new NbtCompound(), profile)));
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        GameProfile gameProfile = NbtHelper.toGameProfile(stack.getOrCreateNbt().getCompound(SKULL_OWNER_KEY));
        return (gameProfile == null ? "Steve" : gameProfile.getName()) + "'s Head";
    }
}

