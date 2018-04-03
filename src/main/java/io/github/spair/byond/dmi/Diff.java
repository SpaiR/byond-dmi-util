package io.github.spair.byond.dmi;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@Data
@SuppressWarnings("WeakerAccess")
public class Diff {

    @Nonnull private String stateName;
    @Nullable private DmiSprite oldSprite;
    @Nullable private DmiSprite newSprite;

    @Setter(AccessLevel.NONE)
    @Nonnull private DiffStatus status;

    public Diff(@Nonnull final String stateName,
                @Nullable final DmiSprite oldSprite,
                @Nullable final DmiSprite newSprite) {
        this.stateName = stateName;
        this.oldSprite = oldSprite;
        this.newSprite = newSprite;

        if (Objects.nonNull(oldSprite) && Objects.nonNull(newSprite)) {
            status = DiffStatus.MODIFIED;
        } else if (Objects.isNull(oldSprite) && Objects.nonNull(newSprite)) {
            status = DiffStatus.CREATED;
        } else if (Objects.nonNull(oldSprite)) {
            status = DiffStatus.DELETED;
        } else {
            throw new IllegalArgumentException("Original and Modified sprites are null. State name: " + stateName);
        }
    }
}
