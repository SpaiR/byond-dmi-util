package io.github.spair.byond.dmi;

import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Data
public class DmiDiff {

    @Nullable private DmiMeta oldMeta;
    @Nullable private DmiMeta newMeta;
    @Nonnull private List<Diff> diffs;

    /**
     * Shows that current diff was generated from the same Dmi's.
     * @return true, if Dmi's was the same, otherwise false
     */
    public boolean isSame() {
        return Objects.equals(oldMeta, newMeta) && diffs.isEmpty();
    }
}
