package de.takacick.stealbodyparts.registry.entity.living;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import de.takacick.stealbodyparts.StealBodyParts;
import de.takacick.stealbodyparts.registry.EntityRegistry;
import de.takacick.stealbodyparts.utils.BodyPart;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AliveMoldingBodyEntity extends WardenEntity {

    private static final TrackedData<NbtCompound> COMPONENTS = BionicDataTracker.registerData(new Identifier(StealBodyParts.MOD_ID, "components"), TrackedDataHandlerRegistry.NBT_COMPOUND);
    public int lightningTicks = 0;

    public AliveMoldingBodyEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(COMPONENTS, new NbtCompound());

        super.initDataTracker();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0);
    }

    @Override
    public void tick() {
        if (this instanceof AliveMoldedBossEntity) {
            super.tick();
        }
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        if (this instanceof AliveMoldedBossEntity) {
            super.pushAwayFrom(entity);
        }
    }

    @Override
    protected void pushAway(Entity entity) {
        if (this instanceof AliveMoldedBossEntity) {
            super.pushAway(entity);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("aliveComponents", getDataTracker().get(COMPONENTS));
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("aliveComponents", NbtElement.COMPOUND_TYPE)) {
            getDataTracker().set(COMPONENTS, nbt.getCompound("aliveComponents"));
        }
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!(this instanceof AliveMoldedBossEntity)) {
            if (source.getName().equals("lightningBolt")) {
                this.discard();
                AliveMoldedBossEntity aliveMoldedBossEntity = new AliveMoldedBossEntity(EntityRegistry.ALIVE_MOLDED_BOSS, world);
                aliveMoldedBossEntity.setPos(getX(), getY(), getZ());
                int id = aliveMoldedBossEntity.getId();
                UUID uuid1 = aliveMoldedBossEntity.getUuid();
                aliveMoldedBossEntity.copyFrom(this);
                aliveMoldedBossEntity.setId(id);
                aliveMoldedBossEntity.uuid = uuid1;
                world.spawnEntity(aliveMoldedBossEntity);
                aliveMoldedBossEntity.playSpawnEffects();

                world.playSound(null, aliveMoldedBossEntity.getX(), aliveMoldedBossEntity.getBodyY(0.5), aliveMoldedBossEntity.getZ(), SoundEvents.ENTITY_WITHER_SPAWN, aliveMoldedBossEntity.getSoundCategory(), 1f, 1f);
                return false;
            } else if (amount < 1000) {
                return false;
            }
        }

        if (source.getName().equals("lightningBolt")) {
            return false;
        }

        return super.damage(source, amount);
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (sound.equals(SoundEvents.ENTITY_WARDEN_TENDRIL_CLICKS) || sound.equals(SoundEvents.ENTITY_WARDEN_HEARTBEAT)) {
            return;
        }

    }

    public boolean setBodyPart(@NotNull BodyPart bodyPart, GameProfile gameProfile, boolean steve) {

        List<MoldingPart> moldingPartList = Arrays.stream(MoldingPart.values())
                .filter(moldingPart -> moldingPart.getName().startsWith(bodyPart.getName())
                        && !hasBodyPart(moldingPart)).sorted().toList();

        if (moldingPartList.isEmpty()) {
            return false;
        }

        MoldingPart moldingPart = moldingPartList.get(0);

        setMoldingPart(moldingPart, gameProfile, steve);

        return true;
    }

    public void setMoldingPart(@NotNull MoldingPart moldingPart, GameProfile gameProfile, boolean steve) {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.copyFrom(getDataTracker().get(COMPONENTS));

        if (gameProfile == null && !steve) {
            nbtCompound.remove(moldingPart.getName());
            nbtCompound.remove(moldingPart.getName() + "slim");
            nbtCompound.remove(moldingPart.getName() + "steve");
        } else {
            if (steve) {
                nbtCompound.putBoolean(moldingPart.getName() + "steve", true);
            } else {
                nbtCompound.put(moldingPart.getName(), NbtHelper.writeGameProfile(new NbtCompound(), gameProfile));
                nbtCompound.putBoolean(moldingPart.getName() + "slim", false);
                if (gameProfile.getProperties().containsKey("textures")) {
                    String decoded = new String(Base64.getDecoder().decode(gameProfile.getProperties()
                            .get("textures").iterator().next().getValue()));
                    JsonObject json = JsonParser.parseString(decoded).getAsJsonObject();
                    if (json.has("textures") && json.get("textures").isJsonObject()) {
                        json = json.getAsJsonObject("textures");
                    }

                    if (json.has("SKIN") && json.getAsJsonObject("SKIN").has("metadata")) {
                        JsonObject skin = json.getAsJsonObject("SKIN").getAsJsonObject("metadata");
                        if (skin.has("model")) {
                            nbtCompound.putBoolean(moldingPart.getName() + "slim", Objects.equals(skin.getAsJsonPrimitive("model").getAsString(), "slim"));
                        }
                    }
                }
            }
        }

        getDataTracker().set(COMPONENTS, nbtCompound);
    }

    public GameProfile getBodyPart(@NotNull MoldingPart moldingPart) {
        NbtCompound nbtCompound = getDataTracker().get(COMPONENTS);
        if (nbtCompound == null || !nbtCompound.contains(moldingPart.getName(), NbtElement.COMPOUND_TYPE)) {
            return null;
        }

        GameProfile gameProfile = null;

        if (nbtCompound.contains(moldingPart.getName(), NbtElement.COMPOUND_TYPE)) {
            gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound(moldingPart.getName()));
        } else if (nbtCompound.contains(moldingPart.getName(), NbtElement.STRING_TYPE) && !StringUtils.isBlank(nbtCompound.getString(moldingPart.getName()))) {
            gameProfile = new GameProfile(null, nbtCompound.getString(moldingPart.getName()));
            nbtCompound.remove(moldingPart.getName());
            SkullBlockEntity.loadProperties(gameProfile, profile -> nbtCompound.put(moldingPart.getName(), NbtHelper.writeGameProfile(new NbtCompound(), profile)));
        }

        return gameProfile;
    }

    public boolean isSlim(@NotNull MoldingPart moldingPart) {
        NbtCompound nbtCompound = getDataTracker().get(COMPONENTS);
        if (nbtCompound == null || !nbtCompound.contains(moldingPart.getName() + "slim", NbtElement.BYTE_TYPE)) {
            return false;
        }

        return nbtCompound.getBoolean(moldingPart.getName() + "slim");
    }

    @Override
    public void playAmbientSound() {
        if (!(this instanceof AliveMoldedBossEntity)) {
            super.playAmbientSound();
        }
    }

    public boolean hasBodyPart(MoldingPart part) {
        NbtCompound nbtCompound = getDataTracker().get(COMPONENTS);

        return nbtCompound.contains(part.getName(), NbtElement.COMPOUND_TYPE)
                || nbtCompound.contains(part.getName() + "steve", NbtElement.BYTE_TYPE);
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public void playSpawnEffects() {
        if (this.world.isClient) {
            this.lightningTicks = 30;
        } else {
            this.world.sendEntityStatus(this, EntityStatuses.PLAY_SPAWN_EFFECTS);
        }
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }
}