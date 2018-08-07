package io.github.spair.byond.dmi.slurper;

import io.github.spair.byond.dmi.SpriteDir;

@SuppressWarnings("checkstyle:MagicNumber")
final class SpriteDirHelper {

    // During DMI slurping all dirs are passed in `for(i = 0; i <= n; i++)` cycle.
    // This method determines the order in which dirs are placed in `.dmi` file.
    static SpriteDir dirByIndex(final int dirCount) {
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

    static int getCompareWeight(final SpriteDir spriteDir) {
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

    private SpriteDirHelper() {
    }
}
