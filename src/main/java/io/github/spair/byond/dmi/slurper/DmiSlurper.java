package io.github.spair.byond.dmi.slurper;

import io.github.spair.byond.dmi.Dmi;
import io.github.spair.byond.dmi.DmiMeta;
import io.github.spair.byond.dmi.DmiState;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.util.Base64;
import java.util.Map;

/**
 * Class to extract {@link io.github.spair.byond.dmi.Dmi} object from file, base64 string or raw input stream.
 */
@SuppressWarnings("WeakerAccess")
public final class DmiSlurper {

    /**
     * @param dmiFile file to deserialize
     * @return {@link io.github.spair.byond.dmi.Dmi} object
     */
    public static Dmi slurpUp(final File dmiFile) {
        try (InputStream input = new FileInputStream(dmiFile)) {
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
     * @param base64content base64 string to deserialize
     * @return {@link io.github.spair.byond.dmi.Dmi} object
     */
    public static Dmi slurpUp(final String dmiName, final String base64content) {
        try (InputStream input = new ByteArrayInputStream(Base64.getMimeDecoder().decode(base64content))) {
            return slurpUp(dmiName, input);
        } catch (IOException e) {
            throw new IllegalArgumentException("Received base64 content can't be read. Dmi name: " + dmiName, e);
        }
    }

    /**
     * @param dmiName the name of resulted {@link io.github.spair.byond.dmi.Dmi} object
     * @param input raw input stream to deserialize
     * @return {@link io.github.spair.byond.dmi.Dmi} object
     */
    public static Dmi slurpUp(final String dmiName, final InputStream input) {
        try (BufferedInputStream bufferedInput = new BufferedInputStream(input)) {
            bufferedInput.mark(input.available());

            BufferedImage dmiImage = ImageIO.read(bufferedInput);
            bufferedInput.reset();

            DmiMeta dmiMeta = MetaExtractor.extractMetadata(bufferedInput);
            Map<String, DmiState> dmiStates = StateExtractor.extractStates(dmiImage, dmiMeta);

            return new Dmi(dmiName, dmiImage.getWidth(), dmiImage.getHeight(), dmiMeta, dmiStates);
        } catch (IOException e) {
            throw new IllegalArgumentException("Received DMI can't be read. Dmi name: " + dmiName, e);
        } catch (Exception e) {
            throw new RuntimeException("Unable to slurp up dmi input. Dmi name: " + dmiName, e);
        }
    }

    private DmiSlurper() {
    }
}
