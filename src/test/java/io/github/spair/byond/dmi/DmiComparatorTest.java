package io.github.spair.byond.dmi;

import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DmiComparatorTest {

    private final DmiSlurper dmiSlurper = DmiSlurper.getInstance();
    private final DmiComparator dmiComparator = DmiComparator.getInstance();

    private static final String ROLLERBED_ORIGINAL_PATH = "src/test/resources/rollerbed_original.dmi";
    private static final String ROLLERBED_DIFF_PATH = "src/test/resources/rollerbed_original_diff.dmi";

    @Test
    public void testCompareWhenNotSame() {
        Dmi original = dmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        Dmi modified = dmiSlurper.slurpUp(new File(ROLLERBED_DIFF_PATH));

        DmiDiff diff = dmiComparator.compare(original, modified);

        assertFalse(diff.isSame());
        assertNotEquals(diff.getOldMeta(), diff.getNewMeta());
        assertEquals(21, diff.getDiffs().size());

        List<Diff> expectedEntries = getExpectedEntries();

        for (int i = 0; i < 21; i++) {
            Diff expectedEntry = expectedEntries.get(i);
            Diff compareToEntry = diff.getDiffs().get(i);

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
        Dmi dmi = dmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        DmiDiff diff = dmiComparator.compare(dmi, dmi);

        assertTrue(diff.isSame());
        assertEquals(diff.getOldMeta(), diff.getNewMeta());
        assertTrue(diff.getDiffs().isEmpty());
    }

    @Test
    public void testCompareWhenModifiedNull() {
        Dmi dmi = dmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        DmiDiff diff = dmiComparator.compare(dmi, null);

        assertEquals(17, diff.getDiffs().size());
        assertNotNull(diff.getOldMeta());
        assertNull(diff.getNewMeta());

        diff.getDiffs().forEach(diffEntry -> assertEquals(DiffStatus.DELETED, diffEntry.getStatus()));
    }

    @Test
    public void testCompareWhenOriginalNull() {
        Dmi dmi = dmiSlurper.slurpUp(new File(ROLLERBED_ORIGINAL_PATH));
        DmiDiff diff = dmiComparator.compare(null, dmi);

        assertEquals(17, diff.getDiffs().size());
        assertNotNull(diff.getNewMeta());
        assertNull(diff.getOldMeta());

        diff.getDiffs().forEach(diffEntry -> assertEquals(DiffStatus.CREATED, diffEntry.getStatus()));
    }

    private List<Diff> getExpectedEntries() {
        return Arrays.asList(
                new Diff("up", new DmiSprite(imgPH(), SpriteDir.SOUTH, 1), null),
                new Diff("up", new DmiSprite(imgPH(), SpriteDir.NORTH, 1), null),
                new Diff("up", new DmiSprite(imgPH(), SpriteDir.EAST, 1), null),
                new Diff("up", new DmiSprite(imgPH(), SpriteDir.WEST, 1), null),

                new Diff("down", new DmiSprite(imgPH(), SpriteDir.SOUTH, 1), new DmiSprite(imgPH(), SpriteDir.SOUTH, 1)),

                new Diff("folded", new DmiSprite(imgPH(), SpriteDir.SOUTH, 3), null),
                new Diff("folded", new DmiSprite(imgPH(), SpriteDir.NORTH, 3), null),
                new Diff("folded", new DmiSprite(imgPH(), SpriteDir.EAST, 3), null),
                new Diff("folded", new DmiSprite(imgPH(), SpriteDir.WEST, 3), null),

                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.SOUTH, 1)),
                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.SOUTH, 2)),
                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.SOUTH, 3)),
                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.NORTH, 1)),
                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.NORTH, 2)),
                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.NORTH, 3)),
                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.EAST, 1)),
                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.EAST, 2)),
                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.EAST, 3)),
                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.WEST, 1)),
                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.WEST, 2)),
                new Diff("folded_new_state", null, new DmiSprite(imgPH(), SpriteDir.WEST, 3))
        );
    }

    private BufferedImage imgPH() {
        return new BufferedImage(1, 1, Image.SCALE_DEFAULT);
    }
}