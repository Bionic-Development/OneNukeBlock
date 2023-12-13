package de.takacick.upgradebody.registry.bodypart.upgrades;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import net.minecraft.util.Identifier;

public class Head extends BodyPart {

    public Head() {
        super("Head", new Identifier(UpgradeBody.MOD_ID, "head"));
    }

    @Override
    public double getHeight() {
        return 0.5 * 0.9375f;
    }

    @Override
    public double getWidth() {
        return 0.5;
    }

    @Override
    public int getHeightIndex() {
        return 0;
    }

    @Override
    public boolean affectModelOrdering() {
        return true;
    }

    @Override
    public String getInheritModelPart() {
        return "head";
    }

    @Override
    public float getHealth() {
        return 0f;
    }
}
