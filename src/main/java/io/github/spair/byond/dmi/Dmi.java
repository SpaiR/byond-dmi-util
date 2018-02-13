package io.github.spair.byond.dmi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@SuppressWarnings("WeakerAccess")
public class Dmi {

    private String name;
    private int width;
    private int height;
    private DmiMeta metadata;
    private Map<String, DmiState> states;
    private boolean hasDuplicates;
    private Set<String> duplicateStatesNames;

    public Dmi(final String name, final int width, final int height,
               final DmiMeta metadata, final Map<String, DmiState> states) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.metadata = metadata;
        this.states = states;
    }

    public void processDuplicates() {
        duplicateStatesNames = new HashSet<>();

        states.forEach((stateName, dmiState) -> {
            if (dmiState.isDuplicate()) {
                duplicateStatesNames.add(stateName);
            }
        });

        if (duplicateStatesNames.size() > 0) {
            hasDuplicates = true;
        }
    }
}
