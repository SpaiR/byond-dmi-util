package io.github.spair.byond.dmi;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DmiComparatorTest {

    @Test
    public void testCompareWhenNotSame() throws Exception {
        Dmi original = DmiSlurper.slurpUp(new File("src/test/resources/rollerbed_original.dmi"));
        Dmi modified = DmiSlurper.slurpUp(new File("src/test/resources/rollerbed_original_diff.dmi"));

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
    public void testCompareWhenSame() throws Exception {
        Dmi dmi = DmiSlurper.slurpUp(new File("src/test/resources/rollerbed_original.dmi"));

        DmiDiff diff = DmiComparator.compare(dmi, dmi);

        assertTrue(diff.isSame());
        assertEquals(diff.getOriginalMeta(), diff.getModifiedMeta());
        assertEquals(0, diff.getDiffEntries().size());
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