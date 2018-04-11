package io.github.spair.byond.dmi;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.*;

public class DmiTest {

    @Test
    public void testCheckForDuplicatesWhenExists() {
        DmiState dmiState1 = new DmiState();
        dmiState1.setDuplicate(true);
        dmiState1.setMeta(new Meta("1", 0, 0, null, false, false, false, null));

        DmiState dmiState2 = new DmiState();
        dmiState2.setDuplicate(true);
        dmiState2.setMeta(new Meta("2", 0, 0, null, false, false, false, null));

        DmiState dmiState3 = new DmiState();
        dmiState3.setDuplicate(false);
        dmiState3.setMeta(new Meta("3", 0, 0, null, false, false, false, null));

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
}