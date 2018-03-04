package io.github.spair.byond.dmi;

import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Data
@SuppressWarnings("WeakerAccess")
public class DmiDiff {

    @Nullable
    private DmiMeta oldMeta;
    @Nullable
    private DmiMeta newMeta;
    @Nonnull
    private List<DiffEntry> diffEntries;

    public boolean isSame() {
        return Objects.equals(oldMeta, newMeta) && diffEntries.isEmpty();
    }

    @Data
    public static class DiffEntry {

        @Nonnull
        private String stateName;
        @Nullable
        private DmiSprite oldSprite;
        @Nullable
        private DmiSprite newSprite;
        private Status status;

        public DiffEntry(@Nonnull final String stateName,
                         @Nullable final DmiSprite oldSprite,
                         @Nullable final DmiSprite newSprite) {
            this.stateName = stateName;
            this.oldSprite = oldSprite;
            this.newSprite = newSprite;

            if (Objects.nonNull(oldSprite) && Objects.nonNull(newSprite)) {
                status = Status.MODIFIED;
            } else if (Objects.isNull(oldSprite) && Objects.nonNull(newSprite)) {
                status = Status.CREATED;
            } else if (Objects.nonNull(oldSprite)) {
                status = Status.DELETED;
            } else {
                throw new IllegalArgumentException("Original and Modified sprites are null. State name: " + stateName);
            }
        }
    }

    public enum Status {
        CREATED, MODIFIED, DELETED
    }
}
