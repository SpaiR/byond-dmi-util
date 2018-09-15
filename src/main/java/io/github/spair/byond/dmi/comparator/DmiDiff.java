package io.github.spair.byond.dmi.comparator;

import io.github.spair.byond.dmi.DmiMeta;
import lombok.Data;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Data
@SuppressWarnings("WeakerAccess")
public class DmiDiff implements Iterable<DmiDiffEntry> {

    private DmiMeta oldMeta;
    private DmiMeta newMeta;
    private List<DmiDiffEntry> dmiDiffEntries;

    DmiDiff(final List<DmiDiffEntry> dmiDiffEntries) {
        this.dmiDiffEntries = dmiDiffEntries;
    }

    /**
     * Shows that current diff was generated from the same Dmi's.
     * @return true, if Dmi's was the same, otherwise false
     */
    public boolean isSame() {
        return Objects.equals(oldMeta, newMeta) && dmiDiffEntries.isEmpty();
    }

    @Override
    public Iterator<DmiDiffEntry> iterator() {
        return dmiDiffEntries.iterator();
    }
}
