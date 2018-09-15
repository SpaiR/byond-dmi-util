package io.github.spair.byond.dmi.comparator;

import io.github.spair.byond.dmi.DmiSprite;
import lombok.Data;

import java.util.Optional;

@Data
@SuppressWarnings("WeakerAccess")
public class DmiDiffEntry {

    private String stateName;
    private DmiSprite oldSprite;
    private DmiSprite newSprite;
    private DmiDiffStatus status;

    DmiDiffEntry(final String stateName, final DmiSprite oldSprite, final DmiSprite newSprite) {
        this.stateName = stateName;
        this.oldSprite = oldSprite;
        this.newSprite = newSprite;

        if (oldSprite != null && newSprite != null) {
            status = DmiDiffStatus.MODIFIED;
        } else if (oldSprite == null && newSprite != null) {
            status = DmiDiffStatus.CREATED;
        } else if (oldSprite != null) {
            status = DmiDiffStatus.DELETED;
        } else {
            throw new IllegalArgumentException("Original and Modified sprites are null. State name: " + stateName);
        }
    }

    public Optional<DmiSprite> getOldSprite() {
        return Optional.ofNullable(oldSprite);
    }

    public Optional<DmiSprite> getNewSprite() {
        return Optional.ofNullable(newSprite);
    }
}
