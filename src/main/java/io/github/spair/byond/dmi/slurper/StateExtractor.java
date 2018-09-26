package io.github.spair.byond.dmi.slurper;

import io.github.spair.byond.dmi.DmiState;
import io.github.spair.byond.dmi.DmiMeta;
import io.github.spair.byond.dmi.DmiMetaEntry;
import io.github.spair.byond.dmi.DmiSprite;
import io.github.spair.byond.dmi.SpriteDir;
import lombok.val;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Comparator;

final class StateExtractor {

    private final Comparator<SpriteDir> dirComparator = Comparator.comparingInt(SpriteDirHelper::getCompareWeight);

    Map<String, DmiState> extractStates(final BufferedImage dmiImage, final DmiMeta dmiMeta) {
        final int dmiWidth = dmiImage.getWidth();
        final int spriteWidth = dmiMeta.getSpritesWidth();
        final int spriteHeight = dmiMeta.getSpritesHeight();

        final int spritesInARow = dmiWidth / spriteWidth;

        int xPos = 0;
        int yPos = 0;
        int spriteIndex = 1;

        Map<String, DmiState> dmiStates = new HashMap<>();

        for (DmiMetaEntry metaEntry : dmiMeta.getMetas()) {
            val stateName = metaEntry.getName();
            val allSprites = new ArrayList<DmiSprite>();

            for (int frameNumber = 1; frameNumber <= metaEntry.getFrames(); frameNumber++) {
                for (int dirCount = 1; dirCount <= metaEntry.getDirs(); dirCount++) {
                    final BufferedImage spriteImage = cropSpriteImage(dmiImage, spriteWidth, spriteHeight, xPos, yPos);

                    allSprites.add(new DmiSprite(spriteImage, SpriteDirHelper.dirByIndex(dirCount), frameNumber));

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

            val dmiState = new DmiState();

            dmiState.setMeta(metaEntry);
            dmiState.setSprites(distributeAllSpritesInMap(allSprites));

            if (dmiStates.containsKey(stateName)) {
                dmiStates.get(stateName).addDuplicate(dmiState);
            } else {
                dmiStates.put(stateName, dmiState);
            }
        }

        return dmiStates;
    }

    private BufferedImage cropSpriteImage(
            final BufferedImage dmiImage, final int width, final int height, final int xPos, final int yPos) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics graphics = dst.getGraphics();
        graphics.drawImage(dmiImage, 0, 0, width, height, xPos, yPos, xPos + width, yPos + height, null);
        graphics.dispose();

        return dst;
    }

    private Map<SpriteDir, List<DmiSprite>> distributeAllSpritesInMap(final List<DmiSprite> allSprites) {
        Map<SpriteDir, List<DmiSprite>> spriteMap = new TreeMap<>(dirComparator);

        for (DmiSprite sprite : allSprites) {
            List<DmiSprite> spritesInDir = spriteMap.computeIfAbsent(sprite.getDir(), k -> new ArrayList<>());
            spritesInDir.add(sprite);
        }

        return spriteMap;
    }
}
