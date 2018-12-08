package io.github.spair.byond.dmi;

import java.util.Comparator;

/**
 * Comparator to place all dirs in BYOND-natural order.
 */
public class SpriteDirComparator implements Comparator<SpriteDir> {

    @Override
    public int compare(final SpriteDir o1, final SpriteDir o2) {
        return getCompareWeight(o1) - getCompareWeight(o2);
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private int getCompareWeight(final SpriteDir spriteDir) {
        switch (spriteDir) {
            case SOUTH:
                return 1;
            case NORTH:
                return 2;
            case EAST:
                return 3;
            case WEST:
                return 4;
            case SOUTHEAST:
                return 5;
            case SOUTHWEST:
                return 6;
            case NORTHEAST:
                return 7;
            case NORTHWEST:
                return 8;
            default:
                return 1;
        }
    }
}
