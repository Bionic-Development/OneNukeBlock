package de.takacick.stealbodyparts.registry.entity.living;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MoldingPart {

    HEAD("head", 0),
    HEAD_1("head_1", 1),
    HEAD_2("head_2", 2),
    RIGHT_LEG("right_leg", 3),
    LEFT_LEG("left_leg", 4),
    RIGHT_ARM("right_arm", 5 ),
    LEFT_ARM("left_arm", 6),
    LEFT_ARM_1("left_arm_1", 7);

    private final String name;
    private final int index;
    private final List<String> parts;

    private MoldingPart(String name, int index, String... parts) {
        this.name = name;
        this.index = index;
        this.parts = new ArrayList<>(Arrays.asList(parts));
        this.parts.add(name);
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public List<String> getParts() {
        return parts;
    }

    public static MoldingPart getByName(String name) {
        for(MoldingPart part : values()) {
            if(part.getName().equals(name)) {
                return  part;
            }
        }

        return null;
    }
}
