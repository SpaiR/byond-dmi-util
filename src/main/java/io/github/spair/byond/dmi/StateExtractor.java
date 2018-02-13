package io.github.spair.byond.dmi;

import java.awt.image.BufferedImage;
import java.util.*;

final class StateExtractor {

    static Map<String, DmiState> extractStates(BufferedImage dmiImage, DmiMeta dmiMeta) {
        final int dmiWidth = dmiImage.getWidth();
        final int spriteWidth = dmiMeta.getSpritesWidth();
        final int spriteHeight = dmiMeta.getSpritesHeight();

        final int spritesInARow = dmiWidth / spriteWidth;

        int xPos = 0;
        int yPos = 0;
        int spriteIndex = 1;

        Map<String, DmiState> dmiStates = new HashMap<>();

        for (DmiMeta.DmiMetaEntry metaEntry : dmiMeta.getEntries()) {
            final boolean stateNameDuplicate = dmiStates.containsKey(metaEntry.getName());
            List<DmiSprite> allSprites = new ArrayList<>();

            for (int frameNumber = 1; frameNumber <= metaEntry.getFrames(); frameNumber++) {
                for (int dirCount = 1; dirCount <= metaEntry.getDirs(); dirCount++) {
                    DmiSprite sprite = new DmiSprite();
                    BufferedImage spriteImage = cropSpriteImage(dmiImage, spriteWidth, spriteHeight, xPos, yPos);

                    sprite.setSprite(spriteImage);
                    sprite.setSpriteFrameNum(frameNumber);
                    sprite.setSpriteDir(SpriteDir.valueOf(dirCount));

                    allSprites.add(sprite);

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

            dmiStates.put(metaEntry.getName(), new DmiState(metaEntry, distributeAllSpritesInMap(allSprites), stateNameDuplicate));
        }

        return dmiStates;
    }

    static void processDuplicates(Dmi dmi) {
        Set<String> duplicateStatesNames = new HashSet<>();

        dmi.getStates().forEach((stateName, dmiState) -> {
            if (dmiState.isDuplicate()) {
                duplicateStatesNames.add(stateName);
            }
        });

        if (duplicateStatesNames.size() > 0) {
            dmi.setDuplicateStatesNames(duplicateStatesNames);
            dmi.setHasDuplicates(true);
        }
    }

    private static BufferedImage cropSpriteImage(BufferedImage dmiImage, int width, int height, int xPos, int yPos) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        dst.getGraphics().drawImage(dmiImage, 0, 0, width, height, xPos, yPos, xPos + width, yPos + height, null);
        return dst;
    }

    private static Map<SpriteDir, List<DmiSprite>> distributeAllSpritesInMap(List<DmiSprite> allSprites) {
        Map<SpriteDir, List<DmiSprite>> spriteMap = new TreeMap<>(Comparator.comparingInt(dir -> dir.compareWeight));

        for (DmiSprite sprite : allSprites) {
            List<DmiSprite> spritesInDir = spriteMap.getOrDefault(sprite.getSpriteDir(), new ArrayList<>());
            spritesInDir.add(sprite);
            spriteMap.putIfAbsent(sprite.getSpriteDir(), spritesInDir);
        }

        return spriteMap;
    }
}
