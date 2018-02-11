package io.github.spair.byond.dmi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;

public final class DmiSlurper {

    public static Dmi slurpUp(File dmiFile) throws DuplicateStateNameException {
        try (InputStream input = new FileInputStream(dmiFile)) {
            return slurpUp(dmiFile.getName(), input);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Provided DMI file doesn't exist");
        }catch (IOException e) {
            throw new IllegalArgumentException("Provided DMI cant be read");
        }
    }

    public static Dmi slurpUp(String dmiName, InputStream input) throws DuplicateStateNameException {
        try (BufferedInputStream bufferedInput = new BufferedInputStream(input)) {
            bufferedInput.mark(input.available());

            BufferedImage dmiImage = ImageIO.read(bufferedInput);
            bufferedInput.reset();

            DmiMeta dmiMeta = MetaExtractor.extractMetadata(bufferedInput);
            Map<String, DmiState> dmiStates = StateExtractor.extractStates(dmiImage, dmiMeta);

            return new Dmi(dmiName, dmiImage.getWidth(), dmiImage.getHeight(), dmiMeta, dmiStates);
        } catch (DuplicateStateNameException e) {
            e.setDmiName(dmiName);
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
