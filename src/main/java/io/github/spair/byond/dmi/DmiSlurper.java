package io.github.spair.byond.dmi;

import javax.annotation.Nonnull;
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
public final class DmiSlurper {

    private final MetaExtractor metaExtractor = new MetaExtractor();
    private final StateExtractor stateExtractor = new StateExtractor();

    /**
     * Static method to process file. Same as {@link #slurpUp(File)},
     * with {@link DmiSlurper} instantiation under the hood.
     *
     * @param dmiFile file to deserialize
     * @return {@link io.github.spair.byond.dmi.Dmi} object
     */
    @Nonnull
    public static Dmi slurpUpFile(final File dmiFile) {
        return new DmiSlurper().slurpUp(dmiFile);
    }

    /**
     * Static method to process file. Same as {@link #slurpUp(String, String)},
     * with {@link DmiSlurper} instantiation under the hood.
     *
     * @param dmiName the name of resulted {@link io.github.spair.byond.dmi.Dmi} object
     * @param base64content base64 string to deserialize
     * @return {@link io.github.spair.byond.dmi.Dmi} object
     */
    @Nonnull
    public static Dmi slurpUpBase64(final String dmiName, final String base64content) {
        return new DmiSlurper().slurpUp(dmiName, base64content);
    }

    /**
     * Static method to process file. Same as {@link #slurpUp(String, InputStream)},
     * with {@link DmiSlurper} instantiation under the hood.
     *
     * @param dmiName the name of resulted {@link io.github.spair.byond.dmi.Dmi} object
     * @param input raw input stream to deserialize
     * @return {@link io.github.spair.byond.dmi.Dmi} object
     */
    @Nonnull
    public static Dmi slurpUpStream(final String dmiName, final InputStream input) {
        return new DmiSlurper().slurpUp(dmiName, input);
    }

    /**
     * @param dmiFile file to deserialize
     * @return {@link io.github.spair.byond.dmi.Dmi} object
     */
    @Nonnull
    public Dmi slurpUp(final File dmiFile) {
        try (InputStream input = new FileInputStream(dmiFile)) {
            return slurpUp(dmiFile.getName(), input);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Provided DMI file doesn't exist");
        } catch (IOException e) {
            throw new IllegalArgumentException("Provided DMI can't be read");
        }
    }

    /**
     * @param dmiName the name of resulted {@link io.github.spair.byond.dmi.Dmi} object
     * @param base64content base64 string to deserialize
     * @return {@link io.github.spair.byond.dmi.Dmi} object
     */
    @Nonnull
    public Dmi slurpUp(final String dmiName, final String base64content) {
        try (InputStream input = new ByteArrayInputStream(Base64.getMimeDecoder().decode(base64content))) {
            return slurpUp(dmiName, input);
        } catch (IOException e) {
            throw new IllegalArgumentException("Provided base64 content can't be read");
        }
    }

    /**
     * @param dmiName the name of resulted {@link io.github.spair.byond.dmi.Dmi} object
     * @param input raw input stream to deserialize
     * @return {@link io.github.spair.byond.dmi.Dmi} object
     */
    @Nonnull
    public Dmi slurpUp(final String dmiName, final InputStream input) {
        try (BufferedInputStream bufferedInput = new BufferedInputStream(input)) {
            bufferedInput.mark(input.available());

            BufferedImage dmiImage = ImageIO.read(bufferedInput);
            bufferedInput.reset();

            DmiMeta dmiMeta = metaExtractor.extractMetadata(bufferedInput);
            Map<String, DmiState> dmiStates = stateExtractor.extractStates(dmiImage, dmiMeta);

            return new Dmi(dmiName, dmiImage.getWidth(), dmiImage.getHeight(), dmiMeta, dmiStates);
        } catch (IOException e) {
            throw new IllegalArgumentException("Provided DMI can't be read");
        }
    }
}
