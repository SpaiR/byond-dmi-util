package io.github.spair.byond.dmi.slurper;

import io.github.spair.byond.dmi.DmiMeta;
import io.github.spair.byond.dmi.DmiMetaEntry;
import io.github.spair.byond.dmi.DmiState;
import io.github.spair.byond.dmi.SpriteDir;
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
        meta.setMetas(
                Arrays.asList(
                        new DmiMetaEntry("down", 1, 1, null, false, false, false, null),
                        new DmiMetaEntry("down (M)", 1, 1, null, false, true, false, null)
                )
        );

        Map<String, DmiState> states = StateExtractor.extractStates(ImageIO.read(new File("src/test/resources/rollerbed_with_move.dmi")), meta);

        assertEquals(meta.getMetas().get(0), states.get("down").getMeta());
        assertEquals(meta.getMetas().get(1), states.get("down (M)").getMeta());

        assertEquals(1, states.get("down").getSprites().size());
        assertEquals(1, states.get("down (M)").getSprites().size());

        assertEquals(SpriteDir.SOUTH, states.get("down").getSprite(SpriteDir.SOUTH).get().getDir());
        assertEquals(SpriteDir.SOUTH, states.get("down (M)").getSprite(SpriteDir.SOUTH).get().getDir());

        assertEquals(states.get("down").getSprite(SpriteDir.SOUTH), states.get("down (M)").getSprite(SpriteDir.SOUTH));
    }
}