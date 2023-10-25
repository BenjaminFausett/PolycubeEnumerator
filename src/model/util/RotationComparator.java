package model.util;

/**
 * This class is slow. The entire program is designed around trying to avoid this class ever being used
 *
 * @author ChatGPT
 */
public class RotationComparator {

    public static boolean trueEquals(boolean[][][] grid1, boolean[][][] grid2) {
        if ((grid1.length * grid1[0].length * grid1[0][0].length) != (grid2.length * grid2[0].length * grid2[0][0].length)) {
            return false;
        }
        if (compareGrids(grid1, grid2)) {
            return true;
        }
        if (rotationallyEquals(grid1, grid2)) {
            return true;
        }
        return rotationallyEquals(reflectAcrossX(grid1), grid2);
    }

    private static boolean rotationallyEquals(boolean[][][] grid1, boolean[][][] grid2) {
        for (int x = 0; x < 2; x++) {
            grid1 = rotateX(grid1);

            for (int y = 0; y < 4; y++) {
                grid1 = rotateY(grid1);
                if (compareGrids(grid1, grid2)) {
                    return true;
                }

                for (int z = 0; z < 4; z++) {
                    grid1 = rotateZ(grid1);
                    if (compareGrids(grid1, grid2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean compareGrids(boolean[][][] grid1, boolean[][][] grid2) {
        if (grid1.length != grid2.length || grid1[0].length != grid2[0].length || grid1[0][0].length != grid2[0][0].length) {
            return false;
        }

        for (int x = 0; x < grid1.length; x++) {
            for (int y = 0; y < grid1[0].length; y++) {
                for (int z = 0; z < grid1[0][0].length; z++) {
                    if (grid1[x][y][z] != grid2[x][y][z]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean[][][] rotateX(boolean[][][] grid) {
        boolean[][][] newGrid = new boolean[grid.length][grid[0][0].length][grid[0].length];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    newGrid[x][z][grid[0].length - 1 - y] = grid[x][y][z];
                }
            }
        }
        return newGrid;
    }

    private static boolean[][][] rotateY(boolean[][][] grid) {
        boolean[][][] newGrid = new boolean[grid[0][0].length][grid[0].length][grid.length];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    newGrid[z][y][grid.length - 1 - x] = grid[x][y][z];
                }
            }
        }
        return newGrid;
    }

    private static boolean[][][] rotateZ(boolean[][][] grid) {
        boolean[][][] newGrid = new boolean[grid[0].length][grid.length][grid[0][0].length];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                System.arraycopy(grid[x][y], 0, newGrid[y][grid.length - 1 - x], 0, grid[0][0].length);
            }
        }
        return newGrid;
    }

    private static boolean[][][] reflectAcrossX(final boolean[][][] grid) {
        boolean[][][] reflectedGrid = new boolean[grid.length][][];
        for (int x = 0; x < grid.length; x++) {
            reflectedGrid[grid.length - x - 1] = grid[x];
        }
        return reflectedGrid;
    }
}
