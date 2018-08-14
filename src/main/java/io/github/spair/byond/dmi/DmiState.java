package io.github.spair.byond.dmi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class DmiState {

    private DmiMetaEntry meta;
    private Map<SpriteDir, List<DmiSprite>> sprites = new HashMap<>();
    private boolean isDuplicate;

    public String getName() {
        return meta.getName();
    }

    public void addSprite(final DmiSprite sprite) {
        List<DmiSprite> spriteList = sprites.get(sprite.getDir());
        if (spriteList == null) {
            spriteList = new ArrayList<>();
        }
        spriteList.add(sprite);
        sprites.putIfAbsent(sprite.getDir(), spriteList);
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
        final List<DmiSprite> spriteList = sprites.get(dir);
        return Optional.ofNullable(spriteList).map(dmiSprites -> dmiSprites.get(0));
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
            throw new IllegalArgumentException("Frame count goes from 1 digit. Method received " + frame + " digit");
        }

        final List<DmiSprite> spriteList = sprites.get(dir);
        return Optional.ofNullable(spriteList).map(
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
}
