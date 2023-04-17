package de.takacick.stealbodyparts.registry.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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

        }

        return super.use(world, user, hand);
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

