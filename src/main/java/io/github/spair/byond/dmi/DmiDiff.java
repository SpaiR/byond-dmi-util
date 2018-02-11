package io.github.spair.byond.dmi;

import java.util.List;
import java.util.Objects;

public class DmiDiff {

    private boolean same;
    private DmiMeta originalMeta;
    private DmiMeta modifiedMeta;
    private List<DiffEntry> diffEntries;

    public DmiDiff() { }

    public DmiDiff(boolean same, DmiMeta originalMeta, DmiMeta modifiedMeta, List<DiffEntry> diffEntries) {
        this.same = same;
        this.originalMeta = originalMeta;
        this.modifiedMeta = modifiedMeta;
        this.diffEntries = diffEntries;
    }

    public boolean isSame() {
        return same;
    }

    public void setSame(boolean same) {
        this.same = same;
    }

    public DmiMeta getOriginalMeta() {
        return originalMeta;
    }

    public void setOriginalMeta(DmiMeta originalMeta) {
        this.originalMeta = originalMeta;
    }

    public DmiMeta getModifiedMeta() {
        return modifiedMeta;
    }

    public void setModifiedMeta(DmiMeta modifiedMeta) {
        this.modifiedMeta = modifiedMeta;
    }

    public List<DiffEntry> getDiffEntries() {
        return diffEntries;
    }

    public void setDiffEntries(List<DiffEntry> diffEntries) {
        this.diffEntries = diffEntries;
    }

    @Override
    public String toString() {
        return "DmiDiff{" +
                "same=" + same +
                ", originalMeta=" + originalMeta +
                ", modifiedMeta=" + modifiedMeta +
                ", diffEntries=" + diffEntries +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DmiDiff dmiDiff = (DmiDiff) o;
        return same == dmiDiff.same &&
                Objects.equals(originalMeta, dmiDiff.originalMeta) &&
                Objects.equals(modifiedMeta, dmiDiff.modifiedMeta) &&
                Objects.equals(diffEntries, dmiDiff.diffEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(same, originalMeta, modifiedMeta, diffEntries);
    }

    public static class DiffEntry {

        private String stateName;
        private DmiSprite originalSprite;
        private DmiSprite modifiedSprite;
        private Status status;

        public DiffEntry(String stateName, DmiSprite originalSprite, DmiSprite modifiedSprite) {
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

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public DmiSprite getOriginalSprite() {
            return originalSprite;
        }

        public void setOriginalSprite(DmiSprite originalSprite) {
            this.originalSprite = originalSprite;
        }

        public DmiSprite getModifiedSprite() {
            return modifiedSprite;
        }

        public void setModifiedSprite(DmiSprite modifiedSprite) {
            this.modifiedSprite = modifiedSprite;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "DiffEntry{" +
                    "stateName='" + stateName + '\'' +
                    ", originalSprite=" + originalSprite +
                    ", modifiedSprite=" + modifiedSprite +
                    ", status=" + status +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DiffEntry diffEntry = (DiffEntry) o;
            return Objects.equals(stateName, diffEntry.stateName) &&
                    Objects.equals(originalSprite, diffEntry.originalSprite) &&
                    Objects.equals(modifiedSprite, diffEntry.modifiedSprite) &&
                    status == diffEntry.status;
        }

        @Override
        public int hashCode() {
            return Objects.hash(stateName, originalSprite, modifiedSprite, status);
        }
    }

    public enum Status {
        CREATED, MODIFIED, DELETED
    }
}
