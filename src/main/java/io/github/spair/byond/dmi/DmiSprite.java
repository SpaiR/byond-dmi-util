package io.github.spair.byond.dmi;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Objects;

@Data
@AllArgsConstructor
public class DmiSprite {

    private BufferedImage sprite;
    private SpriteDir dir;

    private int width = Dmi.DEFAULT_SPRITE_SIZE;
    private int height = Dmi.DEFAULT_SPRITE_SIZE;

    private int frameNumber = 1;

    public DmiSprite(final BufferedImage sprite, final SpriteDir dir, final int frameNum) {
        this.sprite = sprite;
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        this.dir = dir;
        this.frameNumber = frameNum;
    }

    @Override
    public String toString() {
        return "DmiSprite{"
                + "sprite=binary-image"
                + ", dir=" + dir
                + ", width=" + width
                + ", height=" + height
                + ", frameNumber=" + frameNumber
                + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DmiSprite otherDmiSprite = (DmiSprite) o;
        return frameNumber == otherDmiSprite.frameNumber
                && dir == otherDmiSprite.dir
                && isEqualSprite(otherDmiSprite.sprite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sprite, dir, frameNumber);
    }

    private boolean isEqualSprite(final BufferedImage spriteToCheck) {
        if (sprite == spriteToCheck) {
            return true;
        }
        if (sprite.getWidth() != spriteToCheck.getWidth() || sprite.getHeight() != spriteToCheck.getHeight()) {
            return false;
        }

        DataBuffer actual = sprite.getData().getDataBuffer();
        DataBuffer toCheck = spriteToCheck.getData().getDataBuffer();

        if (actual.getClass() != toCheck.getClass()) {
            return false;
        }

        // DataBufferByte - for RGB images, DataBufferInt - for ARGB
        if (actual instanceof DataBufferByte) {
            return Arrays.equals(((DataBufferByte) actual).getData(), ((DataBufferByte) toCheck).getData());
        } else if (actual instanceof DataBufferInt) {
            return Arrays.equals(((DataBufferInt) actual).getData(), ((DataBufferInt) toCheck).getData());
        } else {
            throw new IllegalStateException();
        }
    }
}
