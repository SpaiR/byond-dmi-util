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

    /**
     * <p>BYOND representation of dir as int value.
     * <ul>
     *     <li>SOUTH - 2</li>
     *     <li>NORTH - 1</li>
     *     <li>EAST - 4</li>
     *     <li>WEST - 8</li>
     *     <li>SOUTHEAST - 6</li>
     *     <li>SOUTHWEST - 10</li>
     *     <li>NORTHEAST - 5</li>
     *     <li>NORTHWEST - 9</li>
     * </ul>
     */
    public final int dirValue;

    /**
     * <p>Shorted string representation of constant values.
     * <ul>
     *     <li>SOUTH - S</li>
     *     <li>NORTH - N</li>
     *     <li>EAST - E</li>
     *     <li>WEST - W</li>
     *     <li>SOUTHEAST - SE</li>
     *     <li>SOUTHWEST - SW</li>
     *     <li>NORTHEAST - NE</li>
     *     <li>NORTHWEST - NW</li>
     * </ul>
     */
    public final String shortName;

    // Used to sort dirs in BYOND like order.
    final int compareWeight;

    SpriteDir(final int dirValue, final String shortName, final int compareWeight) {
        this.dirValue = dirValue;
        this.shortName = shortName;
        this.compareWeight = compareWeight;
    }

    /**
     * Returns {@link SpriteDir} equivalent to BYOND dir value. If method got value different from the list below,
     * {@link IllegalArgumentException} will be thrown.
     * <ul>
     *     <li>0,1,3,7 - NORTH</li>
     *     <li>2 - SOUTH</li>
     *     <li>4 - EAST</li>
     *     <li>8 - WEST</li>
     *     <li>5 - NORTHEAST</li>
     *     <li>6 - SOUTHEAST</li>
     *     <li>9 - NORTHWEST</li>
     *     <li>10 - SOUTHWEST</li>
     * </ul>
     *
     * @param dirValue BYOND representation of dir
     * @return {@link SpriteDir} equivalent of BYOND dir value
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public static SpriteDir valueOfByondDir(final int dirValue) {
        switch (dirValue) {
            case 0: case 1: case 3: case 7:
                return NORTH;
            case 2:
                return SOUTH;
            case 4:
                return EAST;
            case 8:
                return WEST;
            case 5:
                return NORTHEAST;
            case 6:
                return SOUTHEAST;
            case 9:
                return NORTHWEST;
            case 10:
                return SOUTHWEST;
            default:
                throw new IllegalArgumentException("Illegal value of BYOND dir. Dir value: " + dirValue);
        }
    }

    /**
     * Same as {@link #valueOfByondDir(int)}, but accept value as string instead of int.
     *
     * @param dirValue BYOND representation of dir
     * @return {@link SpriteDir} equivalent of BYOND dir value
     */
    public static SpriteDir valueOfByondDir(final String dirValue) {
        return valueOfByondDir(Integer.parseInt(dirValue));
    }

    // During DMI slurping all dirs are passed in `for(i = 0; i <= n; i++)` cycle.
    // This method determines the order in which dirs are placed in `.dmi` file.
    @SuppressWarnings("checkstyle:MagicNumber")
    static SpriteDir valueOf(final int dirCount) {
        switch (dirCount) {
            case 1:
                return SOUTH;
            case 2:
                return NORTH;
            case 3:
                return EAST;
            case 4:
                return WEST;
            case 5:
                return SOUTHEAST;
            case 6:
                return SOUTHWEST;
            case 7:
                return NORTHEAST;
            case 8:
                return NORTHWEST;
            default:
                return SOUTH;
        }
    }
}
