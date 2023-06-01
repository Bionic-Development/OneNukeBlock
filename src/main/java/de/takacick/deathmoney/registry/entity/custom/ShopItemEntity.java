package de.takacick.deathmoney.registry.entity.custom;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ShopItemEntity extends Entity {

    private static final TrackedData<ItemStack> ITEM_STACK = BionicDataTracker.registerData(new Identifier(DeathMoney.MOD_ID, "shop_item_stack"), TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<Integer> PRICE = BionicDataTracker.registerData(new Identifier(DeathMoney.MOD_ID, "shop_item_price"), TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> OFFSET = BionicDataTracker.registerData(new Identifier(DeathMoney.MOD_ID, "shop_item_offset"), TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> SCALE = BionicDataTracker.registerData(new Identifier(DeathMoney.MOD_ID, "shop_item_scale"), TrackedDataHandlerRegistry.FLOAT);

    public ShopItemEntity(EntityType<? extends ShopItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ShopItemEntity(World world, double x, double y, double z, ItemStack itemStack) {
        this(EntityRegistry.SHOP_ITEM, world);
        this.setPosition(x, y, z);
        getDataTracker().set(ITEM_STACK, itemStack);
    }

    public static ShopItemEntity create(EntityType<ShopItemEntity> entityType, World world) {
        return new ShopItemEntity(entityType, world);
    }

    @Override
    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    public boolean collides() {
        return !this.isRemoved();
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        getDataTracker().set(ITEM_STACK, ItemStack.fromNbt(nbt.getCompound("customItemStack")));
        getDataTracker().set(PRICE, nbt.getInt("price"));
        getDataTracker().set(OFFSET, nbt.getFloat("offset"));
        getDataTracker().set(SCALE, nbt.getFloat("scale"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("customItemStack", getDataTracker().get(ITEM_STACK).writeNbt(new NbtCompound()));
        nbt.putInt("price", getDataTracker().get(PRICE));
        nbt.putFloat("offset", getDataTracker().get(OFFSET));
        nbt.putFloat("scale", getDataTracker().get(SCALE));
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ITEM_STACK, Items.BARRIER.getDefaultStack());
        this.dataTracker.startTracking(PRICE, 1);
        this.dataTracker.startTracking(OFFSET, 0f);
        this.dataTracker.startTracking(SCALE, 1f);
    }

    @Override
    public boolean shouldRender(double distance) {
        double d = this.getBoundingBox().getAverageSideLength();
        if (Double.isNaN(d)) {
            d = 1.0;
        }
        return distance < (d *= 64.0 * 5) * d;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void pushAwayFrom(Entity entity) {

    }

    public ItemStack getItemStack() {
        return getDataTracker().get(ITEM_STACK).copy();
    }

    @Override
    protected void pushOutOfBlocks(double x, double y, double z) {

    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (((PlayerProperties) player).getDeaths() < getPrice()) {
            return ActionResult.CONSUME;
        }

        world.playSound(getX(), getY() + calculateBoundingBox().getYLength() / 2, getZ(), SoundEvents.ENTITY_WITHER_SKELETON_STEP, SoundCategory.PLAYERS, 0.1f, 1f, true);
        world.playSound(getX(), getY() + calculateBoundingBox().getYLength() / 2, getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, 1f, true);
        if (!world.isClient) {
            ((PlayerProperties) player).addDeaths(-getPrice(), false);
            player.getInventory().offerOrDrop(getItemStack().copy());
        }

        return ActionResult.SUCCESS;
    }

    public void setPrice(int price) {
        getDataTracker().set(PRICE, price);
    }

    public int getPrice() {
        return getDataTracker().get(PRICE);
    }

    public void setOffset(float offset) {
        getDataTracker().set(OFFSET, offset);
    }

    public float getOffset() {
        return getDataTracker().get(OFFSET);
    }

    public void setScale(float scale) {
        getDataTracker().set(SCALE, scale);
    }

    public float getScale() {
        return getDataTracker().get(SCALE);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {

        if (source.getSource() instanceof PlayerEntity player && player.isCreative()) {
            this.discard();
        }

        return false;
    }

    @Override
    public void kill() {

    }

    public float getRotation(float tickDelta) {
        return ((float) this.age + tickDelta) / 20.0f;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}

