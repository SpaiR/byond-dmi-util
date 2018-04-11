package io.github.spair.byond.dmi;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpriteDirTest {

    @Test
    public void testValueOfByondDir() {
        assertEquals(SpriteDir.NORTH, SpriteDir.valueOfByondDir(1));
        assertEquals(SpriteDir.SOUTH, SpriteDir.valueOfByondDir(2));
        assertEquals(SpriteDir.EAST, SpriteDir.valueOfByondDir(4));
        assertEquals(SpriteDir.WEST, SpriteDir.valueOfByondDir(8));

        assertEquals(SpriteDir.SOUTHEAST, SpriteDir.valueOfByondDir(6));
        assertEquals(SpriteDir.SOUTHWEST, SpriteDir.valueOfByondDir(10));
        assertEquals(SpriteDir.NORTHEAST, SpriteDir.valueOfByondDir(5));
        assertEquals(SpriteDir.NORTHWEST, SpriteDir.valueOfByondDir(9));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByondDirWithIllegalValue() {
        //noinspection ResultOfMethodCallIgnored
        SpriteDir.valueOfByondDir(0);
    }

    @Test
    public void testValueOfByondDirWithString() {
        assertEquals(SpriteDir.NORTH, SpriteDir.valueOfByondDir("1"));
        assertEquals(SpriteDir.SOUTHWEST, SpriteDir.valueOfByondDir("10"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByondDirWithIllegalString() {
        SpriteDir.valueOfByondDir("85");
    }
}