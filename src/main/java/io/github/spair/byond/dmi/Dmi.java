package io.github.spair.byond.dmi;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;

@Data
@NoArgsConstructor
@SuppressWarnings("unused")
public class Dmi {

    /**
     * <p>The number of states one Dmi file can store. More states than that value won't work properly.
     * <p><b>Default:</b> 512
     */
    public static final int MAX_STATES = 512;

    @Nonnull private String name = "";
    private int width;
    private int height;
    @Nonnull private DmiMeta metadata = new DmiMeta();
    @Nonnull private Map<String, DmiState> states = new HashMap<>();

    @Setter(AccessLevel.NONE)
    @Nonnull private Set<String> duplicateStatesNames = new HashSet<>();

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

    public void addState(final DmiState dmiState) {
        final String stateName = dmiState.getStateName();
        states.put(stateName, dmiState);
        if (dmiState.isDuplicate()) {
            duplicateStatesNames.add(stateName);
        }
    }

    /**
     * Returns state with provided name or null if wasn't found.
     *
     * @param stateName state name to search
     * @return {@link DmiState} instance or null if wasn't found
     */
    public DmiState getState(final String stateName) {
        return states.get(stateName);
    }

    /**
     * Returns the first sprite of state with provided name or null if wasn't found.
     *
     * @param stateName state name to search
     * @return {@link DmiSprite} instance or null if wasn't found
     */
    public DmiSprite getStateSprite(final String stateName) {
        return getSpriteIfStateNonNull(getState(stateName), DmiState::getSprite);
    }

    /**
     * Returns the first sprite of state with provided name and dir or null if wasn't found.
     *
     * @param stateName state name to search
     * @return {@link DmiSprite} instance or null if wasn't found
     */
    public DmiSprite getStateSprite(final String stateName, final SpriteDir dir) {
        return getSpriteIfStateNonNull(getState(stateName), s -> s.getSprite(dir));
    }

    /**
     * Returns sprite of state with provided name, dir and frame or null if wasn't found.
     *
     * @param stateName state name to search
     * @return {@link DmiSprite} instance or null if wasn't found
     */
    public DmiSprite getStateSprite(final String stateName, final SpriteDir dir, final int frame) {
        return getSpriteIfStateNonNull(getState(stateName), s -> s.getSprite(dir, frame));
    }

    public boolean hasState(final String stateName) {
        return states.containsKey(stateName);
    }

    /**
     * Shows if there are states, which have duplicate names in current Dmi.
     *
     * @return true if there are duplicates, otherwise false
     */
    public boolean isHasDuplicates() {
        return !duplicateStatesNames.isEmpty();
    }

    /**
     * Shows if the number of states more than {@link #MAX_STATES}.
     *
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

    private DmiSprite getSpriteIfStateNonNull(final DmiState state, final Function<DmiState, DmiSprite> function) {
        if (Objects.nonNull(state)) {
            return function.apply(state);
        } else {
            return null;
        }
    }
}
