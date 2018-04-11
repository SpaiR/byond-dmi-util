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
        meta.setMetas(
                Arrays.asList(
                        new Meta("down", 1, 1, null, false, false, false, null),
                        new Meta("down (M)", 1, 1, null, false, true, false, null)
                )
        );

        Map<String, DmiState> states = new StateExtractor().extractStates(ImageIO.read(new File("src/test/resources/rollerbed_with_move.dmi")), meta);

        assertEquals(meta.getMetas().get(0), states.get("down").getMeta());
        assertEquals(meta.getMetas().get(1), states.get("down (M)").getMeta());

        assertEquals(1, states.get("down").getSprites().size());
        assertEquals(1, states.get("down (M)").getSprites().size());

        assertEquals(SpriteDir.SOUTH, states.get("down").getSprite(SpriteDir.SOUTH).getDir());
        assertEquals(SpriteDir.SOUTH, states.get("down (M)").getSprite(SpriteDir.SOUTH).getDir());

        assertEquals(states.get("down").getSprite(SpriteDir.SOUTH), states.get("down (M)").getSprite(SpriteDir.SOUTH));
    }
}