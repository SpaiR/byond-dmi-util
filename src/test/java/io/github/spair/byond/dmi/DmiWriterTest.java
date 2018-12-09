package io.github.spair.byond.dmi;

import io.github.spair.byond.dmi.slurper.DmiSlurper;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class DmiWriterTest {

    private DmiSlurper dmiSlurper = new DmiSlurper();

    @Test
    public void testWriteToFile() throws Exception {
        File f = Files.createTempFile("test", "dmi").toFile();
        f.deleteOnExit();

        Dmi expected = dmiSlurper.slurpUp(new File("src/test/resources/test.dmi"));
        expected.save(f);

        Dmi result = dmiSlurper.slurpUp(f);
        result.setName("test.dmi");

        assertEquals(expected, result);

        f.delete();
    }
}
