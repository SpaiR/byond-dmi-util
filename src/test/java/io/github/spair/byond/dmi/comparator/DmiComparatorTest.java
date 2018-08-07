package io.github.spair.byond.dmi.comparator;

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

public class DmiComparatorTest {

    private static final String ROLLERBED_ORIGINAL_PATH = "src/test/resources/rollerbed_original.dmi";
    private static final String ROLLERBED_DIFF_PATH = "src/test/resources/rollerbed_original_diff.dmi";

    @Test
    public void testCompareWhenNotSame() {
        Dmi original = DmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        Dmi modified = DmiSlurper.slurpUp(new File(ROLLERBED_DIFF_PATH));

        DmiDiff diff = DmiComparator.compare(original, modified);

        assertFalse(diff.isSame());
        assertNotEquals(diff.getOldMeta(), diff.getNewMeta());
        assertEquals(21, diff.getDmiDiffEntries().size());

        List<DmiDiffEntry> expectedEntries = getExpectedEntries();

        for (int i = 0; i < 21; i++) {
            DmiDiffEntry expectedEntry = expectedEntries.get(i);
            DmiDiffEntry compareToEntry = diff.getDmiDiffEntries().get(i);

            assertEquals(expectedEntry.getStateName(), compareToEntry.getStateName());
            assertEquals(expectedEntry.getStatus(), compareToEntry.getStatus());

            switch (expectedEntry.getStatus()) {
                case CREATED:
                    assertNull(compareToEntry.getOldSprite());
                    assertNotNull(expectedEntry.getNewSprite());
                    assertNotNull(compareToEntry.getNewSprite());

                    assertEquals(expectedEntry.getNewSprite().getDir(), compareToEntry.getNewSprite().getDir());
                    assertEquals(expectedEntry.getNewSprite().getFrameNum(), compareToEntry.getNewSprite().getFrameNum());
                    break;
                case MODIFIED:
                    assertNotNull(expectedEntry.getOldSprite());
                    assertNotNull(compareToEntry.getOldSprite());
                    assertNotNull(expectedEntry.getNewSprite());
                    assertNotNull(compareToEntry.getNewSprite());

                    assertEquals(expectedEntry.getOldSprite().getDir(), compareToEntry.getOldSprite().getDir());
                    assertEquals(expectedEntry.getOldSprite().getFrameNum(), compareToEntry.getOldSprite().getFrameNum());
                    assertEquals(expectedEntry.getNewSprite().getDir(), compareToEntry.getNewSprite().getDir());
                    assertEquals(expectedEntry.getNewSprite().getFrameNum(), compareToEntry.getNewSprite().getFrameNum());
                    break;
                case DELETED:
                    assertNull(compareToEntry.getNewSprite());
                    assertNotNull(expectedEntry.getOldSprite());
                    assertNotNull(compareToEntry.getOldSprite());

                    assertEquals(expectedEntry.getOldSprite().getDir(), compareToEntry.getOldSprite().getDir());
                    assertEquals(expectedEntry.getOldSprite().getFrameNum(), compareToEntry.getOldSprite().getFrameNum());
                    break;
            }
        }
    }

    @Test
    public void testCompareWhenSame() {
        Dmi dmi = DmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        DmiDiff diff = DmiComparator.compare(dmi, dmi);

        assertTrue(diff.isSame());
        assertEquals(diff.getOldMeta(), diff.getNewMeta());
        assertTrue(diff.getDmiDiffEntries().isEmpty());
    }

    @Test
    public void testCompareWhenModifiedNull() {
        Dmi dmi = DmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        DmiDiff diff = DmiComparator.compare(dmi, null);

        assertEquals(17, diff.getDmiDiffEntries().size());
        assertNotNull(diff.getOldMeta());
        assertNull(diff.getNewMeta());

        diff.forEach(dmiDiffEntryEntry -> assertEquals(DiffStatus.DELETED, dmiDiffEntryEntry.getStatus()));
    }

    @Test
    public void testCompareWhenOriginalNull() {
        Dmi dmi = DmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        DmiDiff diff = DmiComparator.compare(null, dmi);

        assertEquals(17, diff.getDmiDiffEntries().size());
        assertNotNull(diff.getNewMeta());
        assertNull(diff.getOldMeta());

        diff.forEach(dmiDiffEntryEntry -> assertEquals(DiffStatus.CREATED, dmiDiffEntryEntry.getStatus()));
    }

    private List<DmiDiffEntry> getExpectedEntries() {
        return Arrays.asList(
                new DmiDiffEntry("up", new DmiSprite(imgPH(), SpriteDir.SOUTH, 1), null),
                new DmiDiffEntry("up", new DmiSprite(imgPH(), SpriteDir.NORTH, 1), null),
                new DmiDiffEntry("up", new DmiSprite(imgPH(), SpriteDir.EAST, 1), null),
                new DmiDiffEntry("up", new DmiSprite(imgPH(), SpriteDir.WEST, 1), null),

                new DmiDiffEntry("down", new DmiSprite(imgPH(), SpriteDir.SOUTH, 1), new DmiSprite(imgPH(), SpriteDir.SOUTH, 1)),

                new DmiDiffEntry("folded", new DmiSprite(imgPH(), SpriteDir.SOUTH, 3), null),
                new DmiDiffEntry("folded", new DmiSprite(imgPH(), SpriteDir.NORTH, 3), null),
                new DmiDiffEntry("folded", new DmiSprite(imgPH(), SpriteDir.EAST, 3), null),
                new DmiDiffEntry("folded", new DmiSprite(imgPH(), SpriteDir.WEST, 3), null),

                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.SOUTH, 1)),
                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.SOUTH, 2)),
                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.SOUTH, 3)),
                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.NORTH, 1)),
                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.NORTH, 2)),
                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.NORTH, 3)),
                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.EAST, 1)),
                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.EAST, 2)),
                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.EAST, 3)),
                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.WEST, 1)),
                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.WEST, 2)),
                new DmiDiffEntry("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.WEST, 3))
        );
    }

    private BufferedImage imgPH() {
        return new BufferedImage(1, 1, Image.SCALE_DEFAULT);
    }
}