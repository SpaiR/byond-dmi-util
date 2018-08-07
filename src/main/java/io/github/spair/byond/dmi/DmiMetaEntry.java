package io.github.spair.byond.dmi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class DmiMetaEntry {
    private String name;
    private int dirs;
    private int frames;
    private double[] delay;
    private boolean loop;
    private boolean movement;
    private boolean rewind;
    private double[] hotspot;
}
