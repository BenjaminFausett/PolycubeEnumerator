package model;

import java.util.*;
import java.util.stream.Collectors;

public class Polycube {

    private static final boolean debugMode = false;
    private static final double DECIMAL_ACCURACY = 17;
    private static final double SCALE = Math.pow(10, DECIMAL_ACCURACY);

    private static final int[][] DIRECTIONS = {
            {0, 1, 0},  // up
            {0, -1, 0}, // down
            {-1, 0, 0}, // left
            {1, 0, 0},  // right
            {0, 0, 1},  // forward
            {0, 0, -1}  // backward
    };

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

        int xSize = grid.length;
        int ySize = grid[0].length;
        int zSize = grid[0][0].length;

        this.grid[coordinate.x()][coordinate.y()][coordinate.z()] = new Cube();

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

    public double[] getCenter() {
        int sumX = 0;
        int sumY = 0;
        int sumZ = 0;
        int count = 0;

        int depth = grid.length;
        int height = grid[0].length;
        int width = grid[0][0].length;

        for (int z = 0; z < depth; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (grid[z][y][x] != null) {
                        sumX += x;
                        sumY += y;
                        sumZ += z;
                        count++;
                    }
                }
            }
        }

        return new double[]{(double) sumX / count, (double) sumY / count, (double) sumZ / count};
    }

    private boolean hasClearPathToBoundary(int x, int y, int z, String direction) {
        switch (direction) {
            case "low x":
                for (int i = x + 1; i < grid.length; i++) {
                    if (grid[i][y][z] != null) return false;
                }
                break;
            case "high x":
                for (int i = 0; i < x; i++) {
                    if (grid[i][y][z] != null) return false;
                }
                break;
            case "low y":
                for (int j = y + 1; j < grid[0].length; j++) {
                    if (grid[x][j][z] != null) return false;
                }
                break;
            case "high y":
                for (int j = 0; j < y; j++) {
                    if (grid[x][j][z] != null) return false;
                }
                break;
            case "low z":
                for (int k = z + 1; k < grid[0][0].length; k++) {
                    if (grid[x][y][k] != null) return false;
                }
                break;
            case "high z":
                for (int k = 0; k < z; k++) {
                    if (grid[x][y][k] != null) return false;
                }
                break;
        }
        return true;
    }

    private int getNumberFromLowX() {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int x = 0; x < xLen; x++) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int y = 0; y < yLen; y++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z] != null && hasClearPathToBoundary(x, y, z, "low x")) {
                        cubesInCurrentLayer++;
                        foundTargetLayer = true;
                    }
                }
            }

            if (foundTargetLayer) {
                return cubesInCurrentLayer;
            }
        }

        return 0;
    }

    private int getNumberFromHighX() {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int x = xLen - 1; x >= 0; x--) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int y = 0; y < yLen; y++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z] != null && hasClearPathToBoundary(x, y, z, "high x")) {
                        cubesInCurrentLayer++;
                        foundTargetLayer = true;
                    }
                }
            }

            if (foundTargetLayer) {
                return cubesInCurrentLayer;
            }
        }

        return 0;
    }

    private int getNumberFromLowY() {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int y = 0; y < yLen; y++) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z] != null && hasClearPathToBoundary(x, y, z, "low y")) {
                        cubesInCurrentLayer++;
                        foundTargetLayer = true;
                    }
                }
            }

            if (foundTargetLayer) {
                return cubesInCurrentLayer;
            }
        }

        return 0;
    }

    private int getNumberFromHighY() {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int y = yLen - 1; y >= 0; y--) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z] != null && hasClearPathToBoundary(x, y, z, "high y")) {
                        cubesInCurrentLayer++;
                        foundTargetLayer = true;
                    }
                }
            }

            if (foundTargetLayer) {
                return cubesInCurrentLayer;
            }
        }

        return 0;
    }

    private int getNumberFromLowZ() {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int z = 0; z < zLen; z++) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int y = 0; y < yLen; y++) {
                    if (grid[x][y][z] != null && hasClearPathToBoundary(x, y, z, "low z")) {
                        cubesInCurrentLayer++;
                        foundTargetLayer = true;
                    }
                }
            }

            if (foundTargetLayer) {
                return cubesInCurrentLayer;
            }
        }

        return 0;
    }

    private int getNumberFromHighZ() {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int z = zLen - 1; z >= 0; z--) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int y = 0; y < yLen; y++) {
                    if (grid[x][y][z] != null && hasClearPathToBoundary(x, y, z, "high z")) {
                        cubesInCurrentLayer++;
                        foundTargetLayer = true;
                    }
                }
            }

            if (foundTargetLayer) {
                return cubesInCurrentLayer;
            }
        }

        return 0;
    }

    @Override
    public int hashCode() {
        int hashCount = 0;

        for (Cube[][] layer : grid) {
            for (Cube[] row : layer) {
                for (Cube cell : row) {
                    if (cell != null) {
                        hashCount += cell.hashCode();
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

        Set<Integer> hashes = Arrays.stream(grid)
                .flatMap(Arrays::stream)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .mapToInt(Cube::hashCode)
                .boxed()
                .collect(Collectors.toSet());

        Set<Integer> otherHashes = Arrays.stream(other.grid)
                .flatMap(Arrays::stream)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .mapToInt(Cube::hashCode)
                .boxed()
                .collect(Collectors.toSet());

        boolean equalHashSet = hashes.equals(otherHashes);

        if(!equalHashSet) {
            return false;
        }

        boolean equalBenNumbers = Arrays.equals(getBensNumbers(), other.getBensNumbers());

        if(!equalBenNumbers) {
            return false;
        }

        if (debugMode && !PolycubeComparator.trueEquals(this, other)) {
            System.out.println("FOUND TWO CUBES WITH SAME HASHS BUT DIFFERENT");
            System.out.println("CUBE 1: ");
            System.out.println(this);
            this.printMetrics();
            System.out.println("\nCUBE 2: ");
            System.out.println(other);
            other.printMetrics();
            return false;
        }

        return true;
    }

    public Cube[][][] getGrid() {
        return this.grid;
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
        System.out.println("Volume: " + volume);
        System.out.println("Bounding box: [" + this.grid.length + ", " + this.grid[0].length + ", " + this.grid[0][0].length + "]");
        System.out.println("HashCode: " + this.hashCode());
        System.out.println("Ben's number: " + Arrays.toString(getBensNumbers()));
        System.out.println("Center mass: " + Arrays.toString(this.getCenter()));
        System.out.println("Cube Hashes: ");
        Arrays.stream(grid)
                .flatMap(Arrays::stream)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .mapToInt(Cube::hashCode)
                .sorted()
                .forEach(System.out::println);
    }

    private int[] getBensNumbers() {
        int[] counts = new int[6];
        counts[0] = getNumberFromLowX();
        counts[1] = getNumberFromHighX();
        counts[2] = getNumberFromLowY();
        counts[3] = getNumberFromHighY();
        counts[4] = getNumberFromLowZ();
        counts[5] = getNumberFromHighZ();

        Arrays.sort(counts);
        return counts;
    }
}