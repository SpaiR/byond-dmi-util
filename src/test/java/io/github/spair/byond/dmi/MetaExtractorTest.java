package io.github.spair.byond.dmi;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MetaExtractorTest {

    @Test
    public void testExtractMetadata() throws Exception {
        DmiMeta expectedMeta = new DmiMeta();

        expectedMeta.setSpritesWidth(32);
        expectedMeta.setSpritesHeight(32);
        expectedMeta.setMetas(
                Arrays.asList(
                        new Meta("down", 1, 1, null, false, false, false, null),
                        new Meta("down (M)", 1, 1, null, false, true, false, null)
                )
        );

        DmiMeta metaToCompare = MetaExtractor.extractMetadata(new FileInputStream(new File("src/test/resources/rollerbed_with_move.dmi")));

        assertEquals(expectedMeta, metaToCompare);
    }
}