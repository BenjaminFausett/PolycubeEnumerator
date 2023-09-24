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
    }

    public Polycube(Polycube polycube, Coordinate newCubeCoordinate) {
        this(polycube);
        this.addCube(newCubeCoordinate);
        this.shrinkGrid();
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
        this.rotation = new Rotation(polycube.getRotation().xRotations(), polycube.getRotation().yRotations(), polycube.getRotation().zRotations());
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

    public Rotation getRotation() {
        return rotation;
    }

    public void rotate(int x, int y, int z) {
        this.rotate(new Rotation(x, y, z));
    }

    public void rotate(Rotation rotation) {
        int newX = this.rotation.xRotations() + rotation.xRotations();
        int newY = this.rotation.yRotations() + rotation.yRotations();
        int newZ = this.rotation.zRotations() + rotation.zRotations();
        this.rotation = new Rotation(newX, newY, newZ);
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

    @Override
    public int hashCode() {//TODO the bug is probably here. i think its not returning the same numbers for the same shape and rotation, could also be in the coordinate system, who knows if its actually mapping to the correct virtual rotation
        boolean[] booleans = new boolean[grid.length * grid.length * grid.length];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                for (int z = 0; z < grid[x][y].length; z++) {
                    Coordinate coordinate = new Coordinate(x, y, z, rotation, grid.length);
                    booleans[coordinateToIndex(coordinate)] = this.isCube(coordinate);
                }
            }
        }
        return Arrays.hashCode(booleans);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Polycube other)) {
            return false;
        }
        if(other.getNumOfCubes() != this.numOfCubes) {
            return false;
        }

        if (grid.length != other.getGrid().length) {
            return false;
        }

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

    private boolean isCube(int x, int y, int z) {
        return isCube(new Coordinate(x, y, z, rotation, grid.length));
    }

    private void addBuffer() {
        int length = this.grid.length;

        boolean[][][] bufferedArray = new boolean[length + 2][length + 2][length + 2];

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                System.arraycopy(this.grid[x][y], 0, bufferedArray[x + 1][y + 1], 1, length);
            }
        }

        this.grid = bufferedArray;
    }

    private void shrinkGrid() {
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
}