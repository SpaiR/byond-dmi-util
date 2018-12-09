package io.github.spair.byond.dmi.slurper;

import io.github.spair.byond.dmi.Dmi;
import io.github.spair.byond.dmi.DmiState;
import io.github.spair.byond.dmi.SpriteDir;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.*;

public class DmiSlurperTest {

    private final DmiSlurper dmiSlurper = new DmiSlurper();

    @Test
    public void testSlurpUpFromFile() {
        File dmiFile = new File("src/test/resources/rollerbed_original.dmi");
        Dmi dmi = dmiSlurper.slurpUp(dmiFile);
        commonDmiAssertion(dmi);
    }

    @Test
    public void testSlurpUpFromInputStream() throws Exception {
        File dmiFile = new File("src/test/resources/rollerbed_original.dmi");
        Dmi dmi = dmiSlurper.slurpUp("rollerbed_original.dmi", new FileInputStream(dmiFile));
        commonDmiAssertion(dmi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSlurpUpWithNiFileException() {
        dmiSlurper.slurpUp(new File("non existent path"));
    }

    @Test
    public void testSlurpUpWithDuplicateStates() {
        Dmi dmi = dmiSlurper.slurpUp(new File("src/test/resources/rollerbed_duplicate_states.dmi"));
        assertEquals(1, dmi.getDuplicateStatesNames().size());
        assertTrue(dmi.isHasDuplicates());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void commonDmiAssertion(Dmi dmi) {
        assertEquals("rollerbed_original.dmi", dmi.getName());
        assertEquals(160, dmi.getTotalWidth());
        assertEquals(128, dmi.getTotalHeight());

        assertEquals(32, dmi.getSpriteWidth());
        assertEquals(32, dmi.getSpriteHeight());

        DmiState downState = dmi.getState("down").get();

        assertEquals("down", downState.getName());
        assertEquals(1, downState.getDirs());
        assertEquals(1, downState.getFrames());
        assertNull(downState.getDelay());
        assertEquals(0, downState.getLoop());
        assertFalse(downState.isMovement());
        assertFalse(downState.isRewind());

        DmiState upState = dmi.getState("up").get();

        assertEquals("up", upState.getName());
        assertEquals(4, upState.getDirs());
        assertEquals(1, upState.getFrames());
        assertNull(upState.getDelay());
        assertEquals(1, upState.getLoop());
        assertFalse(upState.isMovement());
        assertTrue(upState.isRewind());

        DmiState foldedState = dmi.getState("folded").get();

        assertEquals("folded", foldedState.getName());
        assertEquals(4, foldedState.getDirs());
        assertEquals(3, foldedState.getFrames());
        assertArrayEquals(new double[]{1, 1, 1, 1, 1, 1}, foldedState.getDelay(), 0);
        assertEquals(0, downState.getLoop());
        assertFalse(foldedState.isMovement());
        assertFalse(foldedState.isRewind());

        assertEquals(new HashSet<>(Collections.singletonList(SpriteDir.SOUTH)), downState.getSprites().keySet());
        assertEquals(1, downState.getSpriteList(SpriteDir.SOUTH).get().size());
        assertEquals(SpriteDir.SOUTH, downState.getSprite(SpriteDir.SOUTH).get().getDir());
        assertEquals(1, downState.getSprite(SpriteDir.SOUTH).get().getFrameNumber());

        assertEquals(new HashSet<>(Arrays.asList(SpriteDir.SOUTH, SpriteDir.NORTH, SpriteDir.EAST, SpriteDir.WEST)), upState.getSprites().keySet());
        assertEquals(1, upState.getSpriteList(SpriteDir.SOUTH).get().size());
        assertEquals(SpriteDir.NORTH, upState.getSprite(SpriteDir.NORTH).get().getDir());
        assertEquals(1, upState.getSprite(SpriteDir.EAST).get().getFrameNumber());

        assertEquals(new HashSet<>(Arrays.asList(SpriteDir.SOUTH, SpriteDir.NORTH, SpriteDir.EAST, SpriteDir.WEST)), foldedState.getSprites().keySet());
        assertEquals(3, foldedState.getSpriteList(SpriteDir.SOUTH).get().size());
        assertEquals(SpriteDir.NORTH, foldedState.getSprite(SpriteDir.NORTH, 2).get().getDir());
        assertEquals(3, foldedState.getSprite(SpriteDir.EAST, 3).get().getFrameNumber());
    }
}
