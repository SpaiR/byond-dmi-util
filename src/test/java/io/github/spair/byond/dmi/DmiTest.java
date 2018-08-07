package io.github.spair.byond.dmi;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.*;

import static org.junit.Assert.*;

public class DmiTest {

    private BufferedImage mockedImage;

    @Before
    public void setUp() {
        mockedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    @Test
    public void testCheckForDuplicatesWhenExists() {
        DmiState dmiState1 = new DmiState();
        dmiState1.setDuplicate(true);
        dmiState1.setMeta(new DmiMetaEntry("1", 0, 0, null, false, false, false, null));

        DmiState dmiState2 = new DmiState();
        dmiState2.setDuplicate(true);
        dmiState2.setMeta(new DmiMetaEntry("2", 0, 0, null, false, false, false, null));

        DmiState dmiState3 = new DmiState();
        dmiState3.setDuplicate(false);
        dmiState3.setMeta(new DmiMetaEntry("3", 0, 0, null, false, false, false, null));

        Dmi dmi = new Dmi();
        dmi.addState(dmiState1);
        dmi.addState(dmiState2);
        dmi.addState(dmiState3);

        assertTrue(dmi.isHasDuplicates());
        assertEquals(new HashSet<>(Arrays.asList("1", "2")), dmi.getDuplicateStatesNames());
    }

    @Test
    public void testCheckForDuplicatesWhenNotExists() {
        Dmi dmi = new Dmi();
        dmi.setStates(new HashMap<>());

        assertFalse(dmi.isHasDuplicates());
        assertTrue(dmi.getDuplicateStatesNames().isEmpty());
    }

    @Test
    public void testIsStateOverflow() {
        Dmi dmi = new Dmi();
        dmi.setStates(new HashMap<>());

        assertFalse(dmi.isStateOverflow());

        Map<String, DmiState> states = new HashMap<>();
        for (int i = 0; i < 513; i++) {
            states.put(String.valueOf(i), new DmiState());
        }
        dmi.setStates(states);

        assertTrue(dmi.isStateOverflow());
    }

    @Test
    public void testGetStateSprite() {
        Dmi dmi = new Dmi();

        DmiState dmiState = new DmiState();
        dmiState.setMeta(new DmiMetaEntry("state", 0, 0, null, false, false, false, null));
        dmiState.addSprite(new DmiSprite(mockedImage, SpriteDir.SOUTH, 0));

        dmi.addState(dmiState);

        assertNotEquals(Optional.empty(), dmi.getStateSprite("state"));
        assertEquals(Optional.empty(), dmi.getStateSprite("1234567890"));
    }

    @Test
    public void testGetStateSpriteWithDir() {
        Dmi dmi = new Dmi();

        DmiState dmiState = new DmiState();
        dmiState.setMeta(new DmiMetaEntry("state", 0, 0, null, false, false, false, null));
        dmiState.addSprite(new DmiSprite(mockedImage, SpriteDir.NORTH, 0));

        dmi.addState(dmiState);

        assertNotEquals(Optional.empty(), dmi.getStateSprite("state", SpriteDir.NORTH));
        assertEquals(Optional.empty(), dmi.getStateSprite("state", SpriteDir.SOUTH));
    }

    @Test
    public void testGetStateSpriteWithDirAndFrame() {
        Dmi dmi = new Dmi();

        DmiState dmiState = new DmiState();
        dmiState.setMeta(new DmiMetaEntry("state", 0, 0, null, false, false, false, null));
        dmiState.addSprite(new DmiSprite(mockedImage, SpriteDir.SOUTHEAST, 0));

        dmi.addState(dmiState);

        assertNotEquals(Optional.empty(), dmi.getStateSprite("state", SpriteDir.SOUTHEAST, 1));
        assertEquals(Optional.empty(), dmi.getStateSprite("state", SpriteDir.SOUTHEAST, 15));
    }

    @Test
    public void testHasState() {
        Dmi dmi = new Dmi();

        DmiState dmiState = new DmiState();
        dmiState.setMeta(new DmiMetaEntry("state", 0, 0, null, false, false, false, null));

        dmi.addState(dmiState);

        assertTrue(dmi.hasState("state"));
        assertFalse(dmi.hasState("1234567890"));
    }
}