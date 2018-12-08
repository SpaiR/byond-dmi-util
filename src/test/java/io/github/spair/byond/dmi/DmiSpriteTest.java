package io.github.spair.byond.dmi;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DmiSpriteTest {

    private static final String ORIGINAL_CAT_IMAGE_PATH = "src/test/resources/cat_original.png";
    private static final String DIFF_CAT_IMAGE_PATH = "src/test/resources/cat_original_diff.png";

    @Test
    public void testEquals() throws Exception {
        DmiSprite cat1 = new DmiSprite(ImageIO.read(new File(ORIGINAL_CAT_IMAGE_PATH)), SpriteDir.SOUTH, 0);
        DmiSprite cat2 = new DmiSprite(ImageIO.read(new File(ORIGINAL_CAT_IMAGE_PATH)), SpriteDir.SOUTH, 0);
        assertEquals(cat1, cat2);
    }

    @Test
    public void testEqualsWithDifferentImages() throws Exception {
        DmiSprite cat1 = new DmiSprite(ImageIO.read(new File(ORIGINAL_CAT_IMAGE_PATH)), SpriteDir.SOUTH, 0);
        DmiSprite cat2 = new DmiSprite(ImageIO.read(new File(DIFF_CAT_IMAGE_PATH)), SpriteDir.SOUTH, 0);
        assertNotEquals(cat1, cat2);
    }

    @Test
    public void testEqualsWithDifferentFields() throws Exception {
        DmiSprite dmiSprite1 = new DmiSprite(ImageIO.read(new File(ORIGINAL_CAT_IMAGE_PATH)), SpriteDir.SOUTH, 0);
        DmiSprite dmiSprite2 = new DmiSprite(ImageIO.read(new File(ORIGINAL_CAT_IMAGE_PATH)), SpriteDir.NORTH, 0);

        assertNotEquals(dmiSprite1, dmiSprite2);

        dmiSprite2.setDir(SpriteDir.SOUTH);
        assertEquals(dmiSprite1, dmiSprite2);

        dmiSprite2.setFrameNumber(2);
        assertNotEquals(dmiSprite1, dmiSprite2);
    }
}