package io.github.spair.byond.dmi;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class DmiSprite {

    private BufferedImage sprite;
    private SpriteDir spriteDir;
    private int spriteFrameNum;

    public DmiSprite() { }

    public DmiSprite(BufferedImage sprite, SpriteDir spriteDir, int spriteFrameNum) {
        this.sprite = sprite;
        this.spriteDir = spriteDir;
        this.spriteFrameNum = spriteFrameNum;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public SpriteDir getSpriteDir() {
        return spriteDir;
    }

    public void setSpriteDir(SpriteDir spriteDir) {
        this.spriteDir = spriteDir;
    }

    public int getSpriteFrameNum() {
        return spriteFrameNum;
    }

    public void setSpriteFrameNum(int spriteFrameNum) {
        this.spriteFrameNum = spriteFrameNum;
    }

    @Override
    public String toString() {
        return "DmiSprite{" +
                "sprite=binary-image" +
                ", spriteDir=" + spriteDir +
                ", spriteFrameNum=" + spriteFrameNum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DmiSprite sprite1 = (DmiSprite) o;
        return spriteFrameNum == sprite1.spriteFrameNum && spriteDir == sprite1.spriteDir && isEqualSprite(sprite1.sprite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sprite, spriteDir, spriteFrameNum);
    }

    private boolean isEqualSprite(BufferedImage spriteToCompare) {
        if (sprite.getHeight() != spriteToCompare.getHeight() || sprite.getWidth() != spriteToCompare.getWidth())
            return false;

        for (int x = 0; x < sprite.getWidth(); x++) {
            for (int y = 0; y < sprite.getHeight(); y++) {
                if (sprite.getRGB(x, y) != spriteToCompare.getRGB(x, y))
                    return false;
            }
        }

        return true;
    }
}
