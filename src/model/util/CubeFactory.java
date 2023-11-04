package model.util;

import model.records.Cube;


/**
 * The purpose of this class is to create, store, and fetch all needed cube objects without ever creating duplicate coordinates. This allows for a lower memory footprint.
 */
public class CubeFactory {

    private final static byte MAX_RANGE = 36;
    private final static byte OFFSET = 18;
    private final static Cube[][][] cubeCache = new Cube[MAX_RANGE][MAX_RANGE][MAX_RANGE];

    public static Cube get(int x, int y, int z) {
        int indexX = x + OFFSET;
        int indexY = y + OFFSET;
        int indexZ = z + OFFSET;

        if (cubeCache[indexX][indexY][indexZ] == null) {
            cubeCache[indexX][indexY][indexZ] = new Cube((byte) x, (byte) y, (byte) z);
        }
        return cubeCache[indexX][indexY][indexZ];
    }

}