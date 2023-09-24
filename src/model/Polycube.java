package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polycube {

    private boolean[][][] grid;
    private int numOfCubes;
    private Rotation rotation;


    //Creates the one and only perfect Mono Cube
    public Polycube() {
        this.grid = new boolean[1][1][1];
        this.rotation = new Rotation(0, 0, 0);
        this.addCube(new Coordinate(0, 0, 0, rotation, grid.length));
        this.numOfCubes = 1;
        this.setToCanonicalRotation();
    }

    public Polycube(Polycube polycube, Coordinate newCubeCoordinate) {
        this(polycube);

        this.addCube(newCubeCoordinate);
        this.shrinkGrid();
        this.setToCanonicalRotation();
    }

    private Polycube(Polycube polycube) {
        int size = polycube.getGrid().length;
        this.grid = new boolean[size][size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.arraycopy(polycube.getGrid()[i][j], 0, this.grid[i][j], 0, size);
            }
        }

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

    private void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public List<Coordinate> getValidNewCubePlacements() {
        this.addBuffer();
        List<Coordinate> validPlacements = new ArrayList<>();
        int size = grid.length;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
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

    private boolean isCube(int x, int y, int z) {
        return isCube(new Coordinate(x, y, z, rotation, grid.length));
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

    private int coordinateToIndex(Coordinate coordinate) {
        int length = this.grid.length;
        return coordinate.x() + (coordinate.y() * length) + (coordinate.z() * length * length);
    }

    @Override
    public int hashCode() {
        boolean[] bools = new boolean[grid.length * grid.length * grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                for (int k = 0; k < grid[i][j].length; k++) {
                    Coordinate coordinate = new Coordinate(i, j, k, rotation, grid.length);
                    bools[coordinateToIndex(coordinate)] = this.isCube(coordinate);
                }
            }
        }
        //System.out.println(bools.hashCode());
        return Arrays.hashCode(bools);
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

        // Check if dimensions of grid are the same
        if (grid.length != other.getGrid().length) {
            return false;
        }

        // Compare hashCodes
        return this.hashCode() == other.hashCode();
    }

    private boolean isCube(Coordinate coordinate) {
        return this.grid[coordinate.x()][coordinate.y()][coordinate.z()];
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            sb.append("Layer ").append(i).append(":\n");
            for (int j = 0; j < grid[i].length; j++) {
                for (int k = 0; k < grid[i][j].length; k++) {
                    sb.append(isCube(i, j, k) ? "1" : "0").append(" ");
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void setToCanonicalRotation() {
        int canonicalHash = hashCode();
        Rotation cannonicalRotation = this.rotation;

        for (Rotation rotation : Rotation.getAllRotations()) {
            if(rotation.equals(this.rotation)) {
                continue;
            }
            this.setRotation(rotation);
            int hash = hashCode();

            if (hash < canonicalHash) {
                canonicalHash = hash;
                cannonicalRotation = rotation;
            }
        }

        this.setRotation(cannonicalRotation);
    }
}