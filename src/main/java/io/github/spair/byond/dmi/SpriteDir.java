package io.github.spair.byond.dmi;

public enum SpriteDir {

    NORTH(1, "N"),
    SOUTH(2, "S"),
    EAST(4, "E"),
    WEST(8, "W"),

    NORTHEAST(5, "NE"),
    NORTHWEST(9, "NW"),
    SOUTHEAST(6, "SE"),
    SOUTHWEST(10, "SW");

    /**
     * <p>BYOND representation of dir as int value.
     * <ul>
     * <li>NORTH - 1</li>
     * <li>SOUTH - 2</li>
     * <li>EAST - 4</li>
     * <li>WEST - 8</li>
     * <li>NORTHEAST - 5</li>
     * <li>NORTHWEST - 9</li>
     * <li>SOUTHEAST - 6</li>
     * <li>SOUTHWEST - 10</li>
     * </ul>
     */
    public final int dirValue;

    /**
     * <p>Shorted string representation of constant values.
     * <ul>
     * <li>NORTH - N</li>
     * <li>SOUTH - S</li>
     * <li>EAST - E</li>
     * <li>WEST - W</li>
     * <li>SOUTHEAST - SE</li>
     * <li>SOUTHWEST - SW</li>
     * <li>NORTHEAST - NE</li>
     * <li>NORTHWEST - NW</li>
     * </ul>
     */
    public final String shortName;

    SpriteDir(final int dirValue, final String shortName) {
        this.dirValue = dirValue;
        this.shortName = shortName;
    }

    /**
     * Returns {@link SpriteDir} equivalent to BYOND dir value. If method got value lesser then 0 or bigger then 10,
     * {@link IllegalArgumentException} will be thrown.
     * <ul>
     * <li>1 - NORTH</li>
     * <li>2 - SOUTH</li>
     * <li>4 - EAST</li>
     * <li>8 - WEST</li>
     * <li>5 - NORTHEAST</li>
     * <li>6 - SOUTHEAST</li>
     * <li>9 - NORTHWEST</li>
     * <li>10 - SOUTHWEST</li>
     * </ul>
     *
     * @param dirValue BYOND representation of dir
     * @return {@link SpriteDir} equivalent of BYOND dir value
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public static SpriteDir valueOfByondDir(final int dirValue) {
        if (dirValue < 0 || dirValue > 10) {
            throw new IllegalArgumentException("Illegal value of BYOND dir. Dir value: " + dirValue);
        }

        // Values below sorted in order in which dirs appears in `dmi` file.
        switch (dirValue) {
            case 2:
                return SOUTH;
            case 1:
                return NORTH;
            case 4:
                return EAST;
            case 8:
                return WEST;
            case 5:
                return NORTHEAST;
            case 9:
                return NORTHWEST;
            case 6:
                return SOUTHEAST;
            case 10:
                return SOUTHWEST;
            default:
                return SOUTH;
        }
    }
}
