package io.github.spair.byond.dmi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class DmiState {

    private DmiMeta.DmiMetaEntry metadata;
    private Map<SpriteDir, List<DmiSprite>> sprites;
    private boolean isDuplicate;
}
