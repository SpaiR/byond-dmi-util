package io.github.spair.byond.dmi;

public enum SpriteDir {
    SOUTH(2, "S", 1),
    NORTH(1, "N", 2),
    EAST(4, "E", 3),
    WEST(8, "W", 4),

    SOUTHEAST(6, "SE", 5),
    SOUTHWEST(10, "SW", 6),
    NORTHEAST(5, "NE", 7),
    NORTHWEST(9, "NW", 8);

    public final int dirValue;
    public final String shortName;

    final int compareWeight;

    private static final int SOUTH_DIR_COUNT = 1;
    private static final int NORTH_DIR_COUNT = 2;
    private static final int EAST_DIR_COUNT = 3;
    private static final int WEST_DIR_COUNT = 4;
    private static final int SOUTHEAST_DIR_COUNT = 5;
    private static final int SOUTHWEST_DIR_COUNT = 6;
    private static final int NORTHEAST_DIR_COUNT = 7;
    private static final int NORTHWEST_DIR_COUNT = 8;

    SpriteDir(final int dirValue, final String shortName, final int compareWeight) {
        this.dirValue = dirValue;
        this.shortName = shortName;
        this.compareWeight = compareWeight;
    }

    static SpriteDir valueOf(final int dirCount) {
        switch (dirCount) {
            case SOUTH_DIR_COUNT:
                return SOUTH;
            case NORTH_DIR_COUNT:
                return NORTH;
            case EAST_DIR_COUNT:
                return EAST;
            case WEST_DIR_COUNT:
                return WEST;
            case SOUTHEAST_DIR_COUNT:
                return SOUTHEAST;
            case SOUTHWEST_DIR_COUNT:
                return SOUTHWEST;
            case NORTHEAST_DIR_COUNT:
                return NORTHEAST;
            case NORTHWEST_DIR_COUNT:
                return NORTHWEST;
            default:
                return SOUTH;
        }
    }
}
