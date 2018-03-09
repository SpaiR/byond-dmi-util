package io.github.spair.byond.dmi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DmiMeta {

    private int spritesWidth;
    private int spritesHeight;
    private List<DmiMetaEntry> entries;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuppressWarnings("WeakerAccess")
    public static class DmiMetaEntry {

        private String name;
        private int dirs;
        private int frames;
        private double[] delay;
        private boolean loop;
        private boolean movement;
        private boolean rewind;
        private double[] hotspot;
    }
}
