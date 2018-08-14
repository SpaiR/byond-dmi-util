package io.github.spair.byond.dmi.slurper;

import io.github.spair.byond.dmi.DmiMeta;
import io.github.spair.byond.dmi.DmiMetaEntry;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class MetaExtractor {

    private static final String PNG_MIME = "image/png";

    private static final String META_ELEMENT_TAG = "TextEntry";
    private static final String META_ATTRIBUTE = "value";

    private static final Pattern WIDTH_HEIGHT = Pattern.compile("(?:width\\s=\\s(\\d+))\n\t(?:height\\s=\\s(\\d+))");
    private static final Pattern STATE_PATTERN = Pattern.compile("(?:state\\s=\\s\".*\"(?:\\n\\t.*)+)");
    private static final Pattern PARAM_PATTERN = Pattern.compile("(\\w+)\\s=\\s(.+)");

    private static final String STATE = "state";
    private static final String DIRS = "dirs";
    private static final String FRAMES = "frames";
    private static final String DELAY = "delay";
    private static final String LOOP = "loop";
    private static final String MOVEMENT = "movement";
    private static final String REWIND = "rewind";
    private static final String HOTSPOT = "hotspot";

    private static final String MOVEMENT_SUFFIX = " (M)";

    static DmiMeta extractMetadata(final InputStream input) {
        IIOMetadata metadata = readMetadata(input);

        String metadataFormatName = IIOMetadataFormatImpl.standardMetadataFormatName;
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metadataFormatName);
        IIOMetadataNode text = (IIOMetadataNode) root.getElementsByTagName(META_ELEMENT_TAG).item(0);

        return parseMetadataText(text.getAttribute(META_ATTRIBUTE));
    }

    private static IIOMetadata readMetadata(final InputStream input) {
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(input)) {
            ImageReader reader = ImageIO.getImageReadersByMIMEType(PNG_MIME).next();

            reader.setInput(imageInputStream, true);
            IIOImage image = reader.readAll(0, null);

            return image.getMetadata();
        } catch (IOException e) {
            throw new IllegalArgumentException("DMI meta can't be read");
        }
    }

    private static DmiMeta parseMetadataText(final String metadataText) {
        DmiMeta metadata = new DmiMeta();
        Matcher widthHeight = WIDTH_HEIGHT.matcher(metadataText);

        if (!widthHeight.find() || widthHeight.group(1) == null || widthHeight.group(2) == null) {
            throw new IllegalArgumentException("DMI meta does't contain width and height properties");
        }

        metadata.setSpritesWidth(Integer.parseInt(widthHeight.group(1)));
        metadata.setSpritesHeight(Integer.parseInt(widthHeight.group(2)));

        Matcher state = STATE_PATTERN.matcher(metadataText);
        List<DmiMetaEntry> entries = new ArrayList<>();

        while (state.find()) {
            DmiMetaEntry entry = parseState(state.group());
            if (entry.isMovement()) {
                entry.setName(entry.getName().concat(MOVEMENT_SUFFIX));
            }
            entries.add(entry);
        }

        if (entries.isEmpty()) {
            throw new IllegalArgumentException("DMI meta does't contain any state property value");
        }

        metadata.setMetas(entries);

        return metadata;
    }

    private static DmiMetaEntry parseState(final String stateText) {
        DmiMetaEntry metaEntry = new DmiMetaEntry();
        Matcher stateParam = PARAM_PATTERN.matcher(stateText);

        while (stateParam.find()) {
            final String paramName = stateParam.group(1);
            final String paramValue = stateParam.group(2);

            switch (paramName) {
                case STATE:
                    String stateNameWithoutQuotes = paramValue.substring(1, paramValue.length() - 1);
                    metaEntry.setName(stateNameWithoutQuotes);
                    break;
                case DIRS:
                    metaEntry.setDirs(Integer.parseInt(paramValue));
                    break;
                case FRAMES:
                    metaEntry.setFrames(Integer.parseInt(paramValue));
                    break;
                case DELAY:
                    metaEntry.setDelay(doubleArrayFromString(paramValue));
                    break;
                case LOOP:
                    metaEntry.setLoop(isValueTrue(paramValue));
                    break;
                case MOVEMENT:
                    metaEntry.setMovement(isValueTrue(paramValue));
                    break;
                case REWIND:
                    metaEntry.setRewind(isValueTrue(paramValue));
                    break;
                case HOTSPOT:
                    metaEntry.setHotspot(doubleArrayFromString(paramValue));
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Invalid meta format detected and can't be read. Param name: " + paramName);
            }
        }

        return metaEntry;
    }

    private static boolean isValueTrue(final String value) {
        return "1".equals(value);
    }

    private static double[] doubleArrayFromString(final String str) {
        return Arrays.stream(str.split(",")).mapToDouble(Double::parseDouble).toArray();
    }

    private MetaExtractor() {
    }
}
