package io.github.spair.byond.dmi.slurper;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MetaExtractorTest {

    @Test
    public void testExtractMetadata() throws Exception {
        MetaExtractor.Meta expectedMeta = new MetaExtractor.Meta();

        expectedMeta.setSpritesWidth(32);
        expectedMeta.setSpritesHeight(32);
        expectedMeta.setMetaStates(
                Arrays.asList(
                        new MetaExtractor.MetaState("down", 1, 1, null, 0, false, false, null),
                        new MetaExtractor.MetaState("down (M)", 1, 1, null, 0, true, false, null)
                )
        );

        MetaExtractor.Meta metaToCompare = new MetaExtractor().extractMetadata(new FileInputStream(new File("src/test/resources/rollerbed_with_move.dmi")));

        assertEquals(expectedMeta, metaToCompare);
    }
}