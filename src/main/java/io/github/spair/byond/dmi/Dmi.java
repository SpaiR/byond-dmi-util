package io.github.spair.byond.dmi;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Dmi {

    private String name;
    private int width;
    private int height;
    private DmiMeta metadata;
    private Map<String, DmiState> states;
    private boolean hasDuplicates;
    private Set<String> duplicateStatesNames;

    public Dmi() { }

    public Dmi(String name, int width, int height, DmiMeta metadata, Map<String, DmiState> states) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.metadata = metadata;
        this.states = states;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public DmiMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(DmiMeta metadata) {
        this.metadata = metadata;
    }

    public Map<String, DmiState> getStates() {
        return states;
    }

    public void setStates(Map<String, DmiState> states) {
        this.states = states;
    }

    public boolean isHasDuplicates() {
        return hasDuplicates;
    }

    public void setHasDuplicates(boolean hasDuplicates) {
        this.hasDuplicates = hasDuplicates;
    }

    public Set<String> getDuplicateStatesNames() {
        return duplicateStatesNames;
    }

    public void setDuplicateStatesNames(Set<String> duplicateStatesNames) {
        this.duplicateStatesNames = duplicateStatesNames;
    }

    @Override
    public String toString() {
        return "Dmi{" +
                "name='" + name + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", metadata=" + metadata +
                ", states=" + states +
                ", hasDuplicates=" + hasDuplicates +
                ", duplicateStatesNames=" + duplicateStatesNames +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dmi)) return false;
        Dmi dmi = (Dmi) o;
        return width == dmi.width &&
                height == dmi.height &&
                hasDuplicates == dmi.hasDuplicates &&
                Objects.equals(name, dmi.name) &&
                Objects.equals(metadata, dmi.metadata) &&
                Objects.equals(states, dmi.states) &&
                Objects.equals(duplicateStatesNames, dmi.duplicateStatesNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, width, height, metadata, states, hasDuplicates, duplicateStatesNames);
    }
}
