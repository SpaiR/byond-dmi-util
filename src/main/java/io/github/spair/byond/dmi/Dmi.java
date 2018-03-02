package io.github.spair.byond.dmi;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@SuppressWarnings("WeakerAccess")
public class Dmi {

    @Nonnull
    private String name = "";
    private int width;
    private int height;
    @Nonnull
    private DmiMeta metadata = new DmiMeta();
    @Nonnull
    private Map<String, DmiState> states = new HashMap<>();
    @Nonnull
    private Set<String> duplicateStatesNames = new HashSet<>();

    public Dmi(@Nonnull final String name, final int width, final int height,
               @Nonnull final DmiMeta metadata, @Nonnull final Map<String, DmiState> states) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.metadata = metadata;
        this.states = states;
        checkDuplicates();
    }

    public void setStates(@Nonnull final Map<String, DmiState> states) {
        this.states = states;
        checkDuplicates();
    }

    public boolean isHasDuplicates() {
        return !duplicateStatesNames.isEmpty();
    }

    private void checkDuplicates() {
        states.forEach((stateName, dmiState) -> {
            if (dmiState.isDuplicate()) {
                duplicateStatesNames.add(stateName);
            }
        });
    }
}
