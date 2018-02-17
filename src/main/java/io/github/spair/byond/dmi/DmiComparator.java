package io.github.spair.byond.dmi;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class DmiComparator {

    public static DmiDiff compare(@Nullable final Dmi original, @Nullable final Dmi modified) {
        DmiDiff dmiDiff = new DmiDiff();

        final DmiMeta originalMetadata = extractMetadataOrNull(original);
        final DmiMeta modifiedMetadata = extractMetadataOrNull(modified);

        dmiDiff.setOriginalMeta(originalMetadata);
        dmiDiff.setModifiedMeta(modifiedMetadata);

        List<DmiDiff.DiffEntry> diffList = new ArrayList<>(getDiffList(original, modified));

        dmiDiff.setDiffEntries(diffList);
        dmiDiff.setSame(Objects.equals(originalMetadata, modifiedMetadata) && diffList.isEmpty());

        return dmiDiff;
    }

    private static List<DmiDiff.DiffEntry> getDiffList(
            @Nullable final Dmi originalDmi, @Nullable final Dmi modifiedDmi) {
        List<DmiDiff.DiffEntry> diffEntries = new ArrayList<>();

        final Map<String, DmiState> originalStates = Optional
                .ofNullable(extractStatesOrNull(originalDmi)).orElse(new HashMap<>());
        final Map<String, DmiState> modifiedStates = Optional
                .ofNullable(extractStatesOrNull(modifiedDmi)).orElse(new HashMap<>());

        originalStates.forEach((stateName, originalState) -> {
            final DmiState modifiedState = modifiedStates.get(stateName);

            if (Objects.nonNull(modifiedState)) {
                diffEntries.addAll(findOriginalAndModifiedStateDiff(originalState, modifiedState));
            } else {
                diffEntries.addAll(listOnlyOneStateSprites(originalState, true));
            }
        });

        modifiedStates.forEach((stateName, modifiedState) -> {
            if (!originalStates.containsKey(stateName)) {
                diffEntries.addAll(listOnlyOneStateSprites(modifiedState, false));
            }
        });

        return diffEntries;
    }

    private static List<DmiDiff.DiffEntry> listOnlyOneStateSprites(final DmiState state, final boolean isOriginal) {
        List<DmiDiff.DiffEntry> diffs = new ArrayList<>();
        final String stateName = state.getMetadata().getName();

        state.getSprites().forEach((spriteDir, stateSprite) ->
                stateSprite.forEach(sprite -> {
                    if (isOriginal) {
                        diffs.add(new DmiDiff.DiffEntry(stateName, sprite, null));
                    } else {
                        diffs.add(new DmiDiff.DiffEntry(stateName, null, sprite));
                    }
                })
        );

        return diffs;
    }

    private static List<DmiDiff.DiffEntry> findOriginalAndModifiedStateDiff(
            final DmiState originalState, final DmiState modifiedState) {
        List<DmiDiff.DiffEntry> diffs = new ArrayList<>();

        final String stateName = originalState.getMetadata().getName();

        final Map<SpriteDir, List<DmiSprite>> originalStateSprites = originalState.getSprites();
        final Map<SpriteDir, List<DmiSprite>> modifiedStateSprites = modifiedState.getSprites();

        final int originalDirsCount = originalStateSprites.size();
        final int modifiedDirsCount = modifiedStateSprites.size();

        originalStateSprites.forEach((spriteDir, originalSprites) -> {
            List<DmiSprite> modifiedSprites = modifiedStateSprites.getOrDefault(spriteDir, new ArrayList<>());

            final int originalSpritesCount = originalSprites.size();
            final int modifiedSpritesCount = modifiedSprites.size();

            for (int frameNumber = 0; frameNumber < originalSpritesCount; frameNumber++) {
                DmiSprite originalSprite = originalSprites.get(frameNumber);
                DmiSprite modifiedSprite;

                if (frameNumber <= (modifiedSpritesCount - 1)) {
                    modifiedSprite = modifiedSprites.get(frameNumber);
                } else {
                    modifiedSprite = null;
                }

                if (!originalSprite.equals(modifiedSprite)) {
                    diffs.add(new DmiDiff.DiffEntry(stateName, originalSprite, modifiedSprite));
                }
            }

            if (originalSpritesCount < modifiedSpritesCount) {
                for (int frameNumber = originalSpritesCount; frameNumber < modifiedSpritesCount; frameNumber++) {
                    diffs.add(new DmiDiff.DiffEntry(stateName, null, modifiedSprites.get(frameNumber)));
                }
            }
        });

        if (originalDirsCount < modifiedDirsCount) {
            modifiedStateSprites.keySet()
                    .stream().filter(d -> !originalStateSprites.keySet().contains(d)).collect(Collectors.toSet())
                    .forEach(spriteDir ->
                            modifiedStateSprites.get(spriteDir).forEach(modifiedSprite ->
                                    diffs.add(new DmiDiff.DiffEntry(stateName, null, modifiedSprite))
                            )
                    );
        }

        return diffs;
    }

    private static DmiMeta extractMetadataOrNull(final Dmi dmi) {
        if (Objects.nonNull(dmi)) {
            return dmi.getMetadata();
        } else {
            return null;
        }
    }

    private static Map<String, DmiState> extractStatesOrNull(final Dmi dmi) {
        if (Objects.nonNull(dmi)) {
            return dmi.getStates();
        } else {
            return null;
        }
    }

    private DmiComparator() { }
}
