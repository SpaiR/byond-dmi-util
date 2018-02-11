package io.github.spair.byond.dmi;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DmiMeta {

    private int spritesWidth;
    private int spritesHeight;
    private List<DmiMetaEntry> entries;

    public DmiMeta() { }

    public DmiMeta(int spritesWidth, int spritesHeight, List<DmiMetaEntry> entries) {
        this.spritesWidth = spritesWidth;
        this.spritesHeight = spritesHeight;
        this.entries = entries;
    }

    public int getSpritesWidth() {
        return spritesWidth;
    }

    public void setSpritesWidth(int spritesWidth) {
        this.spritesWidth = spritesWidth;
    }

    public int getSpritesHeight() {
        return spritesHeight;
    }

    public void setSpritesHeight(int spritesHeight) {
        this.spritesHeight = spritesHeight;
    }

    public List<DmiMetaEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<DmiMetaEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "DmiMeta{" +
                "spritesWidth=" + spritesWidth +
                ", spritesHeight=" + spritesHeight +
                ", entries=" + entries +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DmiMeta dmiMeta = (DmiMeta) o;
        return spritesWidth == dmiMeta.spritesWidth &&
                spritesHeight == dmiMeta.spritesHeight &&
                Objects.equals(entries, dmiMeta.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spritesWidth, spritesHeight, entries);
    }

    public static class DmiMetaEntry {

        private String name;
        private int dirs;
        private int frames;
        private int[] delay;
        private boolean loop;
        private boolean movement;
        private boolean rewind;

        public DmiMetaEntry() { }

        public DmiMetaEntry(String name, int dirs, int frames, int[] delay, boolean loop, boolean movement, boolean rewind) {
            this.name = name;
            this.dirs = dirs;
            this.frames = frames;
            this.delay = delay;
            this.loop = loop;
            this.movement = movement;
            this.rewind = rewind;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDirs() {
            return dirs;
        }

        public void setDirs(int dirs) {
            this.dirs = dirs;
        }

        public int getFrames() {
            return frames;
        }

        public void setFrames(int frames) {
            this.frames = frames;
        }

        public int[] getDelay() {
            return delay;
        }

        public void setDelay(int[] delay) {
            this.delay = delay;
        }

        public boolean isLoop() {
            return loop;
        }

        public void setLoop(boolean loop) {
            this.loop = loop;
        }

        public boolean isMovement() {
            return movement;
        }

        public void setMovement(boolean movement) {
            this.movement = movement;
        }

        public boolean isRewind() {
            return rewind;
        }

        public void setRewind(boolean rewind) {
            this.rewind = rewind;
        }

        @Override
        public String toString() {
            return "DmiMetaEntry{" +
                    "name='" + name + '\'' +
                    ", dirs=" + dirs +
                    ", frames=" + frames +
                    ", delay=" + Arrays.toString(delay) +
                    ", loop=" + loop +
                    ", movement=" + movement +
                    ", rewind=" + rewind +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DmiMetaEntry that = (DmiMetaEntry) o;
            return dirs == that.dirs &&
                    frames == that.frames &&
                    loop == that.loop &&
                    movement == that.movement &&
                    rewind == that.rewind &&
                    Objects.equals(name, that.name) &&
                    Arrays.equals(delay, that.delay);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(name, dirs, frames, loop, movement, rewind);
            result = 31 * result + Arrays.hashCode(delay);
            return result;
        }
    }
}
