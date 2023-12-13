package de.takacick.upgradebody.registry.bodypart;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public abstract class BodyPart {

    private final String name;
    private final Identifier identifier;

    public BodyPart(String name, Identifier identifier) {
        this.name = name;
        this.identifier = identifier;
    }

    public abstract double getHeight();

    public abstract double getWidth();

    public String getName() {
        return name;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public int getHeightIndex() {
        return 0;
    }

    public float getPivotYOffset() {
        return 0f;
    }

    public boolean allowsWalking() {
        return false;
    }

    public boolean allowsArms() {
        return false;
    }

    public boolean affectModelOrdering() {
        return false;
    }

    public String getInheritModelPart() {
        return null;
    }

    public float getHealth() {
        return 0f;
    }

    public void onEquip(PlayerEntity playerEntity) {

    }

    public void onDequip(PlayerEntity playerEntity) {

    }

    public void tick(PlayerEntity playerEntity) {

    }
}
