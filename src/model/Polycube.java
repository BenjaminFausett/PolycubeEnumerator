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
        this.grid = new boolean[1][1][1];
        this.rotation = new Rotation(0, 0, 0);
        this.addCube(new Coordinate(0, 0, 0, rotation, grid.length));
        this.numOfCubes = 1;
        this.rotateToCanonicalState();
    }

    public Polycube(Polycube polycube, Coordinate newCubeCoordinate) {
        this(polycube);

        this.addCube(newCubeCoordinate);
        this.shrinkGrid();
        this.setCanonicalRotation();
        System.out.println("value: " + this.getCanonicalValue());
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

    private void setRotation(int xRotations, int yRotations, int zRotations) {
        setRotation(new Rotation(xRotations, yRotations, zRotations));
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
        Rotation cannonicalRotation = null;

        for (Rotation rotation : Rotation.getAllRotations()) {
            this.setRotation(rotation);
            long value = this.calculateValue();

            if (value < minValue) {
                minValue = value;
                cannonicalRotation = rotation;
                this.canonicalValue = value;
            }
        }

        this.setRotation(cannonicalRotation);
    }

    private long calculateValue() {
        int length = this.grid.length;
        long value = 0;

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                for (int z = 0; z < length; z++) {
                    Coordinate coordinate = new Coordinate(x, y , z, rotation, length);
                    if (this.isCube(coordinate)) {
                        int prime = PrimeNumbers.getNthPrime(coordinateToIndex(coordinate));
                        //prime = prime * (coordinate.x() + coordinate.y() + coordinate.z() + 1);
                        value += prime;
                    }
                }
            }
        }

//        System.out.println("value: " + value);
//        System.out.println("rotation: " + rotation);
//        System.out.println("num of cubes: " + numOfCubes);
//        System.out.println(this);

        return value;
    }

    private int coordinateToIndex(Coordinate coordinate) {
        int length = this.grid.length;
        return coordinate.x() + (coordinate.y() * length) + (coordinate.z() * length * length);
    }

    public int coordinateToIndex(int x, int y, int z) {
       return coordinateToIndex(new Coordinate(x, y, z, rotation, this.grid.length));
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

    private boolean isCube(Coordinate coordinate) {
        return this.grid[coordinate.x()][coordinate.y()][coordinate.z()];
    }

    private boolean isCube(int x, int y, int z) {
        Coordinate coordinate = new Coordinate(x, y, z, rotation, grid.length);
        return isCube(coordinate);
    }

    public long getCanonicalValue() {
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

    public void setCanonicalRotation() {
        int canonicalHash = hash3DArray();
        Rotation cannonicalRotation = this.rotation;

        for (Rotation rotation : Rotation.getAllRotations()) {
            if(rotation.equals(this.rotation)) {
                continue;
            }
            this.setRotation(rotation);
            int hash = hash3DArray();

            if (hash < canonicalHash) {
                canonicalHash = hash;
                cannonicalRotation = rotation;
            }
        }

        this.setRotation(cannonicalRotation);
    }

    public int hash3DArray() {
        boolean[] bools = new boolean[grid.length * grid.length * grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                for (int k = 0; k < grid[i][j].length; k++) {
                    Coordinate coordinate = new Coordinate(i, j, k, rotation, grid.length);
                    bools[coordinateToIndex(coordinate)] = this.isCube(i, j, k);
                }
            }
        }
        System.out.println(bools.hashCode());
        return bools.hashCode();
    }
    //something is going wrong. the hash codes this makes are all unique. but even two matching shapes are giving differnt hashcodes. they are the same shape and oriented the way, they just have a different gird location rotation. figure that out.

}