package io.github.spair.byond.dmi.slurper;

import io.github.spair.byond.dmi.DmiSprite;
import io.github.spair.byond.dmi.DmiState;
import io.github.spair.byond.dmi.SpriteDir;
import lombok.val;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("checkstyle:MagicNumber")
final class StateExtractor {

    Map<String, DmiState> extractStates(final BufferedImage dmiImage, final MetaExtractor.Meta meta) {
        final int dmiWidth = dmiImage.getWidth();
        final int spriteWidth = meta.getSpritesWidth();
        final int spriteHeight = meta.getSpritesHeight();

        final int spritesInARow = dmiWidth / spriteWidth;

        int xPos = 0;
        int yPos = 0;
        int spriteIndex = 1;

        Map<String, DmiState> dmiStates = new LinkedHashMap<>();

        for (MetaExtractor.MetaState metaState : meta.getMetaStates()) {
            val allSprites = new ArrayList<DmiSprite>();

            for (int frameNumber = 1; frameNumber <= metaState.getFrames(); frameNumber++) {
                for (int dirCount = 1; dirCount <= metaState.getDirs(); dirCount++) {
                    final BufferedImage spriteImage = cropSpriteImage(dmiImage, spriteWidth, spriteHeight, xPos, yPos);

                    allSprites.add(new DmiSprite(spriteImage, dirByIndex(dirCount), frameNumber));

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
            val stateName = metaState.getName();

            dmiState.setName(stateName);
            dmiState.setDelay(metaState.getDelay());
            dmiState.setDirs(metaState.getDirs());
            dmiState.setFrames(metaState.getFrames());
            dmiState.setHotspot(metaState.getHotspot());
            dmiState.setLoop(metaState.getLoop());
            dmiState.setMovement(metaState.isMovement());
            dmiState.setRewind(metaState.isRewind());

            for (DmiSprite sprite : allSprites) {
                dmiState.addSprite(sprite);
            }

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

    // During DMI slurping all dirs images parsed one by one.
    // This method determines the order in which dirs are placed in `.dmi` file.
    private SpriteDir dirByIndex(final int dirCount) {
        switch (dirCount) {
            case 1:
                return SpriteDir.SOUTH;
            case 2:
                return SpriteDir.NORTH;
            case 3:
                return SpriteDir.EAST;
            case 4:
                return SpriteDir.WEST;
            case 5:
                return SpriteDir.SOUTHEAST;
            case 6:
                return SpriteDir.SOUTHWEST;
            case 7:
                return SpriteDir.NORTHEAST;
            case 8:
                return SpriteDir.NORTHWEST;
            default:
                return SpriteDir.SOUTH;
        }
    }
}
