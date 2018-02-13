package io.github.spair.byond.dmi;

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.*;

public class DmiSlurperTest {

    @Test
    public void testSlurpUpFromFile() throws Exception {
        File dmiFile = new File("src/test/resources/rollerbed_original.dmi");
        Dmi dmi = DmiSlurper.slurpUp(dmiFile);
        commonDmiAssertion(dmi);
    }

    @Test
    public void testSlurpUpFromInputStream() throws Exception {
        File dmiFile = new File("src/test/resources/rollerbed_original.dmi");
        Dmi dmi = DmiSlurper.slurpUp("rollerbed_original.dmi", new FileInputStream(dmiFile));
        commonDmiAssertion(dmi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSlurpUpWithNiFileException() throws Exception {
        DmiSlurper.slurpUp(new File("non existent path"));
    }

    @Test
    public void testSlurpUpWithDuplicateStates() throws Exception {
        Dmi dmi = DmiSlurper.slurpUp(new File("src/test/resources/rollerbed_duplicate_states.dmi"));
        assertEquals(1, dmi.getDuplicateStatesNames().size());
        assertTrue(dmi.isHasDuplicates());
    }

    private void commonDmiAssertion(Dmi dmi) {
        assertEquals("rollerbed_original.dmi", dmi.getName());
        assertEquals(160, dmi.getWidth());
        assertEquals(128, dmi.getHeight());

        DmiMeta dmiMeta = dmi.getMetadata();

        assertEquals(32, dmiMeta.getSpritesWidth());
        assertEquals(32, dmiMeta.getSpritesHeight());

        DmiMeta.DmiMetaEntry downStateMeta = dmiMeta.getEntries().get(0);

        assertEquals("down", downStateMeta.getName());
        assertEquals(1, downStateMeta.getDirs());
        assertEquals(1, downStateMeta.getFrames());
        assertNull(downStateMeta.getDelay());
        assertFalse(downStateMeta.isLoop());
        assertFalse(downStateMeta.isMovement());
        assertFalse(downStateMeta.isRewind());

        DmiMeta.DmiMetaEntry upStateMeta = dmiMeta.getEntries().get(1);

        assertEquals("up", upStateMeta.getName());
        assertEquals(4, upStateMeta.getDirs());
        assertEquals(1, upStateMeta.getFrames());
        assertNull(upStateMeta.getDelay());
        assertTrue(upStateMeta.isLoop());
        assertFalse(upStateMeta.isMovement());
        assertTrue(upStateMeta.isRewind());

        DmiMeta.DmiMetaEntry foldedStateMeta = dmiMeta.getEntries().get(2);

        assertEquals("folded", foldedStateMeta.getName());
        assertEquals(4, foldedStateMeta.getDirs());
        assertEquals(3, foldedStateMeta.getFrames());
        assertArrayEquals(new int[]{1, 1, 1, 1, 1, 1},foldedStateMeta.getDelay());
        assertFalse(foldedStateMeta.isLoop());
        assertFalse(foldedStateMeta.isMovement());
        assertFalse(foldedStateMeta.isRewind());

        DmiState downState = dmi.getStates().get("down");

        assertEquals(downStateMeta, downState.getMetadata());
        assertEquals(new HashSet<>(Collections.singletonList(SpriteDir.SOUTH)), downState.getSprites().keySet());
        assertEquals(1, downState.getSprites().get(SpriteDir.SOUTH).size());
        assertEquals(SpriteDir.SOUTH, downState.getSprites().get(SpriteDir.SOUTH).get(0).getSpriteDir());
        assertEquals(1, downState.getSprites().get(SpriteDir.SOUTH).get(0).getSpriteFrameNum());

        DmiState upState = dmi.getStates().get("up");

        assertEquals(upStateMeta, upState.getMetadata());
        assertEquals(new HashSet<>(Arrays.asList(SpriteDir.SOUTH, SpriteDir.NORTH, SpriteDir.EAST, SpriteDir.WEST)), upState.getSprites().keySet());
        assertEquals(1, upState.getSprites().get(SpriteDir.SOUTH).size());
        assertEquals(SpriteDir.NORTH, upState.getSprites().get(SpriteDir.NORTH).get(0).getSpriteDir());
        assertEquals(1, upState.getSprites().get(SpriteDir.EAST).get(0).getSpriteFrameNum());

        DmiState foldedState = dmi.getStates().get("folded");

        assertEquals(foldedStateMeta, foldedState.getMetadata());
        assertEquals(new HashSet<>(Arrays.asList(SpriteDir.SOUTH, SpriteDir.NORTH, SpriteDir.EAST, SpriteDir.WEST)), foldedState.getSprites().keySet());
        assertEquals(3, foldedState.getSprites().get(SpriteDir.SOUTH).size());
        assertEquals(SpriteDir.NORTH, foldedState.getSprites().get(SpriteDir.NORTH).get(1).getSpriteDir());
        assertEquals(3, foldedState.getSprites().get(SpriteDir.EAST).get(2).getSpriteFrameNum());
    }
}
