package de.takacick.upgradebody.registry.bodypart;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class BodyPartManager {

    private boolean upgrading;
    private HashSet<BodyPart> bodyParts;
    private double width;
    private double height;
    private boolean canWalk;
    private boolean canUseArms;
    private boolean headOnly;
    private float hearts;

    public BodyPartManager() {
        this(List.of(), false);
    }

    public BodyPartManager(Collection<BodyPart> bodyParts, boolean upgrading) {
        this.bodyParts = new HashSet<>(bodyParts);
        this.upgrading = upgrading;

        reloadAttributes();
    }

    public void setUpgrading(boolean upgrading) {
        this.upgrading = upgrading;
    }

    public boolean isUpgrading() {
        return upgrading;
    }

    public Set<BodyPart> getBodyParts() {
        return this.bodyParts;
    }

    public boolean addBodyPart(BodyPart bodyPart) {
        return this.bodyParts.add(bodyPart);
    }

    public boolean removeBodyPart(BodyPart bodyPart) {
        return this.bodyParts.removeIf(part -> part.equals(bodyPart));
    }

    public boolean setBodyPart(BodyPart bodyPart, boolean enable) {
        return enable ? this.bodyParts.add(bodyPart) : this.bodyParts.removeIf(part -> part.equals(bodyPart));
    }

    public boolean hasBodyPart(BodyPart bodyPart) {
        return this.getBodyParts().contains(bodyPart);
    }

    public boolean canWalk() {
        return this.canWalk;
    }

    public boolean canUseArms() {
        return this.canUseArms;
    }

    public boolean isHeadOnly() {
        return this.headOnly;
    }

    public Box calculateBoundingBox(Vec3d pos) {
        double width = getWidth() / 2d;
        double height = getHeight();

        return new Box(pos.getX() - width, pos.getY(), pos.getZ() - width,
                pos.getX() + width, pos.getY() + height, pos.getZ() + width);
    }

    public void reloadAttributes() {
        this.canWalk = this.getBodyParts().stream().anyMatch(BodyPart::allowsWalking);
        this.canUseArms = this.getBodyParts().stream().anyMatch(BodyPart::allowsArms);
        this.headOnly = hasBodyPart(BodyParts.HEAD) && this.getBodyParts().size() == 1;
        this.hearts = (float) Math.max(this.getBodyParts().stream().mapToDouble(BodyPart::getHealth).sum(), 0f) + 2f;
    }

    public EntityDimensions calculateDimensions() {
        this.width = Math.max(this.getBodyParts().stream().mapToDouble(BodyPart::getWidth).max().orElse(0), 0.1d);

        HashSet<Integer> heightIndexes = new HashSet<>();
        this.bodyParts.forEach(bodyPart -> heightIndexes.add(bodyPart.getHeightIndex()));

        this.height = 0;
        for (Integer index : heightIndexes) {
            this.height += this.bodyParts.stream()
                    .filter(bodyPart -> bodyPart.getHeightIndex() == index)
                    .mapToDouble(BodyPart::getHeight).max().orElse(0);
        }

        this.height = Math.max(this.height, 0.1d);
        return new EntityDimensions((float) getWidth(), (float) getHeight(), false);
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public float getHearts() {
        return hearts;
    }

    public static NbtCompound toNbt(BodyPartManager bodyPartManager, NbtCompound nbtCompound) {
        NbtList list = new NbtList();

        bodyPartManager.getBodyParts().forEach(bodyPart -> {
            NbtCompound nbt = new NbtCompound();
            nbt.putString("identifier", bodyPart.getIdentifier().toString());
            list.add(nbt);
        });

        nbtCompound.putBoolean("upgrading", bodyPartManager.isUpgrading());
        nbtCompound.put("bodyparts", list);

        return nbtCompound;
    }

    public static BodyPartManager fromNbt(NbtCompound nbtCompound) {
        List<BodyPart> bodyParts = new ArrayList<>();
        boolean upgrading = false;

        if (nbtCompound != null) {
            upgrading = nbtCompound.getBoolean("upgrading");

            if (nbtCompound.contains("bodyparts", NbtElement.LIST_TYPE)) {
                NbtList list = nbtCompound.getList("bodyparts", NbtElement.COMPOUND_TYPE);
                list.forEach(nbtElement -> {
                    if (nbtElement instanceof NbtCompound nbt) {
                        if (nbt.contains("identifier", NbtElement.STRING_TYPE)) {
                            Identifier identifier = new Identifier(nbt.getString("identifier"));
                            BodyParts.getBodyPart(identifier).ifPresent(bodyParts::add);
                        }
                    }
                });
            }
        }

        return new BodyPartManager(bodyParts, upgrading);
    }
}
