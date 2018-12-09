package io.github.spair.byond.dmi;

import lombok.val;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.IIOImage;
import javax.imageio.metadata.IIOMetadataNode;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static io.github.spair.byond.dmi.DmiProperties.STATE;
import static io.github.spair.byond.dmi.DmiProperties.DIRS;
import static io.github.spair.byond.dmi.DmiProperties.FRAMES;
import static io.github.spair.byond.dmi.DmiProperties.DELAY;
import static io.github.spair.byond.dmi.DmiProperties.LOOP;
import static io.github.spair.byond.dmi.DmiProperties.MOVEMENT;
import static io.github.spair.byond.dmi.DmiProperties.REWIND;
import static io.github.spair.byond.dmi.DmiProperties.HOTSPOT;

final class DmiWriter {

    private static final String NATIVE_METADATA_FORMAT_NAME = "javax_imageio_png_1.0";

    private DmiWriter() {
    }

    static void writeToFile(final File file, final Dmi dmi) throws IOException {
        createFileIfNotExists(file);

        val metadataNode = collectMetadataNodeForImage(dmi);
        val finalImage = getImageWithPlacedSprites(dmi);

        val imageWriter = ImageIO.getImageWritersByFormatName("PNG").next();
        val typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB);

        val metadata = imageWriter.getDefaultImageMetadata(typeSpecifier, null);
        metadata.mergeTree(NATIVE_METADATA_FORMAT_NAME, metadataNode);

        val stream = ImageIO.createImageOutputStream(file);

        imageWriter.setOutput(stream);
        imageWriter.write(metadata, new IIOImage(finalImage, null, metadata), null);

        stream.flush();
        stream.close();

        imageWriter.dispose();
    }

    private static IIOMetadataNode collectMetadataNodeForImage(final Dmi dmi) {
        val textEntry = new IIOMetadataNode("zTXtEntry");
        textEntry.setAttribute("compressionMethod", "deflate");
        textEntry.setAttribute("keyword", "Description");
        textEntry.setAttribute("text", getMetadataText(dmi));

        val textNode = new IIOMetadataNode("zTXt");
        textNode.appendChild(textEntry);

        val root = new IIOMetadataNode(NATIVE_METADATA_FORMAT_NAME);
        root.appendChild(textNode);

        return root;
    }

    private static String getMetadataText(final Dmi dmi) {
        StringBuilder sb = new StringBuilder("# BEGIN DMI\n");
        sb.append("version = 4.0\n");
        sb.append("\twidth = ").append(dmi.getSpriteWidth()).append('\n');
        sb.append("\theight = ").append(dmi.getSpriteHeight()).append('\n');

        for (val stateEntry : dmi) {
            val dmiState = stateEntry.getValue();
            appendStateMetadata(sb, dmiState);
            if (dmiState.hasDuplicates()) {
                for (DmiState duplicatedState : dmiState.getDuplicates()) {
                    appendStateMetadata(sb, duplicatedState);
                }
            }
        }

        sb.append("# END DMI\n");
        return sb.toString();
    }

    private static void appendStateMetadata(final StringBuilder sb, final DmiState dmiState) {
        sb.append(STATE).append(" = \"").append(dmiState.getRawName()).append("\"\n");
        appendStatePropertyMetadata(sb, DIRS, dmiState.getDirs());
        appendStatePropertyMetadata(sb, FRAMES, dmiState.getFrames());
        if (dmiState.getDelay() != null)
            appendStatePropertyMetadata(sb, DELAY, joinArray(dmiState.getDelay()));
        if (dmiState.getLoop() > 0)
            appendStatePropertyMetadata(sb, LOOP, dmiState.getLoop());
        if (dmiState.isMovement())
            appendStatePropertyMetadata(sb, MOVEMENT, "1");
        if (dmiState.isRewind())
            appendStatePropertyMetadata(sb, REWIND, "1");
        if (dmiState.getHotspot() != null)
            appendStatePropertyMetadata(sb, HOTSPOT, joinArray(dmiState.getHotspot()));
    }

    private static void appendStatePropertyMetadata(final StringBuilder sb, final String propName, final Object propValue) {
        sb.append("\t").append(propName).append(" = ").append(propValue).append('\n');
    }

    private static String joinArray(final double[] arr) {
        StringJoiner sj = new StringJoiner(",");
        for (double v : arr) {
            sj.add(String.valueOf(v).replace(".0", ""));
        }
        return sj.toString();
    }

    private static BufferedImage getImageWithPlacedSprites(final Dmi dmi) {
        val spriteList = getAllSprites(dmi);

        int totalImagesCount = spriteList.size();
        int maxColumns = (int) Math.ceil(Math.sqrt(totalImagesCount));
        int maxRows = totalImagesCount / maxColumns;

        if (maxColumns * maxRows < totalImagesCount) {
            maxRows++;
        }

        int imageWidth = dmi.getSpriteWidth() * maxColumns;
        int imageHeight = dmi.getSpriteHeight() * maxRows;

        val image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        int currentRow = 0;
        int currentColumn = 0;

        Graphics g = image.getGraphics();
        for (val sprite : spriteList) {
            g.drawImage(sprite, currentColumn * dmi.getSpriteWidth(), currentRow * dmi.getSpriteHeight(), null);
            if (++currentColumn == maxColumns) {
                currentColumn = 0;
                currentRow++;
            }
        }
        g.dispose();

        return image;
    }

    private static List<BufferedImage> getAllSprites(final Dmi dmi) {
        List<BufferedImage> allSprites = new ArrayList<>();

        for (val stateEntry : dmi) {
            val dmiState = stateEntry.getValue();
            addStateSpritesToList(allSprites, dmiState);
            if (dmiState.hasDuplicates()) {
                for (DmiState state : dmiState.getDuplicates()) {
                    addStateSpritesToList(allSprites, state);
                }
            }
        }

        return allSprites;
    }

    private static void addStateSpritesToList(final List<BufferedImage> spritesList, final DmiState dmiState) {
        for (int frame = 1; frame <= dmiState.getFrames(); frame++) {
            for (val spriteDir : dmiState.getSprites().keySet()) {
                spritesList.add(dmiState.getSprite(spriteDir, frame).orElseThrow(IllegalStateException::new).getSprite());
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void createFileIfNotExists(final File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
