package model;

/**
 * This class is slow and should only be used for debugging purposes.
 */
public class PolycubeComparator {

    public static boolean trueEquals(final Polycube polycube1, final Polycube polycube2) {

        Cube[][][] grid1 = polycube1.clone().getGrid();
        Cube[][][] reflectedGrid1 = reflectAcrossX(grid1);

        if (compareGrids(grid1, polycube2.getGrid()) || compareGrids(reflectedGrid1, polycube2.getGrid())) {
            return true;
        }

        for (int i = 0; i < 4; i++) {
            Cube[][][] rotatedXGrid1 = rotateX(grid1, i);
            Cube[][][] rotatedXReflectedGrid1 = rotateX(reflectedGrid1, i);

            for (int j = 0; j < 4; j++) {
                Cube[][][] rotatedYGrid1 = rotateY(rotatedXGrid1, j);
                Cube[][][] rotatedYReflectedGrid1 = rotateY(rotatedXReflectedGrid1, j);

                for (int k = 0; k < 4; k++) {
                    Cube[][][] rotatedZGrid1 = rotateZ(rotatedYGrid1, k);
                    Cube[][][] rotatedZReflectedGrid1 = rotateZ(rotatedYReflectedGrid1, k);

                    if (compareGrids(rotatedZGrid1, polycube2.getGrid()) || compareGrids(rotatedZReflectedGrid1, polycube2.getGrid())) {
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

    private static Cube[][][] rotateX(Cube[][][] grid, int times) {
        for (int t = 0; t < times; t++) {
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
        }
        return grid;
    }

    private static Cube[][][] rotateY(Cube[][][] grid, int times) {
        for (int t = 0; t < times; t++) {
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
        }
        return grid;
    }

    private static Cube[][][] rotateZ(Cube[][][] grid, int times) {
        for (int t = 0; t < times; t++) {
            int depth = grid.length, height = grid[0].length, width = grid[0][0].length;
            Cube[][][] newGrid = new Cube[height][depth][width];
            for (int x = 0; x < depth; x++) {
                for (int y = 0; y < height; y++) {
                    for (int z = 0; z < width; z++) {
                        newGrid[y][depth - 1 - x][z] = grid[x][y][z];
                    }
                }
            }
            grid = newGrid;
        }
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
