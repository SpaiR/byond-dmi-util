package io.github.spair.byond.dmi;

import org.junit.Test;

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
        assertNotEquals(diff.getOriginalMeta(), diff.getModifiedMeta());
        assertEquals(21, diff.getDiffEntries().size());

        List<DmiDiff.DiffEntry> expectedEntries = getExpectedEntries();

        for (int i = 0; i < 21; i++) {
            DmiDiff.DiffEntry expectedEntry = expectedEntries.get(i);
            DmiDiff.DiffEntry compareToEntry = diff.getDiffEntries().get(i);

            assertEquals(expectedEntry.getStateName(), compareToEntry.getStateName());
            assertEquals(expectedEntry.getStatus(), compareToEntry.getStatus());

            switch (expectedEntry.getStatus()) {
                case CREATED:
                    assertNull(compareToEntry.getOriginalSprite());
                    assertEquals(expectedEntry.getModifiedSprite().getSpriteDir(), compareToEntry.getModifiedSprite().getSpriteDir());
                    assertEquals(expectedEntry.getModifiedSprite().getSpriteFrameNum(), compareToEntry.getModifiedSprite().getSpriteFrameNum());
                    break;
                case MODIFIED:
                    assertEquals(expectedEntry.getOriginalSprite().getSpriteDir(), compareToEntry.getOriginalSprite().getSpriteDir());
                    assertEquals(expectedEntry.getOriginalSprite().getSpriteFrameNum(), compareToEntry.getOriginalSprite().getSpriteFrameNum());
                    assertEquals(expectedEntry.getModifiedSprite().getSpriteDir(), compareToEntry.getModifiedSprite().getSpriteDir());
                    assertEquals(expectedEntry.getModifiedSprite().getSpriteFrameNum(), compareToEntry.getModifiedSprite().getSpriteFrameNum());
                    break;
                case DELETED:
                    assertNull(compareToEntry.getModifiedSprite());
                    assertEquals(expectedEntry.getOriginalSprite().getSpriteDir(), compareToEntry.getOriginalSprite().getSpriteDir());
                    assertEquals(expectedEntry.getOriginalSprite().getSpriteFrameNum(), compareToEntry.getOriginalSprite().getSpriteFrameNum());
                    break;
            }
        }
    }

    @Test
    public void testCompareWhenSame() {
        Dmi dmi = DmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        DmiDiff diff = DmiComparator.compare(dmi, dmi);

        assertTrue(diff.isSame());
        assertEquals(diff.getOriginalMeta(), diff.getModifiedMeta());
        assertTrue(diff.getDiffEntries().isEmpty());
    }

    @Test
    public void testCompareWhenModifiedNull() {
        Dmi dmi = DmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        DmiDiff diff = DmiComparator.compare(dmi, null);

        assertEquals(17, diff.getDiffEntries().size());
        assertNotNull(diff.getOriginalMeta());
        assertNull(diff.getModifiedMeta());

        diff.getDiffEntries().forEach(diffEntry -> assertEquals(DmiDiff.Status.DELETED, diffEntry.getStatus()));
    }

    @Test
    public void testCompareWhenOriginalNull() {
        Dmi dmi = DmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        DmiDiff diff = DmiComparator.compare(null, dmi);

        assertEquals(17, diff.getDiffEntries().size());
        assertNotNull(diff.getModifiedMeta());
        assertNull(diff.getOriginalMeta());

        diff.getDiffEntries().forEach(diffEntry -> assertEquals(DmiDiff.Status.CREATED, diffEntry.getStatus()));
    }

    private List<DmiDiff.DiffEntry> getExpectedEntries() {
        return Arrays.asList(
                new DmiDiff.DiffEntry("up", new DmiSprite(null, SpriteDir.SOUTH, 1), null),
                new DmiDiff.DiffEntry("up", new DmiSprite(null, SpriteDir.NORTH, 1), null),
                new DmiDiff.DiffEntry("up", new DmiSprite(null, SpriteDir.EAST, 1), null),
                new DmiDiff.DiffEntry("up", new DmiSprite(null, SpriteDir.WEST, 1), null),

                new DmiDiff.DiffEntry("down", new DmiSprite(null, SpriteDir.SOUTH, 1), new DmiSprite(null, SpriteDir.SOUTH, 1)),

                new DmiDiff.DiffEntry("folded", new DmiSprite(null, SpriteDir.SOUTH, 3), null),
                new DmiDiff.DiffEntry("folded", new DmiSprite(null, SpriteDir.NORTH, 3), null),
                new DmiDiff.DiffEntry("folded", new DmiSprite(null, SpriteDir.EAST, 3), null),
                new DmiDiff.DiffEntry("folded", new DmiSprite(null, SpriteDir.WEST, 3), null),

                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.SOUTH, 1)),
                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.SOUTH, 2)),
                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.SOUTH, 3)),
                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.NORTH, 1)),
                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.NORTH, 2)),
                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.NORTH, 3)),
                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.EAST, 1)),
                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.EAST, 2)),
                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.EAST, 3)),
                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.WEST, 1)),
                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.WEST, 2)),
                new DmiDiff.DiffEntry("folded_new_state", null, new DmiSprite(null, SpriteDir.WEST, 3))
        );
    }
}