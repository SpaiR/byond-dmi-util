package io.github.spair.byond.dmi.slurper;

import io.github.spair.byond.dmi.Dmi;
import io.github.spair.byond.dmi.DmiState;
import lombok.val;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.util.Map;

/**
 * Class to extract {@link io.github.spair.byond.dmi.Dmi} object from file or raw input stream.
 */
@SuppressWarnings("WeakerAccess")
public final class DmiSlurper {

    private final MetaExtractor metaExtractor = new MetaExtractor();
    private final StateExtractor stateExtractor = new StateExtractor();

    /**
     * @param dmiFile file to deserialize
     * @return {@link io.github.spair.byond.dmi.Dmi} object
     */
    public Dmi slurpUp(final File dmiFile) {
        try (val input = new FileInputStream(dmiFile)) {
            return slurpUp(dmiFile.getName(), input);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Received DMI file doesn't exist. File path: " + dmiFile.getPath(), e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Received DMI can't be read. File path: " + dmiFile.getPath(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unable to slur up dmi file. File path: " + dmiFile.getPath(), e);
        }
    }

    /**
     * @param dmiName the name of resulted {@link io.github.spair.byond.dmi.Dmi} object
     * @param input   raw input stream to deserialize
     * @return {@link io.github.spair.byond.dmi.Dmi} object
     */
    public Dmi slurpUp(final String dmiName, final InputStream input) {
        try (val bufferedInput = new BufferedInputStream(input)) {
            bufferedInput.mark(input.available());

            val dmiImage = ImageIO.read(bufferedInput);
            bufferedInput.reset();

            MetaExtractor.Meta meta = metaExtractor.extractMetadata(bufferedInput);
            Map<String, DmiState> dmiStates = stateExtractor.extractStates(dmiImage, meta);

            return new Dmi(dmiName, dmiImage.getWidth(), dmiImage.getHeight(), meta.getSpritesWidth(), meta.getSpritesHeight(), dmiStates);
        } catch (IOException e) {
            throw new IllegalArgumentException("Received DMI can't be read. Dmi name: " + dmiName, e);
        } catch (Exception e) {
            throw new RuntimeException("Unable to slurp up dmi input. Dmi name: " + dmiName, e);
        }
    }
}
