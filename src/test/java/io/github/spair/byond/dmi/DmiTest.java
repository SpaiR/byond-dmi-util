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

        Map<String, DmiState> states = new HashMap<>();
        states.put("1", dmiState1);
        states.put("2", dmiState2);
        states.put("3", dmiState3);

        Dmi dmi = new Dmi();
        dmi.setStates(states);

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