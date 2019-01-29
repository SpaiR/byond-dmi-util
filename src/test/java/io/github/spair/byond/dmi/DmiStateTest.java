package io.github.spair.byond.dmi;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.Optional;

import static org.junit.Assert.*;

public class DmiStateTest {

    private BufferedImage mockedImage;
    private DmiState dmiState;

    @Before
    public void setUp() {
        mockedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        dmiState = new DmiState();
    }

    @Test
    public void testGetSprite() {
        dmiState.addSprite(new DmiSprite(mockedImage, SpriteDir.SOUTH, 1));

        assertNotEquals(Optional.empty(), dmiState.getSpriteSafe(SpriteDir.SOUTH));
        assertEquals(Optional.empty(), dmiState.getSpriteSafe(SpriteDir.NORTH));
    }

    @Test
    public void testGetSpriteWithFrame() {
        dmiState.addSprite(new DmiSprite(mockedImage, SpriteDir.SOUTH, 1));
        dmiState.addSprite(new DmiSprite(mockedImage, SpriteDir.EAST, 1));
        dmiState.addSprite(new DmiSprite(mockedImage, SpriteDir.EAST, 2));

        assertNotEquals(Optional.empty(), dmiState.getSpriteSafe(SpriteDir.SOUTH, 1));

        Optional<DmiSprite> sprite = dmiState.getSpriteSafe(SpriteDir.EAST, 2);

        assertTrue(sprite.isPresent());
        assertEquals(2, sprite.get().getFrameNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSpriteWithIllegalFrame() {
        dmiState.getSpriteSafe(SpriteDir.SOUTH, 0);
    }
}