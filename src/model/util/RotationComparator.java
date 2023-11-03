package model.util;

import java.util.Arrays;

public class RotationComparator {

    public static boolean trueEquals(boolean[][][] grid1, boolean[][][] grid2) {
        if ((grid1.length * grid1[0].length * grid1[0][0].length) !=
                (grid2.length * grid2[0].length * grid2[0][0].length)) {
            return false;
        }
        return rotationallyEquals(grid1, grid2) || rotationallyEquals(reflectAcrossX(grid1), grid2);
    }

    private static boolean rotationallyEquals(boolean[][][] grid1, boolean[][][] grid2) {
        if (Arrays.deepEquals(grid1, grid2)) {
            return true;
        }
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    if (Arrays.deepEquals(grid1, grid2)) {
                        return true;
                    }
                    grid1 = rotateZ(grid1);
                }
                grid1 = rotateY(grid1);
            }
            grid1 = rotateX(grid1);
        }
        return false;
    }

    private static boolean[][][] rotateX(boolean[][][] grid) {
        int xLength = grid.length;
        int yLength = grid[0].length;
        int zLength = grid[0][0].length;

        boolean[][][] newGrid = new boolean[xLength][zLength][yLength];
        for (int x = 0; x < xLength; x++) {
            for (int z = 0; z < zLength; z++) {
                for (int y = 0; y < yLength; y++) {
                    newGrid[x][z][yLength - 1 - y] = grid[x][y][z];
                }
            }
        }
        return newGrid;
    }

    private static boolean[][][] rotateY(boolean[][][] grid) {
        int xLength = grid.length;
        int yLength = grid[0].length;
        int zLength = grid[0][0].length;

        boolean[][][] newGrid = new boolean[zLength][yLength][xLength];
        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yLength; y++) {
                for (int z = 0; z < zLength; z++) {
                    newGrid[z][y][xLength - 1 - x] = grid[x][y][z];
                }
            }
        }
        return newGrid;
    }

    private static boolean[][][] rotateZ(boolean[][][] grid) {
        int xLength = grid.length;
        int yLength = grid[0].length;
        int zLength = grid[0][0].length;

        boolean[][][] newGrid = new boolean[yLength][xLength][zLength];
        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yLength; y++) {
                System.arraycopy(grid[x][y], 0, newGrid[y][xLength - 1 - x], 0, zLength);
            }
        }
        return newGrid;
    }

    private static boolean[][][] reflectAcrossX(final boolean[][][] grid) {
        int xLength = grid.length;

        boolean[][][] reflectedGrid = new boolean[xLength][][];
        for (int x = 0; x < xLength; x++) {
            reflectedGrid[xLength - x - 1] = grid[x].clone();
        }
        return reflectedGrid;
    }
}
