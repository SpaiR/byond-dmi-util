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
    private DmiMeta originalMeta;
    @Nullable
    private DmiMeta modifiedMeta;
    @Nonnull
    private List<DiffEntry> diffEntries;

    public boolean isSame() {
        return Objects.equals(originalMeta, modifiedMeta) && diffEntries.isEmpty();
    }

    @Data
    public static class DiffEntry {

        @Nonnull
        private String stateName;
        @Nullable
        private DmiSprite originalSprite;
        @Nullable
        private DmiSprite modifiedSprite;
        private Status status;

        public DiffEntry(@Nonnull final String stateName,
                         @Nullable final DmiSprite originalSprite,
                         @Nullable final DmiSprite modifiedSprite) {
            this.stateName = stateName;
            this.originalSprite = originalSprite;
            this.modifiedSprite = modifiedSprite;

            if (Objects.nonNull(originalSprite) && Objects.nonNull(modifiedSprite)) {
                status = Status.MODIFIED;
            } else if (Objects.isNull(originalSprite) && Objects.nonNull(modifiedSprite)) {
                status = Status.CREATED;
            } else if (Objects.nonNull(originalSprite)) {
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
