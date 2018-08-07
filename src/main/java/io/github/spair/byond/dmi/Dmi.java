package io.github.spair.byond.dmi;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

@Data
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@SuppressWarnings("WeakerAccess")
public class Dmi {

    /**
     * The number of states one Dmi file can store. More states than that value won't work properly.
     */
    public static final int MAX_STATES = 512;

    private String name = "";
    private int width;
    private int height;
    private DmiMeta metadata = new DmiMeta();
    private Map<String, DmiState> states = new HashMap<>();
    private Set<String> duplicateStatesNames = new HashSet<>();

    public Dmi(final String name, final int width, final int height,
               final DmiMeta metadata, final Map<String, DmiState> states) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.metadata = metadata;
        this.states = states;

        states.forEach((stateName, dmiState) -> {
            if (dmiState.isDuplicate()) {
                duplicateStatesNames.add(stateName);
            }
        });
    }

    public void addState(final DmiState dmiState) {
        states.put(dmiState.getName(), dmiState);
        if (dmiState.isDuplicate()) {
            duplicateStatesNames.add(dmiState.getName());
        }
    }

    /**
     * Returns state with provided name or null if wasn't found.
     *
     * @param stateName state name to search
     * @return {@link DmiState} instance or null if wasn't found
     */
    public Optional<DmiState> getState(final String stateName) {
        return Optional.ofNullable(states.get(stateName));
    }

    /**
     * Returns the first sprite of state with provided name or null if wasn't found.
     *
     * @param stateName state name to search
     * @return {@link DmiSprite} instance or null if wasn't found
     */
    public Optional<DmiSprite> getStateSprite(final String stateName) {
        return getState(stateName).flatMap(DmiState::getSprite);
    }

    /**
     * Returns the first sprite of state with provided name and dir or null if wasn't found.
     *
     * @param stateName state name to search
     * @return {@link DmiSprite} instance or null if wasn't found
     */
    public Optional<DmiSprite> getStateSprite(final String stateName, final SpriteDir dir) {
        return getState(stateName).flatMap(s -> s.getSprite(dir));
    }

    /**
     * Returns sprite of state with provided name, dir and frame or null if wasn't found.
     *
     * @param stateName state name to search
     * @return {@link DmiSprite} instance or null if wasn't found
     */
    public Optional<DmiSprite> getStateSprite(final String stateName, final SpriteDir dir, final int frame) {
        return getState(stateName).flatMap(s -> s.getSprite(dir, frame));
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
}
