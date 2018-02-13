package io.github.spair.byond.dmi;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DmiState {

    private DmiMeta.DmiMetaEntry metadata;
    private Map<SpriteDir, List<DmiSprite>> sprites;
    private boolean isDuplicate;

    public DmiState() { }

    public DmiState(DmiMeta.DmiMetaEntry metadata, Map<SpriteDir, List<DmiSprite>> sprites, boolean isDuplicate) {
        this.metadata = metadata;
        this.sprites = sprites;
        this.isDuplicate = isDuplicate;
    }

    public DmiMeta.DmiMetaEntry getMetadata() {
        return metadata;
    }

    public void setMetadata(DmiMeta.DmiMetaEntry metadata) {
        this.metadata = metadata;
    }

    public Map<SpriteDir, List<DmiSprite>> getSprites() {
        return sprites;
    }

    public void setSprites(Map<SpriteDir, List<DmiSprite>> sprites) {
        this.sprites = sprites;
    }

    public boolean isDuplicate() {
        return isDuplicate;
    }

    public void setDuplicate(boolean isDuplicate) {
        this.isDuplicate = isDuplicate;
    }

    @Override
    public String toString() {
        return "DmiState{" +
                "metadata=" + metadata +
                ", sprites=" + sprites +
                ", isDuplicate=" + isDuplicate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DmiState)) return false;
        DmiState dmiState = (DmiState) o;
        return isDuplicate == dmiState.isDuplicate &&
                Objects.equals(metadata, dmiState.metadata) &&
                Objects.equals(sprites, dmiState.sprites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata, sprites, isDuplicate);
    }
}
