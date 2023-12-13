package de.takacick.upgradebody.registry.bodypart;

import de.takacick.upgradebody.registry.bodypart.upgrades.*;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BodyParts {

    public static final List<BodyPart> BODY_PARTS = new ArrayList<>();

    public static BodyPart HEAD = new Head();
    public static BodyPart TANK_TRACKS = new TankTracks();
    public static BodyPart ENERGY_BELLY_CANNON = new EnergyBellyCannon();
    public static BodyPart KILLER_DRILLER = new KillerDriller();
    public static BodyPart CYBER_CHAINSAWS = new CyberChainsaws();

    public static void register() {
        BODY_PARTS.add(HEAD);
        BODY_PARTS.add(TANK_TRACKS);
        BODY_PARTS.add(ENERGY_BELLY_CANNON);
        BODY_PARTS.add(KILLER_DRILLER);
        BODY_PARTS.add(CYBER_CHAINSAWS);
    }

    public static Optional<BodyPart> getBodyPart(Identifier identifier) {
        return BODY_PARTS.stream().filter(bodyPart -> bodyPart.getIdentifier().equals(identifier))
                .findFirst();
    }

    public static Optional<BodyPart> getBodyPart(String name) {
        return BODY_PARTS.stream().filter(bodyPart -> bodyPart.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
