package de.takacick.stealbodyparts.registry.item;

import com.mojang.authlib.GameProfile;
import de.takacick.stealbodyparts.registry.entity.living.AliveMoldingBodyEntity;
import de.takacick.stealbodyparts.utils.BodyPart;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class BodyPartItem extends Item {
    public static final String PART_OWNER_KEY = "PartOwner";

    private final BodyPart bodyPart;

    public BodyPartItem(BodyPart bodyPart, Settings settings) {
        super(settings);
        this.bodyPart = bodyPart;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof AliveMoldingBodyEntity aliveMoldingBodyEntity) {
            GameProfile gameProfile = null;
            NbtCompound nbtCompound = stack.getNbt();
            if (nbtCompound != null) {
                if (nbtCompound.contains("PartOwner", NbtElement.COMPOUND_TYPE)) {
                    gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("PartOwner"));
                } else if (nbtCompound.contains("PartOwner", NbtElement.STRING_TYPE) && !StringUtils.isBlank(nbtCompound.getString("PartOwner"))) {
                    gameProfile = new GameProfile(null, nbtCompound.getString("PartOwner"));
                    nbtCompound.remove("PartOwner");

                    SkullBlockEntity.loadProperties(gameProfile, profile -> nbtCompound.put("PartOwner", NbtHelper.writeGameProfile(new NbtCompound(), profile)));
                }
            }

            if (aliveMoldingBodyEntity.setBodyPart(this.bodyPart, gameProfile, gameProfile == null)) {
                entity.getWorld().playSound(null, entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_MUD_PLACE, entity.getSoundCategory(), 1f, 1f);
                if (!user.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
                return ActionResult.SUCCESS;
            }
        }

        return super.useOnEntity(stack, user, entity, hand);
    }

    public BodyPart getBodyPart() {
        return bodyPart;
    }

    @Override
    public void postProcessNbt(NbtCompound nbt) {
        super.postProcessNbt(nbt);
        if (nbt.contains(PART_OWNER_KEY, NbtElement.STRING_TYPE) && !StringUtils.isBlank(nbt.getString(PART_OWNER_KEY))) {
            GameProfile gameProfile = new GameProfile(null, nbt.getString(PART_OWNER_KEY));
            SkullBlockEntity.loadProperties(gameProfile, profile -> nbt.put(PART_OWNER_KEY, NbtHelper.writeGameProfile(new NbtCompound(), profile)));
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        GameProfile gameProfile = NbtHelper.toGameProfile(stack.getOrCreateNbt().getCompound(PART_OWNER_KEY));
        return (gameProfile == null ? "Steve" : gameProfile.getName()) + "'s " + WordUtils.capitalizeFully(getBodyPart().getName().replace("_", " "));
    }
}
