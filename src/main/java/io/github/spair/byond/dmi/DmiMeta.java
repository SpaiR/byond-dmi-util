package io.github.spair.byond.dmi;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
