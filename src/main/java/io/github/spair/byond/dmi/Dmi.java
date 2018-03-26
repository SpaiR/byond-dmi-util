package io.github.spair.byond.dmi;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
public class Dmi {

    /**
     * <p>The number of states one Dmi file can store. More states than that value won't work properly.
     * <p><b>Default:</b> 512
     */
    public static final int MAX_STATES = 512;

    @Nonnull
    private String name = "";
    private int width;
    private int height;
    @Nonnull
    private DmiMeta metadata = new DmiMeta();
    @Nonnull
    private Map<String, DmiState> states = new HashMap<>();
    @Nonnull
    @Setter(AccessLevel.NONE)
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

    /**
     * Shows if there are states, which have duplicate names in current Dmi.
     * @return true if there are duplicates, otherwise false
     */
    public boolean isHasDuplicates() {
        return !duplicateStatesNames.isEmpty();
    }

    /**
     * Shows if the number of states more than {@link #MAX_STATES}.
     * @return true if too many states, otherwise false
     */
    public boolean isStateOverflow() {
        return states.size() > MAX_STATES;
    }

    private void checkDuplicates() {
        duplicateStatesNames.clear();
        states.forEach((stateName, dmiState) -> {
            if (dmiState.isDuplicate()) {
                duplicateStatesNames.add(stateName);
            }
        });
    }
}
