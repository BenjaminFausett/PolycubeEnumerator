package model;

/**
 * This class is slow and should only be used for debugging purposes.
 */
public class PolycubeComparator {

    public static boolean trueEquals(final Polycube2 polycube1, final Polycube2 polycube2) {

        Cube[][][] grid1 = polycube1.clone().getGrid();
        Cube[][][] reflectedGrid1 = reflectAcrossX(polycube1.clone().getGrid());

        // Check the original and reflected orientation first
        if (compareGrids(grid1, polycube2.getGrid()) || compareGrids(reflectedGrid1, polycube2.getGrid())) {
            return true;
        }

        for (int x = 0; x < 2; x++) { // Rotate about X-axis (original and one more rotation)
            grid1 = rotateX(grid1);
            reflectedGrid1 = rotateX(reflectedGrid1);

            for (int z = 0; z < 4; z++) { // Rotate about Z-axis
                grid1 = rotateZ(grid1);
                reflectedGrid1 = rotateZ(reflectedGrid1);

                if (compareGrids(grid1, polycube2.getGrid()) || compareGrids(reflectedGrid1, polycube2.getGrid())) {
                    return true;
                }

                for (int y = 0; y < 4; y++) { // Rotate about Y-axis
                    grid1 = rotateY(grid1);
                    reflectedGrid1 = rotateY(reflectedGrid1);

                    if (compareGrids(grid1, polycube2.getGrid()) || compareGrids(reflectedGrid1, polycube2.getGrid())) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private static boolean compareGrids(Cube[][][] grid1, Cube[][][] grid2) {
        if (grid1.length != grid2.length || grid1[0].length != grid2[0].length || grid1[0][0].length != grid2[0][0].length) {
            return false;
        }

        for (int x = 0; x < grid1.length; x++) {
            for (int y = 0; y < grid1[0].length; y++) {
                for (int z = 0; z < grid1[0][0].length; z++) {
                    boolean cell1Exists = grid1[x][y][z] != null;
                    boolean cell2Exists = grid2[x][y][z] != null;

                    if (cell1Exists != cell2Exists) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static Cube[][][] rotateX(Cube[][][] grid) {
        int depth = grid.length, height = grid[0].length, width = grid[0][0].length;
        Cube[][][] newGrid = new Cube[depth][width][height];
        for (int x = 0; x < depth; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < width; z++) {
                    newGrid[x][z][height - 1 - y] = grid[x][y][z];
                }
            }
        }
        grid = newGrid;

        return grid;
    }

    private static Cube[][][] rotateY(Cube[][][] grid) {
        int depth = grid.length, height = grid[0].length, width = grid[0][0].length;
        Cube[][][] newGrid = new Cube[width][height][depth];
        for (int x = 0; x < depth; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < width; z++) {
                    newGrid[z][y][depth - 1 - x] = grid[x][y][z];
                }
            }
        }
        grid = newGrid;

        return grid;
    }

    private static Cube[][][] rotateZ(Cube[][][] grid) {
        int depth = grid.length, height = grid[0].length, width = grid[0][0].length;
        Cube[][][] newGrid = new Cube[height][depth][width];
        for (int x = 0; x < depth; x++) {
            for (int y = 0; y < height; y++) {
                System.arraycopy(grid[x][y], 0, newGrid[y][depth - 1 - x], 0, width);
            }
        }
        grid = newGrid;

        return grid;
    }

    private static Cube[][][] reflectAcrossX(final Cube[][][] grid) {
        int depth = grid.length;
        Cube[][][] reflectedGrid = new Cube[depth][][];

        for (int x = 0; x < depth; x++) {
            reflectedGrid[depth - x - 1] = grid[x];
        }

        return reflectedGrid;
    }
}
