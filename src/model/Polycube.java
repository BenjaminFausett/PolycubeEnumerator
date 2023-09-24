package model;

import java.util.ArrayList;
import java.util.List;

public class Polycube {

    private boolean[][][] grid;
    private long canonicalValue;
    private int numOfCubes;

    private Rotation rotation;


    //Creates the one and only perfect Mono Cube
    public Polycube() {
        this.grid = new boolean[3][3][3];
        this.rotation = new Rotation(0, 0, 0);
        this.addCube(new Coordinate(1, 1, 1, rotation, grid.length));
        this.numOfCubes = 1;
        this.rotateToCanonicalState();
    }

    public Polycube(Polycube polycube, Coordinate newCubeCoordinate) {
        this(polycube);

        this.addCube(newCubeCoordinate);
        this.shrinkGrid();
        this.rotateToCanonicalState();
    }

    private Polycube(Polycube polycube) {
        int size = polycube.getGrid().length;
        this.grid = new boolean[size][size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.arraycopy(polycube.getGrid()[i][j], 0, this.grid[i][j], 0, size);
            }
        }

        this.canonicalValue = polycube.canonicalValue;
        this.numOfCubes = polycube.numOfCubes;
        this.rotation = new Rotation(polycube.rotation.xRotations(), polycube.rotation.yRotations(), polycube.rotation.zRotations());
    }

    public Polycube clone() {
        return new Polycube(this);
    }

    public void addCube(Coordinate coordinate) {
        this.numOfCubes += 1;
        this.grid[coordinate.x()][coordinate.y()][coordinate.z()] = true;
    }

    public boolean[][][] getGrid() {
        return this.grid;
    }

    public int getNumOfCubes() {
        return this.numOfCubes;
    }

    private void rotate(int xRotations, int yRotations, int zRotations) {
        this.rotation = new Rotation(xRotations, yRotations, zRotations);
    }

    public List<Coordinate> getValidNewCubePlacements() {
        this.addBuffer();
        List<Coordinate> validPlacements = new ArrayList<>();
        int size = grid.length;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    // Skip if the current cell is already a cube
                    if (this.isCube(x, y, z)) continue;

                    // Check the six possible neighbors
                    if ((x > 0 && this.isCube(x - 1, y, z)) ||
                            (x < size - 1 && this.isCube(x + 1, y, z)) ||
                            (y > 0 && this.isCube(x, y - 1, z)) ||
                            (y < size - 1 && this.isCube(x, y + 1, z)) ||
                            (z > 0 && this.isCube(x, y, z - 1)) ||
                            (z < size - 1 && this.isCube(x, y, z + 1))) {

                        validPlacements.add(new Coordinate(x, y, z, rotation, grid.length));
                    }
                }
            }
        }

        return validPlacements;
    }

    private void addBuffer() {
        int length = this.grid.length;

        boolean[][][] bufferedArray = new boolean[length + 2][length + 2][length + 2];

        // Copy the original array values into the buffered array
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                System.arraycopy(this.grid[x][y], 0, bufferedArray[x + 1][y + 1], 1, length);
            }
        }

        this.grid = bufferedArray;
    }

    public void shrinkToSmallestPerfectCube() {
        int xMin = Integer.MAX_VALUE, xMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE, yMax = Integer.MIN_VALUE;
        int zMin = Integer.MAX_VALUE, zMax = Integer.MIN_VALUE;

        for (int x = 0; x < this.grid.length; x++) {
            for (int y = 0; y < this.grid[0].length; y++) {
                for (int z = 0; z < this.grid[0][0].length; z++) {
                    if (this.isCube(x, y, z)) {
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

        int maxLength = Math.max(Math.max(xMax - xMin, yMax - yMin), zMax - zMin) + 1;

        boolean[][][] shrunkArray = new boolean[maxLength][maxLength][maxLength];

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                if (zMax + 1 - zMin >= 0)
                    System.arraycopy(this.grid[x][y], zMin, shrunkArray[x - xMin][y - yMin], 0, zMax + 1 - zMin);
            }
        }

        this.grid = shrunkArray;
    }

    public void shrinkGrid() {
        int n = grid.length;
        int minLayer = n, maxLayer = -1;
        int minRow = n, maxRow = -1;
        int minCol = n, maxCol = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (grid[i][j][k]) {
                        minLayer = Math.min(minLayer, i);
                        maxLayer = Math.max(maxLayer, i);
                        minRow = Math.min(minRow, j);
                        maxRow = Math.max(maxRow, j);
                        minCol = Math.min(minCol, k);
                        maxCol = Math.max(maxCol, k);
                    }
                }
            }
        }

        int maxDimension = Math.max(Math.max(maxLayer - minLayer, maxRow - minRow), maxCol - minCol) + 1;

        boolean[][][] newGrid = new boolean[maxDimension][maxDimension][maxDimension];

        for (int i = 0; i < maxDimension; i++) {
            for (int j = 0; j < maxDimension; j++) {
                for (int k = 0; k < maxDimension; k++) {
                    int x = i + minLayer;
                    int y = j + minRow;
                    int z = k + minCol;

                    if (x < n && y < n && z < n) {
                        newGrid[i][j][k] = grid[x][y][z];
                    }
                }
            }
        }

        this.grid = newGrid;
    }

    public void rotateToCanonicalState() {
        long minValue = Long.MAX_VALUE;
        int xRotations = 0;
        int yRotations = 0;
        int zRotations = 0;

        int[] possibleRotations = {0, 1, 2, 3};

        for (int x : possibleRotations) {
            for (int y : possibleRotations) {
                for (int z : possibleRotations) {
                    this.rotate(x, y, z);
                    long value = this.calculateValue();

                    if (value < minValue) {
                        minValue = value;
                        xRotations = x;
                        yRotations = y;
                        zRotations = z;
                        this.canonicalValue = value;
                    }
                }
            }
        }
        this.rotate(xRotations, yRotations, zRotations);
    }

    private long calculateValue() {
        int length = this.grid.length;
        long value = 0;

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                for (int z = 0; z < length; z++) {
                    if (this.isCube(x, y, z)) {
                        value += PrimeNumbers.getNthPrime(coordinateToIndex(x, y, z));
                    }
                }
            }
        }

        return value;
    }

    public int coordinateToIndex(int x, int y, int z) {
        Coordinate coordinate = new Coordinate(x, y, z, rotation, grid.length);
        int length = this.grid.length;
        return coordinate.x() + (coordinate.y() * length) + (coordinate.z() * length * length);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        // Hash polycubeValue
        result = prime * result + (int) (canonicalValue ^ (canonicalValue >>> 32));

        // Hash the grid
        for (boolean[][] booleans : grid) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    result = prime * result + (booleans[y][z] ? 1231 : 1237);
                }
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // Check if the object is an instance of Polycube
        if (!(obj instanceof Polycube other)) {
            return false;
        }
        if(other.getNumOfCubes() != this.numOfCubes) {
            return false;
        }

        // Check if polycube values are the same
        if(this.canonicalValue != other.getCanonicalValue()) {
            return false;
        }

        // Check if dimensions of grid are the same
        if (grid.length != other.getGrid().length) {
            return false;
        }

        // Compare grid values
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    if (this.isCube(x, y, z) != other.isCube(x, y, z)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean isCube(int x, int y, int z) {
        Coordinate coordinate = new Coordinate(x, y, z, rotation, grid.length);
        return this.grid[coordinate.x()][coordinate.y()][coordinate.z()];
    }

    private long getCanonicalValue() {
        return this.canonicalValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            sb.append("Layer ").append(i).append(":\n");
            for (int j = 0; j < grid[i].length; j++) {
                for (int k = 0; k < grid[i][j].length; k++) {
                    sb.append(grid[i][j][k] ? "1" : "0").append(" ");
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}