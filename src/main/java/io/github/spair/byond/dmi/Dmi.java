package io.github.spair.byond.dmi;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;
import java.util.Iterator;

@Data
@NoArgsConstructor
@SuppressWarnings("WeakerAccess")
public class Dmi implements Iterable<Map.Entry<String, DmiState>> {

    /**
     * The number of states one Dmi file can store. More states than that value won't work properly.
     */
    public static final int MAX_STATES = 512;
    public static final int DEFAULT_SPRITE_SIZE = 32;

    private String name = "";

    private int totalWidth = DEFAULT_SPRITE_SIZE;
    private int totalHeight = DEFAULT_SPRITE_SIZE;

    private int spriteWidth = DEFAULT_SPRITE_SIZE;
    private int spriteHeight = DEFAULT_SPRITE_SIZE;

    @Setter(AccessLevel.NONE) private Map<String, DmiState> states = new LinkedHashMap<>();
    @Setter(AccessLevel.NONE) private Set<String> duplicateStatesNames = new HashSet<>();

    public Dmi(final String name, final int totalWidth, final int totalHeight,
               final int spriteWidth, final int spriteHeight, final Map<String, DmiState> states) {
        this.name = name;
        this.totalWidth = totalWidth;
        this.totalHeight = totalHeight;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.states = states;

        for (val state : states.entrySet()) {
            if (state.getValue().hasDuplicates()) {
                duplicateStatesNames.add(state.getKey());
            }
        }
    }

    public void save(final File file) {
        try {
            DmiWriter.writeToFile(file, this);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to save dmi in file", e);
        }
    }

    public void addState(final DmiState dmiState) {
        states.put(dmiState.getName(), dmiState);
        if (dmiState.hasDuplicates()) {
            duplicateStatesNames.add(dmiState.getName());
        }
    }

    public DmiState removeState(final String stateName) {
        duplicateStatesNames.remove(stateName);
        return states.remove(stateName);
    }

    /**
     * Returns state with provided name or empty optional if not found.
     *
     * @param stateName state name to search
     * @return {@link DmiState} instance or empty optional if not found
     */
    public Optional<DmiState> getState(final String stateName) {
        return Optional.ofNullable(states.get(stateName));
    }

    /**
     * Returns the first sprite of state with provided name or empty optional if not found.
     *
     * @param stateName state name to search
     * @return {@link DmiSprite} instance or empty optional if not found
     */
    public Optional<DmiSprite> getStateSprite(final String stateName) {
        return getState(stateName).flatMap(DmiState::getSprite);
    }

    /**
     * Returns sprite of state with provided name and dir or empty optional if not found.
     *
     * @param stateName state name to search
     * @param dir       dir value to search
     * @return {@link DmiSprite} instance or empty optional if not found
     */
    public Optional<DmiSprite> getStateSprite(final String stateName, final SpriteDir dir) {
        return getState(stateName).flatMap(s -> s.getSprite(dir));
    }

    /**
     * Returns sprite of state with provided name, dir and frame or empty optional if not found.
     *
     * @param stateName state name to search
     * @param dir       dir value to search
     * @param frame     dir number to search
     * @return {@link DmiSprite} instance or empty optional if not found
     */
    public Optional<DmiSprite> getStateSprite(final String stateName, final SpriteDir dir, final int frame) {
        return getState(stateName).flatMap(s -> s.getSprite(dir, frame));
    }

    public boolean hasState(final String stateName) {
        return states.containsKey(stateName);
    }

    /**
     * Shows if there are metaStates, which have duplicate names in current Dmi.
     *
     * @return true if there are duplicates, otherwise false
     */
    public boolean isHasDuplicates() {
        return !duplicateStatesNames.isEmpty();
    }

    /**
     * Shows if the number of metaStates more than {@link #MAX_STATES}.
     *
     * @return true if too many metaStates, otherwise false
     */
    public boolean isStateOverflow() {
        return states.size() > MAX_STATES;
    }

    @Override
    public Iterator<Map.Entry<String, DmiState>> iterator() {
        return states.entrySet().iterator();
    }
}
