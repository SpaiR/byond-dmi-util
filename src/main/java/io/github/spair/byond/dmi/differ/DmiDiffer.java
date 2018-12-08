package io.github.spair.byond.dmi.differ;

import io.github.spair.byond.dmi.Dmi;
import io.github.spair.byond.dmi.DmiSprite;
import io.github.spair.byond.dmi.DmiState;
import lombok.val;

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
public final class DmiDiffer {

    private DmiDiffer() {
    }

    /**
     * Method to compare two dmi's and get list of diffs for them
     *
     * @param initDmi initial dmi
     * @param modDmi  modified dmi
     * @return list of {@link DmiDiff} objects
     */
    public static List<DmiDiff> findDiffs(final Dmi initDmi, final Dmi modDmi) {
        List<DmiDiff> dmiDiffs = new ArrayList<>();

        val initStates = extractStates(initDmi);
        val modStates = extractStates(modDmi);

        for (val state : initStates.entrySet()) {
            val initState = state.getValue();
            val modState = modStates.get(state.getKey());

            if (modState != null) {
                dmiDiffs.addAll(findInitAndModStateDiff(initState, modState));
            } else {
                dmiDiffs.addAll(listOnlyOneStateSprites(initState, true));
            }
        }

        for (val state : modStates.entrySet()) {
            if (!initStates.containsKey(state.getKey())) {
                dmiDiffs.addAll(listOnlyOneStateSprites(state.getValue(), false));
            }
        }

        return dmiDiffs;
    }

    private static List<DmiDiff> listOnlyOneStateSprites(final DmiState state, final boolean isInitState) {
        List<DmiDiff> dmiDiffs = new ArrayList<>();
        val stateName = state.getName();

        for (val stateEntry : state) {
            for (val sprite : stateEntry.getValue()) {
                if (isInitState) {
                    dmiDiffs.add(new DmiDiff(stateName, sprite, null));
                } else {
                    dmiDiffs.add(new DmiDiff(stateName, null, sprite));
                }
            }
        }

        return dmiDiffs;
    }

    private static List<DmiDiff> findInitAndModStateDiff(final DmiState initState, final DmiState modState) {
        val dmiDiffs = new ArrayList<DmiDiff>();
        val stateName = initState.getName();

        val initStateSprites = initState.getSprites();
        val modStateSprites = modState.getSprites();

        for (val stateEntry : initStateSprites.entrySet()) {
            val initSpritesList = stateEntry.getValue();
            val modSpritesList = modStateSprites.getOrDefault(stateEntry.getKey(), Collections.emptyList());

            val initSpritesCount = initSpritesList.size();
            val modSpritesCount = modSpritesList.size();

            for (int frameNumber = 0; frameNumber < initSpritesCount; frameNumber++) {
                DmiSprite initSprite = initSpritesList.get(frameNumber);
                DmiSprite modSprite = null;

                if (frameNumber <= (modSpritesCount - 1)) {
                    modSprite = modSpritesList.get(frameNumber);
                }

                if (!initSprite.equals(modSprite)) {
                    dmiDiffs.add(new DmiDiff(stateName, initSprite, modSprite));
                }
            }

            if (initSpritesCount < modSpritesCount) {
                for (int frameNumber = initSpritesCount; frameNumber < modSpritesCount; frameNumber++) {
                    dmiDiffs.add(new DmiDiff(stateName, null, modSpritesList.get(frameNumber)));
                }
            }
        }

        // Find and add all new sprites to diff.
        if (initStateSprites.size() < modStateSprites.size()) {
            modStateSprites.keySet()
                    .stream().filter(d -> !initStateSprites.keySet().contains(d)).collect(Collectors.toSet())
                    .forEach(spriteDir ->
                            modStateSprites.get(spriteDir).forEach(newSprite ->
                                    dmiDiffs.add(new DmiDiff(stateName, null, newSprite))
                            )
                    );
        }

        return dmiDiffs;
    }

    private static Map<String, DmiState> extractStates(final Dmi dmi) {
        return Optional.ofNullable(dmi).map(Dmi::getStates).orElse(Collections.emptyMap());
    }
}
