package io.github.spair.byond.dmi.slurper;

import io.github.spair.byond.dmi.DmiState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.spair.byond.dmi.DmiProperties.STATE;
import static io.github.spair.byond.dmi.DmiProperties.DIRS;
import static io.github.spair.byond.dmi.DmiProperties.FRAMES;
import static io.github.spair.byond.dmi.DmiProperties.DELAY;
import static io.github.spair.byond.dmi.DmiProperties.LOOP;
import static io.github.spair.byond.dmi.DmiProperties.MOVEMENT;
import static io.github.spair.byond.dmi.DmiProperties.REWIND;
import static io.github.spair.byond.dmi.DmiProperties.HOTSPOT;

final class MetaExtractor {

    private final Pattern widthHeightPattern = Pattern.compile("(?:width\\s=\\s(\\d+))\n\t(?:height\\s=\\s(\\d+))");
    private final Pattern statePattern = Pattern.compile("(?:state\\s=\\s\".*\"(?:\\n\\t.*)+)");
    private final Pattern paramPattern = Pattern.compile("(\\w+)\\s=\\s(.+)");

    Meta extractMetadata(final InputStream input) {
        IIOMetadata metadata = readMetadata(input);

        String metadataFormatName = IIOMetadataFormatImpl.standardMetadataFormatName;
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metadataFormatName);
        IIOMetadataNode text = (IIOMetadataNode) root.getElementsByTagName("TextEntry").item(0);

        return parseMetadataText(text.getAttribute("value"));
    }

    private IIOMetadata readMetadata(final InputStream input) {
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(input)) {
            ImageReader reader = ImageIO.getImageReadersByMIMEType("image/png").next();

            reader.setInput(imageInputStream, true);
            IIOImage image = reader.readAll(0, null);

            return image.getMetadata();
        } catch (IOException e) {
            throw new IllegalArgumentException("DMI meta can't be read");
        }
    }

    private Meta parseMetadataText(final String metadataText) {
        Matcher widthHeight = widthHeightPattern.matcher(metadataText);

        if (!widthHeight.find() || widthHeight.group(1) == null || widthHeight.group(2) == null) {
            throw new IllegalArgumentException("DMI meta does't contain totalWidth and totalHeight properties");
        }

        Meta metadata = new Meta();
        metadata.spritesWidth = Integer.parseInt(widthHeight.group(1));
        metadata.spritesHeight = Integer.parseInt(widthHeight.group(2));

        Matcher state = statePattern.matcher(metadataText);

        while (state.find()) {
            metadata.metaStates.add(parseState(state.group()));
        }

        if (metadata.metaStates.isEmpty()) {
            throw new IllegalArgumentException("DMI meta does't contain any state property value");
        }

        return metadata;
    }

    private MetaState parseState(final String stateText) {
        Matcher stateParam = paramPattern.matcher(stateText);
        MetaState metaState = new MetaState();

        while (stateParam.find()) {
            final String paramName = stateParam.group(1);
            final String paramValue = stateParam.group(2);

            switch (paramName) {
                case STATE:
                    metaState.name = paramValue.substring(1, paramValue.length() - 1);
                    break;
                case DIRS:
                    metaState.dirs = Integer.parseInt(paramValue);
                    break;
                case FRAMES:
                    metaState.frames = Integer.parseInt(paramValue);
                    break;
                case DELAY:
                    metaState.delay = doubleArrayFromString(paramValue);
                    break;
                case LOOP:
                    metaState.loop = Integer.parseInt(paramValue);
                    break;
                case MOVEMENT:
                    metaState.movement = isValueTrue(paramValue);
                    break;
                case REWIND:
                    metaState.rewind = isValueTrue(paramValue);
                    break;
                case HOTSPOT:
                    metaState.hotspot = doubleArrayFromString(paramValue);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Invalid meta format detected and can't be read. Param name: " + paramName);
            }
        }

        if (metaState.movement) {
            metaState.name += DmiState.MOVEMENT_SUFFIX;
        }

        return metaState;
    }

    private boolean isValueTrue(final String value) {
        return "1".equals(value);
    }

    private double[] doubleArrayFromString(final String str) {
        return Arrays.stream(str.split(",")).mapToDouble(Double::parseDouble).toArray();
    }

    @Data
    static final class Meta {
        private int spritesWidth;
        private int spritesHeight;
        private List<MetaState> metaStates = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static final class MetaState {
        private String name = "";
        private int dirs;
        private int frames;
        private double[] delay;
        private int loop;
        private boolean movement;
        private boolean rewind;
        private double[] hotspot;
    }
}
