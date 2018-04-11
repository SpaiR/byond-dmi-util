package io.github.spair.byond.dmi;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DmiStateTest {

    @Test
    public void testGetSprite() {
        DmiState dmiState = new DmiState();
        dmiState.setSprites(new HashMap<SpriteDir, List<DmiSprite>>() {
            {
                put(SpriteDir.SOUTH, Collections.singletonList(new DmiSprite(mock(BufferedImage.class), SpriteDir.SOUTH, 1)));
            }
        });

        assertNotNull(dmiState.getSprite(SpriteDir.SOUTH));
        assertNull(dmiState.getSprite(SpriteDir.NORTH));
    }

    @Test
    public void testGetSpriteWithFrame() {
        DmiState dmiState = new DmiState();
        dmiState.setSprites(new HashMap<SpriteDir, List<DmiSprite>>() {
            {
                put(SpriteDir.SOUTH, Collections.singletonList(new DmiSprite(mock(BufferedImage.class), SpriteDir.SOUTH, 1)));
                put(SpriteDir.EAST, Arrays.asList(
                        new DmiSprite(mock(BufferedImage.class), SpriteDir.EAST, 1),
                        new DmiSprite(mock(BufferedImage.class), SpriteDir.EAST, 2))
                );
            }
        });

        assertNotNull(dmiState.getSprite(SpriteDir.SOUTH, 1));

        DmiSprite sprite = dmiState.getSprite(SpriteDir.EAST, 2);
        assertNotNull(sprite);
        assertEquals(2, sprite.getFrameNum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSpriteWithIllegalFrame() {
        DmiState dmiState = new DmiState();
        dmiState.getSprite(SpriteDir.SOUTH, 0);
    }
}