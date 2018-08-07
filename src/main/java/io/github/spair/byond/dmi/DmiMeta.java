package io.github.spair.byond.dmi;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class DmiMeta implements Iterable<DmiMetaEntry> {

    private int spritesWidth;
    private int spritesHeight;
    private List<DmiMetaEntry> metas = Collections.emptyList();

    @Override
    public Iterator<DmiMetaEntry> iterator() {
        return metas.iterator();
    }
}
