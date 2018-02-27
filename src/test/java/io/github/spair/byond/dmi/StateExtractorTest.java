package io.github.spair.byond.dmi;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StateExtractorTest {

    @Test
    public void testExtractStates() throws Exception {
        DmiMeta meta = new DmiMeta();

        meta.setSpritesWidth(32);
        meta.setSpritesHeight(32);
        meta.setEntries(
                Arrays.asList(
                        new DmiMeta.DmiMetaEntry("down", 1, 1, null, false, false, false, null),
                        new DmiMeta.DmiMetaEntry("down (M)", 1, 1, null, false, true, false, null)
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
}