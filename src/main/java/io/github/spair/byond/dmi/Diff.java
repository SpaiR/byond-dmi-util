package io.github.spair.byond.dmi;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Data
@Setter(AccessLevel.PACKAGE)
@SuppressWarnings("WeakerAccess")
public class Diff {

    @Nonnull private String stateName;
    @Nullable private DmiSprite oldSprite;
    @Nullable private DmiSprite newSprite;

    @Setter(AccessLevel.NONE)
    @Nonnull private DiffStatus status;

    Diff(@Nonnull final String stateName, @Nullable final DmiSprite oldSprite, @Nullable final DmiSprite newSprite) {
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
