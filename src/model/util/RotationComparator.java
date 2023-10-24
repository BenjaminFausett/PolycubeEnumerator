package model.util;

public class RotationComparator {

    public static boolean trueEquals(boolean[][][] grid1, boolean[][][] grid2) {
        int grid1Volume = grid1.length * grid1[0].length * grid1[0][0].length;
        int grid2Volume = grid2.length * grid2[0].length * grid2[0][0].length;

        if (grid1Volume != grid2Volume) {
            return false;
        }
        if (compareGrids(grid1, grid2)) {
            return true;
        }
        if (rotationallyEquals(grid1, grid2)) {
            return true;
        }

        grid1 = reflectAcrossX(grid1);
        return rotationallyEquals(grid1, grid2);
    }

    private static boolean rotationallyEquals(boolean[][][] grid1, boolean[][][] grid2) {
        for (int x = 0; x < 2; x++) {
            grid1 = rotateX(grid1);

            for (int z = 0; z < 4; z++) {
                grid1 = rotateZ(grid1);
                if (compareGrids(grid1, grid2)) {
                    return true;
                }

                for (int y = 0; y < 4; y++) {
                    grid1 = rotateY(grid1);
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
        int depth = grid.length, height = grid[0].length, width = grid[0][0].length;
        boolean[][][] newGrid = new boolean[depth][width][height];
        for (int x = 0; x < depth; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < width; z++) {
                    newGrid[x][z][height - 1 - y] = grid[x][y][z];
                }
            }
        }

        return newGrid;
    }

    private static boolean[][][] rotateY(boolean[][][] grid) {
        int depth = grid.length, height = grid[0].length, width = grid[0][0].length;
        boolean[][][] newGrid = new boolean[width][height][depth];
        for (int x = 0; x < depth; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < width; z++) {
                    newGrid[z][y][depth - 1 - x] = grid[x][y][z];
                }
            }
        }

        return newGrid;
    }

    private static boolean[][][] rotateZ(boolean[][][] grid) {
        int depth = grid.length, height = grid[0].length, width = grid[0][0].length;
        boolean[][][] newGrid = new boolean[height][depth][width];
        for (int x = 0; x < depth; x++) {
            for (int y = 0; y < height; y++) {
                System.arraycopy(grid[x][y], 0, newGrid[y][depth - 1 - x], 0, width);
            }
        }

        return newGrid;
    }

    private static boolean[][][] reflectAcrossX(final boolean[][][] grid) {
        int depth = grid.length;
        boolean[][][] reflectedGrid = new boolean[depth][][];

        for (int x = 0; x < depth; x++) {
            reflectedGrid[depth - x - 1] = grid[x];
        }

        return reflectedGrid;
    }
}
