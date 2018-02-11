package io.github.spair.byond.dmi;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class MetaExtractorTest {

    @Test
    public void testExtractMetadata() throws Exception {
        DmiMeta expectedMeta = new DmiMeta();

        expectedMeta.setSpritesWidth(32);
        expectedMeta.setSpritesHeight(32);
        expectedMeta.setEntries(
                Arrays.asList(
                        new DmiMeta.DmiMetaEntry("down", 1, 1, null, false, false, false),
                        new DmiMeta.DmiMetaEntry("down (M)", 1, 1, null, false, true, false)
                )
        );

        DmiMeta metaToCompare = MetaExtractor.extractMetadata(new FileInputStream(new File("src/test/resources/rollerbed_with_move.dmi")));

        assertEquals(expectedMeta, metaToCompare);
    }
}