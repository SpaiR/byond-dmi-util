package io.github.spair.byond.dmi;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Iterator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class DmiState implements Iterable<Map.Entry<SpriteDir, List<DmiSprite>>> {

    private String name = "";
    private int dirs;
    private int frames;
    private double[] delay;
    private boolean loop;
    private boolean movement;
    private boolean rewind;
    private double[] hotspot;

    @Setter(AccessLevel.NONE)
    private Map<SpriteDir, List<DmiSprite>> sprites = new TreeMap<>(new SpriteDirComparator());
    @Setter(AccessLevel.NONE)
    private List<DmiState> duplicates = new ArrayList<>();

    public boolean hasDuplicates() {
        return !duplicates.isEmpty();
    }

    public void addSprite(final DmiSprite sprite) {
        List<DmiSprite> spriteList = sprites.computeIfAbsent(sprite.getDir(), k -> new ArrayList<>());
        spriteList.add(sprite);
    }

    public void addDuplicate(final DmiState dmiState) {
        duplicates.add(dmiState);
    }

    /**
     * Returns the first available sprite.
     * That means, that sprite will be the first frame and {@link SpriteDir#SOUTH} dir.
     *
     * @return first available sprite
     */
    public Optional<DmiSprite> getSprite() {
        return getSprite(SpriteDir.SOUTH);
    }

    /**
     * Returns the first available sprite of specified dir.
     * That means, that sprite will be the first frame of provided dir. If sprite wasn't found result will be null.
     *
     * @param dir dir to search sprite
     * @return optional sprite instance
     */
    public Optional<DmiSprite> getSprite(final SpriteDir dir) {
        return Optional.ofNullable(sprites.get(dir)).map(dmiSprites -> dmiSprites.get(0));
    }

    /**
     * Returns sprite with specified dir and frame. If sprite wasn't found result will be null.
     * Frame count goes <b>from</b> '1' number, so if method get something lesser then it than exception will be thrown.
     *
     * @param dir   dir to search sprite
     * @param frame frame to search sprite
     * @return optional sprite instance
     */
    public Optional<DmiSprite> getSprite(final SpriteDir dir, final int frame) {
        if (frame <= 0) {
            throw new IllegalArgumentException("Frame count goes from 1 digit. Received: " + frame);
        }
        return Optional.ofNullable(sprites.get(dir)).map(
                dmiSprites -> dmiSprites.size() >= frame - 1 ? dmiSprites.get(frame - 1) : null
        );
    }

    /**
     * Returns the list of sprites for specified dir.
     *
     * @param dir dir to search sprite
     * @return optional list of sprites for specified dir
     */
    public Optional<List<DmiSprite>> getSpriteList(final SpriteDir dir) {
        return Optional.ofNullable(sprites.get(dir));
    }

    @Override
    public Iterator<Map.Entry<SpriteDir, List<DmiSprite>>> iterator() {
        return sprites.entrySet().iterator();
    }
}
