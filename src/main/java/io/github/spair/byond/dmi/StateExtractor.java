package io.github.spair.byond.dmi;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Comparator;

final class StateExtractor {

    static Map<String, DmiState> extractStates(final BufferedImage dmiImage, final DmiMeta dmiMeta) {
        final int dmiWidth = dmiImage.getWidth();
        final int spriteWidth = dmiMeta.getSpritesWidth();
        final int spriteHeight = dmiMeta.getSpritesHeight();

        final int spritesInARow = dmiWidth / spriteWidth;

        int xPos = 0;
        int yPos = 0;
        int spriteIndex = 1;

        Map<String, DmiState> dmiStates = new HashMap<>();

        for (DmiMeta.DmiMetaEntry metaEntry : dmiMeta.getEntries()) {
            List<DmiSprite> allSprites = new ArrayList<>();

            for (int frameNumber = 1; frameNumber <= metaEntry.getFrames(); frameNumber++) {
                for (int dirCount = 1; dirCount <= metaEntry.getDirs(); dirCount++) {
                    final BufferedImage spriteImage = cropSpriteImage(dmiImage, spriteWidth, spriteHeight, xPos, yPos);

                    allSprites.add(new DmiSprite(spriteImage, SpriteDir.valueOf(dirCount), frameNumber));

                    if (spriteIndex < spritesInARow) {
                        spriteIndex++;
                        xPos += spriteWidth;
                    } else {
                        spriteIndex = 1;
                        xPos = 0;
                        yPos += spriteHeight;
                    }
                }
            }

            DmiState dmiState = new DmiState();

            dmiState.setMetadata(metaEntry);
            dmiState.setSprites(distributeAllSpritesInMap(allSprites));
            dmiState.setDuplicate(dmiStates.containsKey(metaEntry.getName()));

            dmiStates.put(metaEntry.getName(), dmiState);
        }

        return dmiStates;
    }

    private static BufferedImage cropSpriteImage(
            final BufferedImage dmiImage, final int width, final int height, final int xPos, final int yPos) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics graphics = dst.getGraphics();
        graphics.drawImage(dmiImage, 0, 0, width, height, xPos, yPos, xPos + width, yPos + height, null);
        graphics.dispose();

        return dst;
    }

    private static Map<SpriteDir, List<DmiSprite>> distributeAllSpritesInMap(final List<DmiSprite> allSprites) {
        Map<SpriteDir, List<DmiSprite>> spriteMap = new TreeMap<>(Comparator.comparingInt(dir -> dir.compareWeight));

        for (DmiSprite sprite : allSprites) {
            List<DmiSprite> spritesInDir = spriteMap.getOrDefault(sprite.getSpriteDir(), new ArrayList<>());
            spritesInDir.add(sprite);
            spriteMap.putIfAbsent(sprite.getSpriteDir(), spritesInDir);
        }

        return spriteMap;
    }

    private StateExtractor() {
    }
}
