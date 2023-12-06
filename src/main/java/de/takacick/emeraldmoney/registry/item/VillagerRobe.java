package de.takacick.emeraldmoney.registry.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class VillagerRobe extends Item implements Equipment {

    private static final VillagerData DEFAULT = new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 0);
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public VillagerRobe(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        UUID uUID = UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D");
        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uUID, "Armor modifier", 15d, EntityAttributeModifier.Operation.ADDITION));

        this.attributeModifiers = builder.build();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return this.equipAndSwap(this, world, user, hand);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.CHEST;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.CHEST) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9Check the pockets for some §aemeralds§9!"));

        super.appendTooltip(stack, world, tooltip, context);
    }

    public Text getName(ItemStack stack) {
        VillagerData villagerData = getVillagerData(stack);

        if (villagerData != null) {
            if (!villagerData.getProfession().equals(VillagerProfession.NONE)) {
                return Text.translatable(EntityType.VILLAGER.getTranslationKey() + "." + Registries.VILLAGER_PROFESSION.getId(villagerData.getProfession()).getPath())
                        .append(" ")
                        .append(super.getName(stack));
            }
        }

        return super.getName(stack);
    }

    public static VillagerData getVillagerData(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();

        if (nbtCompound == null) {
            return DEFAULT;
        }

        DataResult<VillagerData> dataResult = VillagerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbtCompound.get("VillagerData")));
        return dataResult.result().orElse(DEFAULT);
    }

    public static ItemStack setVillagerData(ItemStack stack, VillagerData villagerData) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();

        VillagerData.CODEC.encodeStart(NbtOps.INSTANCE, villagerData).result()
                .ifPresent(nbtElement -> nbtCompound.put("VillagerData", nbtElement));
        return stack;
    }
}
