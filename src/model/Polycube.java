package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Polycube {

    private boolean[][][] grid;

    private int volume;
    private int surfaceArea;
    private int vertices;
    private int edges;
    private int boundingBoxVolume;
    private Coordinate centroid;
    private double distanceVectorsSum;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int z = 0; z < grid.length; z++) {
            sb.append("Layer ").append(z).append(":\n");
            for (int y = 0; y < grid[z].length; y++) {
                for (int x = 0; x < grid[z][y].length; x++) {
                    sb.append(grid[z][y][x] ? "1" : "0");
                    sb.append(" ");
                }
                sb.append("\n");
            }
            sb.append("\n");
        }

        sb.append(("" + this.edges + this.surfaceArea + this.vertices + this.volume + this.boundingBoxVolume + this.distanceVectorsSum));
        return sb.toString();
    }

    //Creates the one and only perfect Mono Cube
    public Polycube() {
        this.grid = new boolean[1][1][1];
        this.volume = 0;
        this.addCube(new Coordinate(0, 0, 0));
        this.calculateSurfaceArea();
        this.calculateVertices();
        this.calculateEdges();
        this.calculateCentroid();
        this.calculateBoundingBoxVolume();
        this.calculateDistanceVectors();
        this.calculateExposedVertices();
    }

    public Polycube(Polycube polycube, Coordinate newCubeCoordinate) {
        this(polycube);
        this.addCube(newCubeCoordinate);
        this.shrinkGrid();
        this.calculateSurfaceArea();
        this.calculateVertices();
        this.calculateEdges();
        this.calculateCentroid();
        this.calculateBoundingBoxVolume();
        this.calculateDistanceVectors();
        this.calculateExposedVertices();
    }

    private void calculateBoundingBoxVolume() {
        this.boundingBoxVolume = this.grid.length * this.grid[0].length * this.grid[0][0].length;
    }

    public int getVolume() {
        return this.volume;
    }

    private Polycube(Polycube polycube) {
        int xLength = polycube.grid.length;
        boolean[][][] copy = new boolean[xLength][][];

        for (int x = 0; x < xLength; x++) {
            if (polycube.grid[x] == null) {
                continue;
            }

            int yLength = polycube.grid[x].length;
            copy[x] = new boolean[yLength][];

            for (int y = 0; y < yLength; y++) {
                if (polycube.grid[x][y] == null) {
                    continue;
                }

                int zLength = polycube.grid[x][y].length;
                copy[x][y] = new boolean[zLength];

                System.arraycopy(polycube.grid[x][y], 0, copy[x][y], 0, zLength);
            }
        }

        this.grid = copy;

        this.volume = polycube.volume;
        this.surfaceArea = polycube.surfaceArea;
        this.edges = polycube.edges;
        this.centroid = polycube.centroid;
        this.distanceVectorsSum = polycube.distanceVectorsSum;
        this.vertices = polycube.vertices;
    }

    public Polycube clone() {
        return new Polycube(this);
    }

    public void addCube(Coordinate coordinate) {
        this.volume += 1;
        this.grid[(int) coordinate.x()][(int) coordinate.y()][(int) coordinate.z()] = true;
    }

    public void calculateSurfaceArea() {
        int surfaceArea = 0;
        int[] dx = {-1, 1, 0, 0, 0, 0};
        int[] dy = {0, 0, -1, 1, 0, 0};
        int[] dz = {0, 0, 0, 0, -1, 1};

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    if (grid[x][y][z]) {
                        int exposedFaces = 6; // Max number of exposed faces for a cube
                        for (int i = 0; i < 6; i++) {
                            int newX = x + dx[i];
                            int newY = y + dy[i];
                            int newZ = z + dz[i];
                            if (newX >= 0 && newX < grid.length &&
                                    newY >= 0 && newY < grid[0].length &&
                                    newZ >= 0 && newZ < grid[0][0].length &&
                                    grid[newX][newY][newZ]) {
                                exposedFaces--;
                            }
                        }
                        surfaceArea += exposedFaces;
                    }
                }
            }
        }
        this.surfaceArea = surfaceArea;
    }

    public void calculateEdges() {//TODO, this aint right, should only count visiable ones, and some edges can be combined
        int edges = 0;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    if (grid[x][y][z]) {
                        int[] dx = {0, 0, 1, 0, 0, -1};
                        int[] dy = {0, 1, 0, 0, -1, 0};
                        int[] dz = {1, 0, 0, -1, 0, 0};
                        for (int i = 0; i < 6; i++) {
                            int newX = x + dx[i];
                            int newY = y + dy[i];
                            int newZ = z + dz[i];
                            if (newX >= 0 && newX < grid.length &&
                                    newY >= 0 && newY < grid[0].length &&
                                    newZ >= 0 && newZ < grid[0][0].length &&
                                    !grid[newX][newY][newZ]) {
                                edges += 4;  // Each exposed face contributes 4 edges
                            }
                        }
                    }
                }
            }
        }
        this.edges = edges / 2;  // Each edge is counted twice, so divide by 2
    }

    private void calculateVertices() { //TODO
        this.vertices = 0;
    }

    private void calculateCentroid() {
        double totalX = 0, totalY = 0, totalZ = 0;
        int count = 0;

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    if (grid[x][y][z]) {
                        totalX += x + 0.5;
                        totalY += y + 0.5;
                        totalZ += z + 0.5;
                        count++;
                    }
                }
            }
        }

        if (count == 0) {
            this.centroid = new Coordinate(0, 0, 0);  // Return the origin if there are no cubes
        }

        this.centroid = new Coordinate(totalX / count, totalY / count, totalZ / count);
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
                    if (this.grid[x][y][z]) continue;

                    if ((x > 0 && this.grid[x - 1][y][z]) ||
                            (x < xSize - 1 && this.grid[x + 1][y][z]) ||
                            (y > 0 && this.grid[x][y - 1][z]) ||
                            (y < ySize - 1 && this.grid[x][y + 1][z]) ||
                            (z > 0 && this.grid[x][y][z - 1]) ||
                            (z < zSize - 1 && this.grid[x][y][z + 1])) {

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

        boolean[][][] bufferedArray = new boolean[xLength + 2][yLength + 2][zLength + 2];

        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yLength; y++) {
                System.arraycopy(this.grid[x][y], 0, bufferedArray[x + 1][y + 1], 1, zLength);
            }
        }

        this.grid = bufferedArray;
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
                    if (grid[x][y][z]) {
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
            this.grid = new boolean[0][0][0];
        }

        boolean[][][] shrunkGrid = new boolean[xMax - xMin + 1][yMax - yMin + 1][zMax - zMin + 1];

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                if (zMax + 1 - zMin >= 0)
                    System.arraycopy(grid[x][y], zMin, shrunkGrid[x - xMin][y - yMin], 0, zMax + 1 - zMin);
            }
        }

        this.grid = shrunkGrid;
    }

    public boolean isValidCube() {
        int length = grid.length;

        boolean[][][] visited = new boolean[length][length][length];

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                for (int z = 0; z < length; z++) {
                    if (!grid[x][y][z] && !visited[x][y][z]) {
                        if (!canFindBoundary(visited, x, y, z)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean canFindBoundary(boolean[][][] visited, int x, int y, int z) {
        int length = grid.length;

        if (x < 0 || y < 0 || z < 0 || x >= length || y >= length || z >= length) {
            return true;
        }

        if (visited[x][y][z] || grid[x][y][z]) {
            return false;
        }

        visited[x][y][z] = true;

        boolean isBoundary = false;

        isBoundary |= canFindBoundary(visited, x + 1, y, z);
        isBoundary |= canFindBoundary(visited, x - 1, y, z);
        isBoundary |= canFindBoundary(visited, x, y + 1, z);
        isBoundary |= canFindBoundary(visited, x, y - 1, z);
        isBoundary |= canFindBoundary(visited, x, y, z + 1);
        isBoundary |= canFindBoundary(visited, x, y, z - 1);

        return isBoundary;
    }

    public void calculateDistanceVectors() {

        double xDistance = 0;
        double yDistance = 0;
        double zDistance = 0;

        // Iterate through the 3D array to find cubes and calculate distance vectors
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    if (grid[x][y][z]) {
                        Coordinate cube = new Coordinate(x, y, z);

                        xDistance += Math.abs(cube.x() - this.centroid.x());
                        yDistance += Math.abs(cube.y() - this.centroid.y());
                        zDistance += Math.abs(cube.z() - this.centroid.z());
                    }
                }
            }
        }

        this.distanceVectorsSum = xDistance + yDistance + zDistance;
    }

    public void calculateExposedVertices() {
        int exposedVerticesCount = 0;

        int xLength = grid.length;
        int yLength = grid[0].length;
        int zLength = grid[0][0].length;

        int[][] dirs = {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};

        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yLength; y++) {
                for (int z = 0; z < zLength; z++) {
                    // Skip if this cell is empty
                    if (!grid[x][y][z]) continue;

                    for (int dx = 0; dx <= 1; dx++) {
                        for (int dy = 0; dy <= 1; dy++) {
                            for (int dz = 0; dz <= 1; dz++) {
                                int exposedCount = 0;
                                for (int[] dir : dirs) {
                                    int newX = x + dx + dir[0];
                                    int newY = y + dy + dir[1];
                                    int newZ = z + dz + dir[2];
                                    if (newX < 0 || newY < 0 || newZ < 0 ||
                                            newX >= xLength || newY >= yLength || newZ >= zLength ||
                                            !grid[newX][newY][newZ]) {
                                        exposedCount++;
                                    }
                                }

                                if (exposedCount >= 3) {
                                    exposedVerticesCount += 1;
                                }
                            }
                        }
                    }
                }
            }
        }

        this.vertices = exposedVerticesCount;
    }

    @Override
    public int hashCode() {
        return ("" + this.edges + this.surfaceArea + this.vertices + this.volume + this.boundingBoxVolume + this.distanceVectorsSum).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Polycube other)) {
            return false;
        }
        if (this.edges != other.edges || this.surfaceArea != other.surfaceArea || this.vertices != other.vertices || this.volume != other.volume || this.boundingBoxVolume != other.boundingBoxVolume || this.distanceVectorsSum != other.distanceVectorsSum) {
            return false;
        }

        return this.hashCode() == other.hashCode();
    }

    public void printMetrics() {
        System.out.println("edges: " + edges);
        System.out.println("surfaceArea: " + surfaceArea);
        System.out.println("vertices: " + vertices);
        System.out.println("volume: " + volume);
        System.out.println("boundingBoxVolume: " + boundingBoxVolume);
    }
}