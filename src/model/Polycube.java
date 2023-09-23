package model;

import java.util.ArrayList;
import java.util.List;

public class Polycube implements Comparable<Polycube> {

    private boolean[][][] grid;
    private long polycubeValue;

    //Creates the perfect and only Mono Cube
    public Polycube() {
        this.grid = new boolean[3][3][3];
        this.addCube(new Coordinate(1, 1, 1));
        this.polycubeValue = -1;
    }

    public Polycube(Polycube polycube, Coordinate newCubeCoordinate) {
        int size = polycube.getGrid().length;

        // Create a new grid with dimensions incremented by 1
        this.grid = new boolean[size + 1][size + 1][size + 1];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.arraycopy(polycube.getGrid()[i][j], 0, this.grid[i][j], 0, size);
            }
        }

        this.addCube(newCubeCoordinate);
        this.minimizeGrid();
        this.polycubeValue = -1;
    }

    private Polycube(Polycube polycube) {
        int size = polycube.getGrid().length;
        boolean[][][] newGrid = new boolean[size][size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.arraycopy(polycube.getGrid()[i][j], 0, newGrid[i][j], 0, size);
            }
        }

        this.grid = newGrid;
        this.polycubeValue = polycube.polycubeValue;
    }

    public Polycube(String str) {
        this.polycubeValue = -1;
        String[] parts = str.split(" ");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        int z = Integer.parseInt(parts[2]);

        this.grid = new boolean[x][y][z];
        char[] chars = parts[3].toCharArray();

        int index = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    this.grid[i][j][k] = chars[index++] == '1';
                }
            }
        }
    }

    public Polycube clone() {
        return new Polycube(this);
    }

    public void addCube(Coordinate coordinate) {
        this.grid[coordinate.x()][coordinate.y()][coordinate.z()] = true;
    }

    public boolean[][][] getGrid() {
        return this.grid;
    }

    public void rotateAroundX() {
        rotate(0, 2, 1);
    }

    public void rotateAroundY() {
        rotate(2, 0, 1);
    }

    public void rotateAroundZ() {
        rotate(1, 0, 2);
    }

    private void rotate(int axis1, int axis2, int axis3) {
        this.polycubeValue = -1;
        int xLength = this.grid.length;
        int yLength = this.grid[0].length;
        int zLength = this.grid[0][0].length;

        boolean[][][] copyGrid = this.clone().getGrid();

        int[] lengths = new int[]{xLength, yLength, zLength};

        for (int i = 0; i < lengths[axis1]; i++) {
            for (int j = 0; j < lengths[axis2]; j++) {
                for (int k = 0; k < lengths[axis3]; k++) {
                    int[] oldIndices = new int[3];
                    oldIndices[axis1] = i;
                    oldIndices[axis2] = j;
                    oldIndices[axis3] = k;

                    this.grid[i][j][k] = copyGrid[oldIndices[0]][oldIndices[1]][oldIndices[2]];
                }
            }
        }
    }

    public List<Coordinate> getValidNewCubePlacements() {
        List<Coordinate> validPlacements = new ArrayList<>();
        int size = grid.length;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    // Skip if the current cell is already true
                    if (grid[x][y][z]) continue;

                    // Check the six possible neighbors
                    if ((x > 0 && grid[x - 1][y][z]) ||
                            (x < size - 1 && grid[x + 1][y][z]) ||
                            (y > 0 && grid[x][y - 1][z]) ||
                            (y < size - 1 && grid[x][y + 1][z]) ||
                            (z > 0 && grid[x][y][z - 1]) ||
                            (z < size - 1 && grid[x][y][z + 1])) {

                        validPlacements.add(new Coordinate(x, y, z));
                    }
                }
            }
        }

        return validPlacements;
    }

    private void reshapeGrid() {
        int xMin = grid.length, xMax = 0;
        int yMin = grid[0].length, yMax = 0;
        int zMin = grid[0][0].length, zMax = 0;

        // Flags to identify if the grid needs to grow
        boolean growX = false, growY = false, growZ = false;

        // Finding boundaries with true values
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    if (grid[x][y][z]) {
                        xMin = Math.min(xMin, x);
                        xMax = Math.max(xMax, x);
                        yMin = Math.min(yMin, y);
                        yMax = Math.max(yMax, y);
                        zMin = Math.min(zMin, z);
                        zMax = Math.max(zMax, z);

                        // Check if the grid need to grow
                        if (x == 0 || x == grid.length - 1) growX = true;
                        if (y == 0 || y == grid[0].length - 1) growY = true;
                        if (z == 0 || z == grid[0][0].length - 1) growZ = true;
                    }
                }
            }
        }

        // Adjusting the boundaries to be 1 unit away from true values
        xMin = Math.max(0, xMin - 1);
        xMax = growX ? xMax + 1 : Math.min(grid.length - 1, xMax + 1);
        yMin = Math.max(0, yMin - 1);
        yMax = growY ? yMax + 1 : Math.min(grid[0].length - 1, yMax + 1);
        zMin = Math.max(0, zMin - 1);
        zMax = growZ ? zMax + 1 : Math.min(grid[0][0].length - 1, zMax + 1);

        // Creating the new, modified grid
        int newX = xMax - xMin + 1;
        int newY = yMax - yMin + 1;
        int newZ = zMax - zMin + 1;

        boolean[][][] newGrid = new boolean[newX][newY][newZ];

        for (int x = 0; x < newX; x++) {
            for (int y = 0; y < newY; y++) {
                for (int z = 0; z < newZ; z++) {
                    int oldX = x + xMin;
                    int oldY = y + yMin;
                    int oldZ = z + zMin;

                    if (oldX >= 0 && oldX < grid.length && oldY >= 0 && oldY < grid[0].length && oldZ >= 0 && oldZ < grid[0][0].length) {
                        newGrid[x][y][z] = grid[oldX][oldY][oldZ];
                    }
                }
            }
        }

        // Replace the old grid with the new one
        grid = newGrid;
    }

    public Polycube rotateToCanonicalState() {
        ArrayList<Polycube> rotatedPolycubes = new ArrayList<>();

        // Rotate 4 times around x-axis
        for (int x = 0; x < 4; x++) {
            Polycube rotatedX = (x == 0) ? this.clone() : rotatedPolycubes.get(rotatedPolycubes.size() - 1).clone();
            rotatedX.rotateAroundX();

            // Rotate 4 times around y-axis
            for (int y = 0; y < 4; y++) {
                Polycube rotatedY = (y == 0) ? rotatedX.clone() : rotatedPolycubes.get(rotatedPolycubes.size() - 1).clone();
                rotatedY.rotateAroundY();

                rotatedPolycubes.add(rotatedY);
            }
        }

        // Rotate 2 more times around z-axis (for the two remaining unique rotations)
        Polycube baseZ = this.clone();
        for (int z = 0; z < 2; z++) {
            Polycube rotatedZ = (z == 0) ? baseZ : rotatedPolycubes.get(rotatedPolycubes.size() - 1).clone();
            rotatedZ.rotateAroundZ();

            // Rotate 4 times around x-axis
            for (int x = 0; x < 4; x++) {
                Polycube rotatedX = (x == 0) ? rotatedZ.clone() : rotatedPolycubes.get(rotatedPolycubes.size() - 1).clone();
                rotatedX.rotateAroundX();

                rotatedPolycubes.add(rotatedX);
            }
        }

        return rotatedPolycubes.stream().min(Polycube::compareTo).orElse(this);
    }

    @Override
    public int compareTo(Polycube other) {
        return Long.compare(this.calculateValue(), other.calculateValue());
    }

    private long calculateValue() {
        if (this.polycubeValue > 0) {
            return polycubeValue;

        }
        int xLength = this.grid.length;
        int yLength = this.grid[0].length;
        int zLength = this.grid[0][0].length;

        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yLength; y++) {
                for (int z = 0; z < zLength; z++) {
                    if (this.grid[x][y][z]) {
                        int cubeIndex = coordinatesToIndex(x, y, z);
                        polycubeValue += PrimeNumbers.getNthPrime(cubeIndex);
                    }
                }
            }
        }

        return polycubeValue;
    }

    public int coordinatesToIndex(int x, int y, int z) {
        int xLength = this.grid.length;
        int yLength = this.grid[0].length;
        return x + (y * xLength) + (z * xLength * yLength);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.grid.length).append(" ");
        sb.append(this.grid[0].length).append(" ");
        sb.append(this.grid[0][0].length).append(" ");

        for (boolean[][] booleans : this.grid) {
            for (boolean[] aBoolean : booleans) {
                for (boolean b : aBoolean) {
                    sb.append(b ? '1' : '0');
                }
            }
        }
        return sb.toString();
    }
}