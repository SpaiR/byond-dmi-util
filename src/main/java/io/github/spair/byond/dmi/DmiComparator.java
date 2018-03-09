package io.github.spair.byond.dmi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class DmiComparator {

    @Nonnull
    public static DmiDiff compare(@Nullable final Dmi oldDmi, @Nullable final Dmi newDmi) {
        DmiDiff dmiDiff = new DmiDiff(getDiffList(oldDmi, newDmi));

        dmiDiff.setOldMeta(extractOrNull(oldDmi, Dmi::getMetadata));
        dmiDiff.setNewMeta(extractOrNull(newDmi, Dmi::getMetadata));

        return dmiDiff;
    }

    private static List<DmiDiff.DiffEntry> getDiffList(@Nullable final Dmi oldDmi, @Nullable final Dmi newDmi) {
        List<DmiDiff.DiffEntry> diffEntries = new ArrayList<>();

        final Map<String, DmiState> oldStates = Optional
                .ofNullable(extractOrNull(oldDmi, Dmi::getStates)).orElse(Collections.emptyMap());
        final Map<String, DmiState> newStates = Optional
                .ofNullable(extractOrNull(newDmi, Dmi::getStates)).orElse(Collections.emptyMap());

        oldStates.forEach((stateName, oldState) -> {
            final DmiState newState = newStates.get(stateName);

            if (Objects.nonNull(newState)) {
                diffEntries.addAll(findOldAndNewStateDiff(oldState, newState));
            } else {
                diffEntries.addAll(listOnlyOneStateSprites(oldState, true));
            }
        });

        newStates.forEach((stateName, newState) -> {
            if (!oldStates.containsKey(stateName)) {
                diffEntries.addAll(listOnlyOneStateSprites(newState, false));
            }
        });

        return diffEntries;
    }

    private static List<DmiDiff.DiffEntry> listOnlyOneStateSprites(final DmiState state, final boolean isOldState) {
        List<DmiDiff.DiffEntry> diffs = new ArrayList<>();
        final String stateName = state.getMetadata().getName();

        state.getSprites().forEach((spriteDir, stateSprite) ->
                stateSprite.forEach(sprite -> {
                    if (isOldState) {
                        diffs.add(new DmiDiff.DiffEntry(stateName, sprite, null));
                    } else {
                        diffs.add(new DmiDiff.DiffEntry(stateName, null, sprite));
                    }
                })
        );

        return diffs;
    }

    private static List<DmiDiff.DiffEntry> findOldAndNewStateDiff(final DmiState oldState, final DmiState newState) {
        List<DmiDiff.DiffEntry> diffs = new ArrayList<>();

        final String stateName = oldState.getMetadata().getName();

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
                    diffs.add(new DmiDiff.DiffEntry(stateName, oldSprite, newSprite));
                }
            }

            if (oldSpritesCount < newSpritesCount) {
                for (int frameNumber = oldSpritesCount; frameNumber < newSpritesCount; frameNumber++) {
                    diffs.add(new DmiDiff.DiffEntry(stateName, null, newSprites.get(frameNumber)));
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
                                    diffs.add(new DmiDiff.DiffEntry(stateName, null, newSprite))
                            )
                    );
        }

        return diffs;
    }

    private static <V> V extractOrNull(final Dmi dmi, final Function<Dmi, V> function) {
        if (Objects.nonNull(dmi)) {
            return function.apply(dmi);
        } else {
            return null;
        }
    }

    private DmiComparator() {
    }
}
