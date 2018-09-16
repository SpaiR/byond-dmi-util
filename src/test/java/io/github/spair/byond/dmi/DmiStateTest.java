package io.github.spair.byond.dmi;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.*;

import static org.junit.Assert.*;

public class DmiStateTest {

    private BufferedImage mockedImage;

    @Before
    public void setUp() {
        mockedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    @Test
    public void testGetSprite() {
        DmiState dmiState = new DmiState();
        dmiState.setSprites(new HashMap<SpriteDir, List<DmiSprite>>() {
            {
                put(SpriteDir.SOUTH, Collections.singletonList(new DmiSprite(mockedImage, SpriteDir.SOUTH, 1)));
            }
        });

        assertNotEquals(Optional.empty(), dmiState.getSprite(SpriteDir.SOUTH));
        assertEquals(Optional.empty(), dmiState.getSprite(SpriteDir.NORTH));
    }

    @Test
    public void testGetSpriteWithFrame() {
        DmiState dmiState = new DmiState();
        dmiState.setSprites(new HashMap<SpriteDir, List<DmiSprite>>() {
            {
                put(SpriteDir.SOUTH, Collections.singletonList(new DmiSprite(mockedImage, SpriteDir.SOUTH, 1)));
                put(SpriteDir.EAST, Arrays.asList(
                        new DmiSprite(mockedImage, SpriteDir.EAST, 1),
                        new DmiSprite(mockedImage, SpriteDir.EAST, 2))
                );
            }
        });

        assertNotEquals(Optional.empty(), dmiState.getSprite(SpriteDir.SOUTH, 1));

        Optional<DmiSprite> sprite = dmiState.getSprite(SpriteDir.EAST, 2);

        assertTrue(sprite.isPresent());
        assertEquals(2, sprite.get().getFrameNum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSpriteWithIllegalFrame() {
        DmiState dmiState = new DmiState();
        dmiState.getSprite(SpriteDir.SOUTH, 0);
    }

    @Test
    public void testGetMetaProperties() {
        DmiState dmiState = new DmiState();
        dmiState.setMeta(new DmiMetaEntry("state", 4, 1, null, false, false, false, null));

        assertEquals("state", dmiState.getName());
        assertEquals(4, dmiState.getDirs());
        assertEquals(1, dmiState.getFrames());
        assertNull(dmiState.getDelay());
        assertFalse(dmiState.hasLoop());
        assertFalse(dmiState.isMovement());
        assertFalse(dmiState.hasRewind());
        assertNull(dmiState.getHotspot());
    }
}