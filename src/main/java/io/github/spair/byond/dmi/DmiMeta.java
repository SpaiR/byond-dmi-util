package io.github.spair.byond.dmi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class DmiMeta {

    private int spritesWidth;
    private int spritesHeight;
    private List<Meta> metas;
}
