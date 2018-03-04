package io.github.spair.byond.dmi;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.Objects;

@Data
@AllArgsConstructor
public class DmiSprite {

    @Nonnull
    private BufferedImage sprite;
    @Nonnull
    private SpriteDir spriteDir;
    private int spriteFrameNum;

    public String getSpriteAsBase64() {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(sprite, "PNG", os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String toString() {
        return "DmiSprite{"
                + "sprite=binary-image"
                + ", spriteDir=" + spriteDir
                + ", spriteFrameNum=" + spriteFrameNum
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
        return spriteFrameNum == sprite1.spriteFrameNum
                && spriteDir == sprite1.spriteDir
                && isEqualSprite(sprite1.sprite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sprite, spriteDir, spriteFrameNum);
    }

    private boolean isEqualSprite(final BufferedImage spriteToCompare) {
        if (sprite == spriteToCompare) {
            return true;
        }

        if (sprite.getHeight() != spriteToCompare.getHeight() || sprite.getWidth() != spriteToCompare.getWidth()) {
            return false;
        }

        for (int x = 0; x < sprite.getWidth(); x++) {
            for (int y = 0; y < sprite.getHeight(); y++) {
                if (sprite.getRGB(x, y) != spriteToCompare.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
}
