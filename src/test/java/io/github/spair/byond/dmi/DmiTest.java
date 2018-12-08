package io.github.spair.byond.dmi;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;

public class DmiTest {

    private BufferedImage mockedImage;
    private DmiState dmiState;

    @Before
    public void setUp() {
        mockedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        dmiState = new DmiState();
        dmiState.setName("state");
    }

    @Test
    public void testCheckForDuplicatesWhenExists() {
        DmiState dmiState1 = new DmiState();
        dmiState1.addDuplicate(new DmiState());
        dmiState1.setName("1");

        DmiState dmiState2 = new DmiState();
        dmiState2.addDuplicate(new DmiState());
        dmiState2.setName("2");

        DmiState dmiState3 = new DmiState();
        dmiState3.setName("3");

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
        assertFalse(dmi.isHasDuplicates());
        assertTrue(dmi.getDuplicateStatesNames().isEmpty());
    }

    @Test
    public void testIsStateOverflow() {
        Dmi dmi = new Dmi();

        assertFalse(dmi.isStateOverflow());

        for (int i = 0; i < 513; i++) {
            DmiState state = new DmiState();
            state.setName(String.valueOf(i));
            dmi.addState(state);
        }

        assertTrue(dmi.isStateOverflow());
    }

    @Test
    public void testGetStateSprite() {
        Dmi dmi = new Dmi();
        dmiState.addSprite(new DmiSprite(mockedImage, SpriteDir.SOUTH, 0));

        dmi.addState(dmiState);

        assertNotEquals(Optional.empty(), dmi.getStateSprite("state"));
        assertEquals(Optional.empty(), dmi.getStateSprite("1234567890"));
    }

    @Test
    public void testGetStateSpriteWithDir() {
        Dmi dmi = new Dmi();
        dmiState.addSprite(new DmiSprite(mockedImage, SpriteDir.NORTH, 0));

        dmi.addState(dmiState);

        assertNotEquals(Optional.empty(), dmi.getStateSprite("state", SpriteDir.NORTH));
        assertEquals(Optional.empty(), dmi.getStateSprite("state", SpriteDir.SOUTH));
    }

    @Test
    public void testGetStateSpriteWithDirAndFrame() {
        Dmi dmi = new Dmi();
        dmiState.addSprite(new DmiSprite(mockedImage, SpriteDir.SOUTHEAST, 0));

        dmi.addState(dmiState);

        assertNotEquals(Optional.empty(), dmi.getStateSprite("state", SpriteDir.SOUTHEAST, 1));
        assertEquals(Optional.empty(), dmi.getStateSprite("state", SpriteDir.SOUTHEAST, 15));
    }

    @Test
    public void testHasState() {
        Dmi dmi = new Dmi();
        dmi.addState(dmiState);

        assertTrue(dmi.hasState("state"));
        assertFalse(dmi.hasState("1234567890"));
    }
}