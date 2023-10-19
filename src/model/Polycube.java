package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Polycube {

    private final static double DECIMAL_ACCURACY = 10;
    private static final double SCALE = Math.pow(10, DECIMAL_ACCURACY);

    private Cube[][][] grid;
    private int volume;

    //Creates the one and only perfect MonoCube
    public Polycube() {
        this.grid = new Cube[1][1][1];
        this.volume = 0;
        this.addCube(new Coordinate(0, 0, 0));
    }

    public Polycube(Polycube polycube, Coordinate newCubeCoordinate) {
        this(polycube);
        this.addCube(newCubeCoordinate);
        this.shrinkGrid();
    }

    private Polycube(Polycube polycube) {
        int xLength = polycube.grid.length;
        Cube[][][] copy = new Cube[xLength][][];

        for (int x = 0; x < xLength; x++) {
            if (polycube.grid[x] == null) {
                continue;
            }

            int yLength = polycube.grid[x].length;
            copy[x] = new Cube[yLength][];

            for (int y = 0; y < yLength; y++) {
                if (polycube.grid[x][y] == null) {
                    continue;
                }

                int zLength = polycube.grid[x][y].length;
                copy[x][y] = new Cube[zLength];

                for (int z = 0; z < zLength; z++) {
                    if (polycube.grid[x][y][z] != null) {
                        copy[x][y][z] = polycube.grid[x][y][z].clone();
                    }
                }
            }
        }

        this.grid = copy;
        this.volume = polycube.volume;
    }

    public Polycube clone() {
        return new Polycube(this);
    }

    public int getVolume() {
        return this.volume;
    }

    public void addCube(Coordinate coordinate) {
        this.volume += 1;
        this.grid[coordinate.x()][coordinate.y()][coordinate.z()] = new Cube();

        int xSize = grid.length;
        int ySize = grid[0].length;
        int zSize = grid[0][0].length;

        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    if (grid[x][y][z] != null) {

                        if (x == coordinate.x() && y == coordinate.y() && z == coordinate.z()) {
                            continue;
                        }

                        int dx = Math.abs(coordinate.x() - x);
                        int dy = Math.abs(coordinate.y() - y);
                        int dz = Math.abs(coordinate.z() - z);

                        double distance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
                        distance = Math.round(distance * SCALE) / SCALE;

                        grid[x][y][z].addToDistances(distance);
                        grid[coordinate.x()][coordinate.y()][coordinate.z()].addToDistances(distance);
                    }
                }
            }
        }
    }

    public List<Coordinate> getValidNewCubePlacements() {
        this.addBuffer();
        List<Coordinate> validPlacements = new ArrayList<>();
        int xSize = grid.length;
        int ySize = grid[0].length;
        int zSize = grid[0][0].length;

        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    if (this.grid[x][y][z] != null) continue;

                    if ((x > 0 && this.grid[x - 1][y][z] != null) ||
                            (x < xSize - 1 && this.grid[x + 1][y][z] != null) ||
                            (y > 0 && this.grid[x][y - 1][z] != null) ||
                            (y < ySize - 1 && this.grid[x][y + 1][z] != null) ||
                            (z > 0 && this.grid[x][y][z - 1] != null) ||
                            (z < zSize - 1 && this.grid[x][y][z + 1] != null)) {

                        validPlacements.add(new Coordinate(x, y, z));
                    }
                }
            }
        }

        return validPlacements;
    }

    private void addBuffer() {
        int xLength = this.grid.length;
        int yLength = this.grid[0].length;
        int zLength = this.grid[0][0].length;

        Cube[][][] bufferedGrid = new Cube[xLength + 2][yLength + 2][zLength + 2];

        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yLength; y++) {
                for (int z = 0; z < zLength; z++) {
                    if (this.grid[x][y][z] != null) {
                        bufferedGrid[x + 1][y + 1][z + 1] = this.grid[x][y][z].clone();
                    }
                }
            }
        }

        this.grid = bufferedGrid;
    }

    private void shrinkGrid() {
        int xMin = Integer.MAX_VALUE, xMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE, yMax = Integer.MIN_VALUE;
        int zMin = Integer.MAX_VALUE, zMax = Integer.MIN_VALUE;

        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int x = 0; x < xLen; x++) {
            for (int y = 0; y < yLen; y++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z] != null) {
                        xMin = Math.min(xMin, x);
                        xMax = Math.max(xMax, x);
                        yMin = Math.min(yMin, y);
                        yMax = Math.max(yMax, y);
                        zMin = Math.min(zMin, z);
                        zMax = Math.max(zMax, z);
                    }
                }
            }
        }

        if (xMin > xMax || yMin > yMax || zMin > zMax) {
            this.grid = new Cube[0][0][0];
            return;
        }

        Cube[][][] shrunkGrid = new Cube[xMax - xMin + 1][yMax - yMin + 1][zMax - zMin + 1];

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    if (grid[x][y][z] != null) {
                        shrunkGrid[x - xMin][y - yMin][z - zMin] = grid[x][y][z].clone();
                    }
                }
            }
        }

        this.grid = shrunkGrid;
    }

    @Override
    public int hashCode() {
        int hashCount = 0;
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int x = 0; x < xLen; x++) {
            for (int y = 0; y < yLen; y++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z] != null) {
                        hashCount += grid[x][y][z].hashCode();
                    }
                }
            }
        }

        return hashCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Polycube other)) {
            return false;
        }

        int[] cubeHashes = Arrays.stream(grid)
                .flatMap(Arrays::stream)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .mapToInt(Cube::hashCode)
                .sorted().toArray();

        int[] otherHashes = Arrays.stream(other.grid)
                .flatMap(Arrays::stream)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .mapToInt(Cube::hashCode)
                .sorted().toArray();

        return Arrays.equals(cubeHashes, otherHashes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int z = 0; z < grid[0][0].length; z++) {
            sb.append("Layer ").append(z).append(":\n");

            for (Cube[][] cubes : grid) {
                for (int x = 0; x < grid[0].length; x++) {
                    if (cubes[x][z] != null) {
                        sb.append("[#]");
                    } else {
                        sb.append("[ ]");
                    }
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void printMetrics() {
        System.out.println();
        System.out.println("Volume: " + volume);
        System.out.println("Bounding box: [" + this.grid.length + ", " + this.grid[0].length + ", " + this.grid[0][0].length + "]");
        System.out.println("HashCode: " + this.hashCode());
        System.out.println("Distances map:");
        Arrays.stream(grid)
                .flatMap(Arrays::stream)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .sorted()
                .toList()
                .forEach(System.out::println);
        System.out.println();
    }
}