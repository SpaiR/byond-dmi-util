package io.github.spair.byond.dmi;

import lombok.Data;
import lombok.NoArgsConstructor;
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
@SuppressWarnings({"WeakerAccess", "OptionalGetWithoutIsPresent"})
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

    private final Map<String, DmiState> states = new LinkedHashMap<>();
    private final Set<String> duplicateStatesNames = new HashSet<>();

    public Dmi(final String name, final int totalWidth, final int totalHeight,
               final int spriteWidth, final int spriteHeight, final Map<String, DmiState> states) {
        this.name = name;
        this.totalWidth = totalWidth;
        this.totalHeight = totalHeight;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.states.putAll(states);

        for (val state : states.entrySet()) {
            if (state.getValue().hasDuplicates()) {
                duplicateStatesNames.add(state.getKey());
            }
        }
    }

    /**
     * Method to serialize current object to file. File would have proper metadata,
     * so it will become fully functional file of dmi format.
     * The only difference from original dmi file could be that, during saving, state duplicates will be placed together.
     *
     * @param file fully functional dmi file with proper metadata
     */
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

    public void clearStates() {
        states.clear();
        duplicateStatesNames.clear();
    }

    public DmiState getState(final String stateName) {
        return getStateSafe(stateName).get();
    }

    /**
     * Returns state with provided name or empty optional if not found.
     *
     * @param stateName state name to search
     * @return {@link DmiState} instance or empty optional if not found
     */
    public Optional<DmiState> getStateSafe(final String stateName) {
        return Optional.ofNullable(states.get(stateName));
    }

    public DmiSprite getStateSprite(final String stateName) {
        return getStateSpriteSafe(stateName).get();
    }

    /**
     * Returns the first sprite of state with provided name or empty optional if not found.
     *
     * @param stateName state name to search
     * @return {@link DmiSprite} instance or empty optional if not found
     */
    public Optional<DmiSprite> getStateSpriteSafe(final String stateName) {
        return getStateSafe(stateName).flatMap(DmiState::getSpriteSafe);
    }

    public DmiSprite getStateSprite(final String stateName, final SpriteDir dir) {
        return getStateSpriteSafe(stateName, dir).get();
    }

    /**
     * Returns sprite of state with provided name and dir or empty optional if not found.
     *
     * @param stateName state name to search
     * @param dir       dir value to search
     * @return {@link DmiSprite} instance or empty optional if not found
     */
    public Optional<DmiSprite> getStateSpriteSafe(final String stateName, final SpriteDir dir) {
        return getStateSafe(stateName).flatMap(s -> s.getSpriteSafe(dir));
    }

    public DmiSprite getStateSprite(final String stateName, final SpriteDir dir, final int frame) {
        return getStateSpriteSafe(stateName, dir, frame).get();
    }

    /**
     * Returns sprite of state with provided name, dir and frame or empty optional if not found.
     *
     * @param stateName state name to search
     * @param dir       dir value to search
     * @param frame     dir number to search
     * @return {@link DmiSprite} instance or empty optional if not found
     */
    public Optional<DmiSprite> getStateSpriteSafe(final String stateName, final SpriteDir dir, final int frame) {
        return getStateSafe(stateName).flatMap(s -> s.getSpriteSafe(dir, frame));
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
