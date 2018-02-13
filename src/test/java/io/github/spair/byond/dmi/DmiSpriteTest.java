package io.github.spair.byond.dmi;

import org.junit.Test;

import javax.imageio.ImageIO;

import java.io.File;

import static org.junit.Assert.*;

public class DmiSpriteTest {

    @Test
    public void testEquals() throws Exception {
        final String catImagePage = "src/test/resources/cat_original.png";

        DmiSprite cat1 = new DmiSprite();
        cat1.setSprite(ImageIO.read(new File(catImagePage)));

        DmiSprite cat2 = new DmiSprite();
        cat2.setSprite(ImageIO.read(new File(catImagePage)));

        assertEquals(cat1, cat2);
    }

    @Test
    public void testEqualsWithDifferentImages() throws Exception {
        DmiSprite cat1 = new DmiSprite();
        cat1.setSprite(ImageIO.read(new File("src/test/resources/cat_original.png")));

        DmiSprite cat2 = new DmiSprite();
        cat2.setSprite(ImageIO.read(new File("src/test/resources/cat_original_diff.png")));

        assertNotEquals(cat1, cat2);
    }

    @Test
    public void testEqualsWithDifferentFields() {
        DmiSprite dmiSprite1 = new DmiSprite();
        dmiSprite1.setSpriteDir(SpriteDir.SOUTH);
        dmiSprite1.setSpriteFrameNum(1);

        DmiSprite dmiSprite2 = new DmiSprite();
        dmiSprite2.setSpriteDir(SpriteDir.NORTH);
        dmiSprite2.setSpriteFrameNum(1);

        assertNotEquals(dmiSprite1, dmiSprite2);

        dmiSprite2.setSpriteDir(SpriteDir.SOUTH);
        assertEquals(dmiSprite1, dmiSprite2);

        dmiSprite2.setSpriteFrameNum(2);
        assertNotEquals(dmiSprite1, dmiSprite2);
    }
}