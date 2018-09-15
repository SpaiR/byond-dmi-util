package io.github.spair.byond.dmi.slurper;

import io.github.spair.byond.dmi.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public void testSlurpUpFromBase64Content() throws Exception {
        String filePath = "src/test/resources/rollerbed_original_base64.txt";
        Dmi dmi = dmiSlurper.slurpUp("rollerbed_original.dmi", new String(Files.readAllBytes(Paths.get(filePath))));
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
        assertEquals(160, dmi.getWidth());
        assertEquals(128, dmi.getHeight());

        DmiMeta dmiMeta = dmi.getMetadata();

        assertEquals(32, dmiMeta.getSpritesWidth());
        assertEquals(32, dmiMeta.getSpritesHeight());

        DmiMetaEntry downStateMeta = dmiMeta.getMetas().get(0);

        assertEquals("down", downStateMeta.getName());
        assertEquals(1, downStateMeta.getDirs());
        assertEquals(1, downStateMeta.getFrames());
        assertNull(downStateMeta.getDelay());
        assertFalse(downStateMeta.isLoop());
        assertFalse(downStateMeta.isMovement());
        assertFalse(downStateMeta.isRewind());

        DmiMetaEntry upStateMeta = dmiMeta.getMetas().get(1);

        assertEquals("up", upStateMeta.getName());
        assertEquals(4, upStateMeta.getDirs());
        assertEquals(1, upStateMeta.getFrames());
        assertNull(upStateMeta.getDelay());
        assertTrue(upStateMeta.isLoop());
        assertFalse(upStateMeta.isMovement());
        assertTrue(upStateMeta.isRewind());

        DmiMetaEntry foldedStateMeta = dmiMeta.getMetas().get(2);

        assertEquals("folded", foldedStateMeta.getName());
        assertEquals(4, foldedStateMeta.getDirs());
        assertEquals(3, foldedStateMeta.getFrames());
        assertArrayEquals(new double[]{1, 1, 1, 1, 1, 1},foldedStateMeta.getDelay(), 0);
        assertFalse(foldedStateMeta.isLoop());
        assertFalse(foldedStateMeta.isMovement());
        assertFalse(foldedStateMeta.isRewind());

        DmiState downState = dmi.getState("down").get();

        assertEquals(downStateMeta, downState.getMeta());
        assertEquals(new HashSet<>(Collections.singletonList(SpriteDir.SOUTH)), downState.getSprites().keySet());
        assertEquals(1, downState.getSpriteList(SpriteDir.SOUTH).get().size());
        assertEquals(SpriteDir.SOUTH, downState.getSprite(SpriteDir.SOUTH).get().getDir());
        assertEquals(1, downState.getSprite(SpriteDir.SOUTH).get().getFrameNum());

        DmiState upState = dmi.getState("up").get();

        assertEquals(upStateMeta, upState.getMeta());
        assertEquals(new HashSet<>(Arrays.asList(SpriteDir.SOUTH, SpriteDir.NORTH, SpriteDir.EAST, SpriteDir.WEST)), upState.getSprites().keySet());
        assertEquals(1, upState.getSpriteList(SpriteDir.SOUTH).get().size());
        assertEquals(SpriteDir.NORTH, upState.getSprite(SpriteDir.NORTH).get().getDir());
        assertEquals(1, upState.getSprite(SpriteDir.EAST).get().getFrameNum());

        DmiState foldedState = dmi.getState("folded").get();

        assertEquals(foldedStateMeta, foldedState.getMeta());
        assertEquals(new HashSet<>(Arrays.asList(SpriteDir.SOUTH, SpriteDir.NORTH, SpriteDir.EAST, SpriteDir.WEST)), foldedState.getSprites().keySet());
        assertEquals(3, foldedState.getSpriteList(SpriteDir.SOUTH).get().size());
        assertEquals(SpriteDir.NORTH, foldedState.getSprite(SpriteDir.NORTH, 2).get().getDir());
        assertEquals(3, foldedState.getSprite(SpriteDir.EAST, 3).get().getFrameNum());
    }
}
