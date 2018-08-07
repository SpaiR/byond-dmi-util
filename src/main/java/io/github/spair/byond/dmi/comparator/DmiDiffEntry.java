package io.github.spair.byond.dmi.comparator;

import io.github.spair.byond.dmi.DmiSprite;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PACKAGE)
@SuppressWarnings("WeakerAccess")
public class DmiDiffEntry {

    private String stateName;
    private DmiSprite oldSprite;
    private DmiSprite newSprite;
    private DiffStatus status;

    DmiDiffEntry(final String stateName, final DmiSprite oldSprite, final DmiSprite newSprite) {
        this.stateName = stateName;
        this.oldSprite = oldSprite;
        this.newSprite = newSprite;

        if (oldSprite != null && newSprite != null) {
            status = DiffStatus.MODIFIED;
        } else if (oldSprite == null && newSprite != null) {
            status = DiffStatus.CREATED;
        } else if (oldSprite != null) {
            status = DiffStatus.DELETED;
        } else {
            throw new IllegalArgumentException("Original and Modified sprites are null. State name: " + stateName);
        }
    }
}
