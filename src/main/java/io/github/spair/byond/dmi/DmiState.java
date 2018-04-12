package io.github.spair.byond.dmi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class DmiState {

    private Meta meta;
    private Map<SpriteDir, List<DmiSprite>> sprites;
    private boolean isDuplicate;

    public String getStateName() {
        return meta.getName();
    }

    /**
     * Returns the first available sprite.
     * That means, that sprite will be the first frame and {@link SpriteDir#SOUTH} dir.
     * If sprite wasn't found result will be null.
     *
     * @return first available sprite
     */
    public DmiSprite getSprite() {
        return getSprite(SpriteDir.SOUTH);
    }

    /**
     * Returns the first available sprite of specified dir.
     * That means, that sprite will be the first frame of provided dir. If sprite wasn't found result will be null.
     *
     * @param dir dir to search sprite
     * @return sprite instance or null if not found
     */
    public DmiSprite getSprite(final SpriteDir dir) {
        List<DmiSprite> spriteList = sprites.get(dir);
        if (Objects.nonNull(spriteList) && !spriteList.isEmpty()) {
            return spriteList.get(0);
        } else {
            return null;
        }
    }

    /**
     * Returns sprite with specified dir and frame. If sprite wasn't found result will be null.
     * Frame count goes <b>from</b> '1' number, so if method get something lesser then it than exception will be thrown.
     *
     * @param dir   dir to search sprite
     * @param frame frame to search sprite
     * @return sprite instance or null if not found
     */
    public DmiSprite getSprite(final SpriteDir dir, final int frame) {
        if (frame <= 0) {
            throw new IllegalArgumentException("Frame count goes from 1 digit. Method received " + frame + " digit");
        }

        List<DmiSprite> spriteList = sprites.get(dir);
        if (Objects.nonNull(spriteList) && !spriteList.isEmpty() && spriteList.size() >= frame - 1) {
            return spriteList.get(frame - 1);
        } else {
            return null;
        }
    }

    /**
     * Returns the list of sprites for specified dir.
     *
     * @param dir dir to search sprite
     * @return list of sprites for specified dir
     */
    public List<DmiSprite> getSpriteList(final SpriteDir dir) {
        return sprites.get(dir);
    }
}
