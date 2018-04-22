package io.github.spair.byond.dmi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class DmiState {

    private Meta meta;
    private Map<SpriteDir, List<DmiSprite>> sprites = new HashMap<>();
    private boolean isDuplicate;

    public String getStateName() {
        return meta.getName();
    }

    public void addSprite(final DmiSprite sprite) {
        List<DmiSprite> spriteList = sprites.getOrDefault(sprite.getDir(), new ArrayList<>());
        spriteList.add(sprite);
        sprites.putIfAbsent(sprite.getDir(), spriteList);
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
        final List<DmiSprite> spriteList = sprites.get(dir);
        return CheckSupplierUtil.returnIfNonNullAndTrue(spriteList, () -> !spriteList.isEmpty(), s -> s.get(0));
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

        final List<DmiSprite> spriteList = sprites.get(dir);
        return CheckSupplierUtil.returnIfNonNullAndTrue(
                spriteList, () -> (!spriteList.isEmpty() && spriteList.size() >= frame - 1), s -> s.get(frame - 1)
        );
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
