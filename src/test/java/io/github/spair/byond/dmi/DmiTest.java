package io.github.spair.byond.dmi;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.*;

public class DmiTest {

    @Test
    public void testProcessDuplicatesWhenExists() {
        DmiState dmiState1 = new DmiState();
        dmiState1.setDuplicate(true);
        dmiState1.setMetadata(new DmiMeta.DmiMetaEntry("1", 0, 0, null, false, false, false));

        DmiState dmiState2 = new DmiState();
        dmiState2.setDuplicate(true);
        dmiState2.setMetadata(new DmiMeta.DmiMetaEntry("2", 0, 0, null, false, false, false));

        DmiState dmiState3 = new DmiState();
        dmiState3.setDuplicate(false);
        dmiState3.setMetadata(new DmiMeta.DmiMetaEntry("3", 0, 0, null, false, false, false));

        Map<String, DmiState> states = new HashMap<>();
        states.put("1", dmiState1);
        states.put("2", dmiState2);
        states.put("3", dmiState3);

        Dmi dmi = new Dmi();
        dmi.setStates(states);

        dmi.processDuplicates();

        assertTrue(dmi.isHasDuplicates());
        assertEquals(new HashSet<>(Arrays.asList("1", "2")), dmi.getDuplicateStatesNames());
    }

    @Test
    public void testProcessDuplicatesWhenNotExists() {
        Dmi dmi = new Dmi();
        dmi.setStates(new HashMap<>());

        dmi.processDuplicates();

        assertFalse(dmi.isHasDuplicates());
        assertTrue(dmi.getDuplicateStatesNames().isEmpty());
    }
}