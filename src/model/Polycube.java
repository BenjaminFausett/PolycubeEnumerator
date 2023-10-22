package model;

import java.util.*;

public class Polycube {

    private static final boolean DEBUG_MODE = false;
    private static final boolean PERFECT_MODE = false;
    private static final int DECIMAL_ACCURACY = 12;
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

    public Cube[][][] getGrid() {
        return this.grid;
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

                        int manhattanDistance = dx + dy + dz;
                        double euclideanDistance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
                        euclideanDistance = Math.round(euclideanDistance * SCALE) / SCALE;

                        grid[x][y][z].addDistances(euclideanDistance, manhattanDistance);
                        grid[coordinate.x()][coordinate.y()][coordinate.z()].addDistances(euclideanDistance, manhattanDistance);
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

                    for (int[] dir : DIRECTIONS) {
                        int xOffset = x + dir[0];
                        int yOffset = y + dir[1];
                        int zOffset = z + dir[2];

                        if (xOffset < 0 || xOffset >= xSize || yOffset < 0 || yOffset >= ySize || zOffset < 0 || zOffset >= zSize) {
                            continue;
                        }
                        if (this.grid[xOffset][yOffset][zOffset] != null) {
                            validPlacements.add(new Coordinate(x, y, z));
                        }
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

    //as a reminder to future ben, this equals method only ever runs if the hashes of the two objects are the same, so dont recheck that
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Polycube other)) {
            return false;
        }

        if(!Arrays.equals(this.getBensNumbers(), other.getBensNumbers())) {
            return false;
        }

        if (PERFECT_MODE && !PolycubeComparator.trueEquals(this, other)) {
            if(DEBUG_MODE) {
                System.out.println("\n~~~Two different cubes that passed all equality checks found~~~");
                System.out.println("Cube 1: ");
                System.out.println(this);
                this.printMetrics();
                System.out.println("\nCube 2: ");
                System.out.println(other);
                other.printMetrics();
            }
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int z = 0; z < grid[0][0].length; z++) {
            sb.append("Layer ").append(z).append(":\n");

            for (Cube[][] cubes : grid) {
                for (int x = 0; x < grid[0].length; x++) {
                    if (cubes[x][z] != null) {
                        sb.append("[").append("#").append("]");
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
        //System.out.println("Ben's numbers: " + Arrays.toString(getBensNumbers()));
    }

    //Called Bens numbers because I could not find a good name for this metric.
    //this finds how many visible faces there are when viewed from each of the 6 directions
    //BUT - it only considers faces that are on the layer that is furthest from the viewing side that that contains a visible face.
    //Hard to explain but spin a real polycube around in your hand and look at it from any 90 degree angle. Bens number is the count of visible faces from the layer furthest from your face that still have a visible face.
    private int[] getBensNumbers() {
        //TODO make these methods return a hashmap which is a collection of how many faces can see the boundry at each layer. add all that up into one master hashmap using putAll, then hash that to get Bens Hash!
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
}