package de.takacick.stealbodyparts.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BodyPart implements Comparable<BodyPart> {

    HEAD("head", 0, "hat"),
    RIGHT_LEG("right_leg", 1, "right_pants"),
    LEFT_LEG("left_leg", 2, "left_pants"),
    RIGHT_ARM("right_arm", 3, "right_sleeve"),
    LEFT_ARM("left_arm", 4, "left_sleeve");

    private final String name;
    private final int index;
    private final List<String> parts;

    private BodyPart(String name, int index, String... parts) {
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

    public static BodyPart getByName(String name) {
        for(BodyPart part : values()) {
            if(part.getName().equals(name)) {
                return  part;
            }
        }

        return null;
    }
}
