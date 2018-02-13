package io.github.spair.byond.dmi;

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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class MetaExtractor {

    private static final String PNG_MIME = "image/png";

    private static final String META_ELEMENT_TAG = "TextEntry";
    private static final String META_ATTRIBUTE = "value";

    private static final Pattern W_H_PATTERN = Pattern.compile("(?:width\\s=\\s(\\d+))\n\t(?:height\\s=\\s(\\d+))");
    private static final Pattern STATE_PATTERN = Pattern.compile("(?:state\\s=\\s\".*\"(?:\\n\\t.*)+)");
    private static final Pattern PARAM_PATTERN = Pattern.compile("(\\w+)\\s=\\s(.+)");

    private static final String STATE = "state";
    private static final String DIRS = "dirs";
    private static final String FRAMES = "frames";
    private static final String DELAY = "delay";
    private static final String LOOP = "loop";
    private static final String MOVEMENT = "movement";
    private static final String REWIND = "rewind";

    private static final String MOVEMENT_SUFFIX = " (M)";

    static DmiMeta extractMetadata(final InputStream input) {
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(input)) {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByMIMEType(PNG_MIME);
            ImageReader reader = readers.next();

            reader.setInput(imageInputStream, true);

            IIOImage image = reader.readAll(0, null);
            IIOMetadata metadata = image.getMetadata();

            String metadataFormatName = IIOMetadataFormatImpl.standardMetadataFormatName;
            IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metadataFormatName);
            IIOMetadataNode text = (IIOMetadataNode) root.getElementsByTagName(META_ELEMENT_TAG).item(0);

            String metadataText = text.getAttribute(META_ATTRIBUTE);

            return parseMetadataText(metadataText);
        } catch (IOException e) {
            throw new IllegalArgumentException("DMI metadata can't be read", e);
        }
    }

    private static DmiMeta parseMetadataText(final String metadataText) {
        DmiMeta metadata = new DmiMeta();
        Matcher widthHeight = W_H_PATTERN.matcher(metadataText);

        if (widthHeight.find() && Objects.nonNull(widthHeight.group(1)) && Objects.nonNull(widthHeight.group(2))) {
            metadata.setSpritesWidth(Integer.parseInt(widthHeight.group(1)));
            metadata.setSpritesHeight(Integer.parseInt(widthHeight.group(2)));
        } else {
            throw new IllegalArgumentException("DMI metadata does't contain width and height properties");
        }

        Matcher state = STATE_PATTERN.matcher(metadataText);

        if (state.find()) {
            state.reset();

            List<DmiMeta.DmiMetaEntry> entries = new ArrayList<>();

            while (state.find()) {
                DmiMeta.DmiMetaEntry entry = parseState(state.group());

                if (entry.isMovement()) {
                    entry.setName(entry.getName().concat(MOVEMENT_SUFFIX));
                }

                entries.add(entry);
            }

            metadata.setEntries(entries);
        } else {
            throw new IllegalArgumentException("DMI metadata does't contain any state property value");
        }

        return metadata;
    }

    private static DmiMeta.DmiMetaEntry parseState(final String stateText) {
        DmiMeta.DmiMetaEntry metaEntry = new DmiMeta.DmiMetaEntry();
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
                    String[] delays = paramValue.split(",");
                    metaEntry.setDelay(Arrays.stream(delays).mapToInt(Integer::parseInt).toArray());
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
                default:
                    throw new IllegalArgumentException("Invalid metadata format detected and can't be read");
            }
        }

        return metaEntry;
    }

    private static boolean isValueTrue(final String value) {
        return "1".equals(value);
    }

    private MetaExtractor() { }
}
