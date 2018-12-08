package io.github.spair.byond.dmi.differ;

import io.github.spair.byond.dmi.Dmi;
import io.github.spair.byond.dmi.DmiSprite;
import io.github.spair.byond.dmi.SpriteDir;
import io.github.spair.byond.dmi.slurper.DmiSlurper;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DmiDifferTest {

    private static final String ROLLERBED_ORIGINAL_PATH = "src/test/resources/rollerbed_original.dmi";
    private static final String ROLLERBED_DIFF_PATH = "src/test/resources/rollerbed_original_diff.dmi";

    private final DmiSlurper dmiSlurper = new DmiSlurper();

    @Test
    public void testFindDiffsWhenNotSame() {
        Dmi original = dmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        Dmi modified = dmiSlurper.slurpUp(new File(ROLLERBED_DIFF_PATH));

        List<DmiDiff> diff = DmiDiffer.findDiffs(original, modified);

        assertFalse(diff.isEmpty());
        assertEquals(21, diff.size());

        List<DmiDiff> expectedEntries = getExpectedEntries();

        for (int i = 0; i < 21; i++) {
            DmiDiff expectedEntry = expectedEntries.get(i);
            DmiDiff compareToEntry = diff.get(i);

            assertEquals(expectedEntry.getStateName(), compareToEntry.getStateName());
            assertEquals(expectedEntry.getStatus(), compareToEntry.getStatus());

            switch (expectedEntry.getStatus()) {
                case CREATED:
                    assertFalse(compareToEntry.getOldSprite().isPresent());
                    assertTrue(expectedEntry.getNewSprite().isPresent());
                    assertTrue(compareToEntry.getNewSprite().isPresent());

                    assertEquals(expectedEntry.getNewSprite().get().getDir(), compareToEntry.getNewSprite().get().getDir());
                    assertEquals(expectedEntry.getNewSprite().get().getFrameNumber(), compareToEntry.getNewSprite().get().getFrameNumber());
                    break;
                case MODIFIED:
                    assertTrue(expectedEntry.getOldSprite().isPresent());
                    assertTrue(compareToEntry.getOldSprite().isPresent());
                    assertTrue(expectedEntry.getNewSprite().isPresent());
                    assertTrue(compareToEntry.getNewSprite().isPresent());

                    assertEquals(expectedEntry.getOldSprite().get().getDir(), compareToEntry.getOldSprite().get().getDir());
                    assertEquals(expectedEntry.getOldSprite().get().getFrameNumber(), compareToEntry.getOldSprite().get().getFrameNumber());
                    assertEquals(expectedEntry.getNewSprite().get().getDir(), compareToEntry.getNewSprite().get().getDir());
                    assertEquals(expectedEntry.getNewSprite().get().getFrameNumber(), compareToEntry.getNewSprite().get().getFrameNumber());
                    break;
                case DELETED:
                    assertFalse(compareToEntry.getNewSprite().isPresent());
                    assertTrue(expectedEntry.getOldSprite().isPresent());
                    assertTrue(compareToEntry.getOldSprite().isPresent());

                    assertEquals(expectedEntry.getOldSprite().get().getDir(), compareToEntry.getOldSprite().get().getDir());
                    assertEquals(expectedEntry.getOldSprite().get().getFrameNumber(), compareToEntry.getOldSprite().get().getFrameNumber());
                    break;
            }
        }
    }

    @Test
    public void testFindDiffsWhenSame() {
        Dmi dmi = dmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        List<DmiDiff> diff = DmiDiffer.findDiffs(dmi, dmi);
        assertTrue(diff.isEmpty());
    }

    @Test
    public void testFindDiffsWhenModifiedNull() {
        Dmi dmi = dmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        List<DmiDiff> diff = DmiDiffer.findDiffs(dmi, null);

        assertEquals(17, diff.size());

        diff.forEach(dmiDiffEntryEntry -> assertEquals(DmiDiffStatus.DELETED, dmiDiffEntryEntry.getStatus()));
    }

    @Test
    public void testFindDiffsWhenOriginalNull() {
        Dmi dmi = dmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        List<DmiDiff> diff = DmiDiffer.findDiffs(null, dmi);

        assertEquals(17, diff.size());

        diff.forEach(dmiDiffEntryEntry -> assertEquals(DmiDiffStatus.CREATED, dmiDiffEntryEntry.getStatus()));
    }

    private List<DmiDiff> getExpectedEntries() {
        return Arrays.asList(
                new DmiDiff("up", new DmiSprite(imgPH(), SpriteDir.SOUTH, 1), null),
                new DmiDiff("up", new DmiSprite(imgPH(), SpriteDir.NORTH, 1), null),
                new DmiDiff("up", new DmiSprite(imgPH(), SpriteDir.EAST, 1), null),
                new DmiDiff("up", new DmiSprite(imgPH(), SpriteDir.WEST, 1), null),

                new DmiDiff("down", new DmiSprite(imgPH(), SpriteDir.SOUTH, 1), new DmiSprite(imgPH(), SpriteDir.SOUTH, 1)),

                new DmiDiff("folded", new DmiSprite(imgPH(), SpriteDir.SOUTH, 3), null),
                new DmiDiff("folded", new DmiSprite(imgPH(), SpriteDir.NORTH, 3), null),
                new DmiDiff("folded", new DmiSprite(imgPH(), SpriteDir.EAST, 3), null),
                new DmiDiff("folded", new DmiSprite(imgPH(), SpriteDir.WEST, 3), null),

                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.SOUTH, 1)),
                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.SOUTH, 2)),
                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.SOUTH, 3)),
                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.NORTH, 1)),
                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.NORTH, 2)),
                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.NORTH, 3)),
                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.EAST, 1)),
                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.EAST, 2)),
                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.EAST, 3)),
                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.WEST, 1)),
                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.WEST, 2)),
                new DmiDiff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.WEST, 3))
        );
    }

    private BufferedImage imgPH() {
        return new BufferedImage(1, 1, Image.SCALE_DEFAULT);
    }
}