package io.github.spair.byond.dmi;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@SuppressWarnings("WeakerAccess")
public class DmiDiff {

    private boolean same;
    private DmiMeta originalMeta;
    private DmiMeta modifiedMeta;
    private List<DiffEntry> diffEntries;

    @Data
    public static class DiffEntry {

        private String stateName;
        private DmiSprite originalSprite;
        private DmiSprite modifiedSprite;
        private Status status;

        public DiffEntry(final String stateName, final DmiSprite originalSprite, final DmiSprite modifiedSprite) {
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
