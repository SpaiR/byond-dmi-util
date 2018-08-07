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
@SuppressWarnings("WeakerAccess")
public class DmiSprite {

    private BufferedImage sprite;
    private int width;
    private int height;
    private SpriteDir dir;
    private int frameNum;

    public DmiSprite(final BufferedImage sprite, final SpriteDir dir, final int frameNum) {
        this.sprite = sprite;
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        this.dir = dir;
        this.frameNum = frameNum;
    }

    @Override
    public String toString() {
        return "DmiSprite{"
                + "sprite=binary-image"
                + ", width=" + width
                + ", height=" + height
                + ", dir=" + dir
                + ", frameNum=" + frameNum
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
        DmiSprite sprite1 = (DmiSprite) o;
        return frameNum == sprite1.frameNum
                && dir == sprite1.dir
                && isEqualSprite(sprite1.sprite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sprite, dir, frameNum);
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
