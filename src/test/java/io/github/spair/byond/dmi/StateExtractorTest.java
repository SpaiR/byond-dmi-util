package io.github.spair.byond.dmi;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class StateExtractorTest {

    @Test
    public void testExtractStates() throws Exception {
        DmiMeta meta = new DmiMeta();

        meta.setSpritesWidth(32);
        meta.setSpritesHeight(32);
        meta.setEntries(
                Arrays.asList(
                        new DmiMeta.DmiMetaEntry("down", 1, 1, null, false, false, false),
                        new DmiMeta.DmiMetaEntry("down (M)", 1, 1, null, false, true, false)
                )
        );

        Map<String, DmiState> states = StateExtractor.extractStates(ImageIO.read(new File("src/test/resources/rollerbed_with_move.dmi")), meta);

        assertEquals(meta.getEntries().get(0), states.get("down").getMetadata());
        assertEquals(meta.getEntries().get(1), states.get("down (M)").getMetadata());

        assertEquals(1, states.get("down").getSprites().size());
        assertEquals(1, states.get("down (M)").getSprites().size());

        assertEquals(SpriteDir.SOUTH, states.get("down").getSprites().get(SpriteDir.SOUTH).get(0).getSpriteDir());
        assertEquals(SpriteDir.SOUTH, states.get("down (M)").getSprites().get(SpriteDir.SOUTH).get(0).getSpriteDir());

        assertEquals(states.get("down").getSprites().get(SpriteDir.SOUTH).get(0), states.get("down (M)").getSprites().get(SpriteDir.SOUTH).get(0));
    }

    @Test
    public void testStateExtractorWhenExists() {
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

        StateExtractor.processDuplicates(dmi);

        assertTrue(dmi.isHasDuplicates());
        assertEquals(new HashSet<>(Arrays.asList("1", "2")), dmi.getDuplicateStatesNames());
    }

    @Test
    public void testStateExtractorWhenNotExists() {
        Dmi dmi = new Dmi();
        dmi.setStates(new HashMap<>());

        StateExtractor.processDuplicates(dmi);

        assertFalse(dmi.isHasDuplicates());
        assertNull(dmi.getDuplicateStatesNames());
    }
}