package io.github.spair.byond.dmi.comparator;

import io.github.spair.byond.dmi.Dmi;
import io.github.spair.byond.dmi.DmiState;
import io.github.spair.byond.dmi.DmiSprite;
import io.github.spair.byond.dmi.SpriteDir;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Class to compare two {@link io.github.spair.byond.dmi.Dmi} objects and get result of comparison
 * as {@link DmiDiff}.
 */
@SuppressWarnings("WeakerAccess")
public final class DmiComparator {

    /**
     * Compares two {@link io.github.spair.byond.dmi.Dmi} objects. One of argument may be null,
     * which will be interpreted as if another Dmi created if null is the first parameter or deleted if vice versa.
     *
     * @param oldDmi old {@link io.github.spair.byond.dmi.Dmi} object or null
     * @param newDmi new {@link io.github.spair.byond.dmi.Dmi} object or null
     * @return {@link DmiDiff} object
     */
    public static DmiDiff compare(final Dmi oldDmi, final Dmi newDmi) {
        DmiDiff dmiDiff = new DmiDiff(getDiffList(oldDmi, newDmi));

        dmiDiff.setOldMeta(Optional.ofNullable(oldDmi).map(Dmi::getMetadata).orElse(null));
        dmiDiff.setNewMeta(Optional.ofNullable(newDmi).map(Dmi::getMetadata).orElse(null));

        return dmiDiff;
    }

    private static List<DmiDiffEntry> getDiffList(final Dmi oldDmi, final Dmi newDmi) {
        List<DmiDiffEntry> dmiDiffEntryEntries = new ArrayList<>();

        final Map<String, DmiState> oldStates = extractStates(oldDmi);
        final Map<String, DmiState> newStates = extractStates(newDmi);

        oldStates.forEach((stateName, oldState) -> {
            final DmiState newState = newStates.get(stateName);

            if (newState != null) {
                dmiDiffEntryEntries.addAll(findOldAndNewStateDiff(oldState, newState));
            } else {
                dmiDiffEntryEntries.addAll(listOnlyOneStateSprites(oldState, true));
            }
        });

        newStates.forEach((stateName, newState) -> {
            if (!oldStates.containsKey(stateName)) {
                dmiDiffEntryEntries.addAll(listOnlyOneStateSprites(newState, false));
            }
        });

        return dmiDiffEntryEntries;
    }

    private static List<DmiDiffEntry> listOnlyOneStateSprites(final DmiState state, final boolean isOldState) {
        List<DmiDiffEntry> dmiDiffEntries = new ArrayList<>();
        final String stateName = state.getName();

        state.getSprites().forEach((spriteDir, stateSprite) ->
                stateSprite.forEach(sprite -> {
                    if (isOldState) {
                        dmiDiffEntries.add(new DmiDiffEntry(stateName, sprite, null));
                    } else {
                        dmiDiffEntries.add(new DmiDiffEntry(stateName, null, sprite));
                    }
                })
        );

        return dmiDiffEntries;
    }

    private static List<DmiDiffEntry> findOldAndNewStateDiff(final DmiState oldState, final DmiState newState) {
        List<DmiDiffEntry> dmiDiffEntries = new ArrayList<>();

        final String stateName = oldState.getName();

        final Map<SpriteDir, List<DmiSprite>> oldStateSprites = oldState.getSprites();
        final Map<SpriteDir, List<DmiSprite>> newStateSprites = newState.getSprites();

        oldStateSprites.forEach((spriteDir, oldSprites) -> {
            final List<DmiSprite> newSprites = newStateSprites.getOrDefault(spriteDir, Collections.emptyList());

            final int oldSpritesCount = oldSprites.size();
            final int newSpritesCount = newSprites.size();

            for (int frameNumber = 0; frameNumber < oldSpritesCount; frameNumber++) {
                DmiSprite oldSprite = oldSprites.get(frameNumber);
                DmiSprite newSprite = null;

                if (frameNumber <= (newSpritesCount - 1)) {
                    newSprite = newSprites.get(frameNumber);
                }

                if (!oldSprite.equals(newSprite)) {
                    dmiDiffEntries.add(new DmiDiffEntry(stateName, oldSprite, newSprite));
                }
            }

            if (oldSpritesCount < newSpritesCount) {
                for (int frameNumber = oldSpritesCount; frameNumber < newSpritesCount; frameNumber++) {
                    dmiDiffEntries.add(new DmiDiffEntry(stateName, null, newSprites.get(frameNumber)));
                }
            }
        });

        final int oldDirsCount = oldStateSprites.size();
        final int newDirsCount = newStateSprites.size();

        if (oldDirsCount < newDirsCount) {
            newStateSprites.keySet()
                    .stream().filter(d -> !oldStateSprites.keySet().contains(d)).collect(Collectors.toSet())
                    .forEach(spriteDir ->
                            newStateSprites.get(spriteDir).forEach(newSprite ->
                                    dmiDiffEntries.add(new DmiDiffEntry(stateName, null, newSprite))
                            )
                    );
        }

        return dmiDiffEntries;
    }

    private static Map<String, DmiState> extractStates(final Dmi dmi) {
        return Optional.ofNullable(dmi).map(Dmi::getStates).orElse(Collections.emptyMap());
    }

    private DmiComparator() {
    }
}
