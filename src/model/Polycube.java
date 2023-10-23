package model;

import config.Options;
import model.records.BenPair;
import model.records.Point;
import model.util.RotationComparator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Polycube implements Serializable {

    private static final int DECIMAL_ACCURACY = 12;
    private static final double SCALE = Math.pow(10, DECIMAL_ACCURACY);

    private final List<Cube> cubes;

    //Creates the one and only perfect MonoCube
    public Polycube() {
        this.cubes = new ArrayList<>();
        this.cubes.add(new Cube(100, 100, 100));
    }

    private Polycube(Polycube polycube) {
        this.cubes = new ArrayList<>();
        for (Cube cube : polycube.cubes) {
            this.cubes.add(cube.clone());
        }
    }

    public Polycube(Polycube polycube, Point newCubePoint) {
        this(polycube);
        this.addCube(newCubePoint);
    }

    public Polycube clone() {
        return new Polycube(this);
    }

    public void addCube(Point point) {
        Cube newCube = new Cube(point);

        for (Cube cube : cubes) {
            int dx = Math.abs(cube.getPoint().x() - newCube.getPoint().x());
            int dy = Math.abs(cube.getPoint().y() - newCube.getPoint().y());
            int dz = Math.abs(cube.getPoint().z() - newCube.getPoint().z());

            int manhattanDistance = dx + dy + dz;
            double euclideanDistance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
            int euclideanDistanceHash = Double.hashCode(Math.round(euclideanDistance * SCALE) / SCALE);

            newCube.addManhattanDistance(manhattanDistance);
            newCube.addEuclideanDistancesSum(euclideanDistanceHash);

            cube.addManhattanDistance(manhattanDistance);
            cube.addEuclideanDistancesSum(euclideanDistanceHash);
        }

        this.cubes.add(newCube);
    }

    public Set<Point> getValidNewCubePoints() {
        Set<Point> validPlacements = new HashSet<>();

        cubes.forEach(cube -> {
            validPlacements.add(new Point(cube.getPoint().x() - 1, cube.getPoint().y(), cube.getPoint().z()));
            validPlacements.add(new Point(cube.getPoint().x() + 1, cube.getPoint().y(), cube.getPoint().z()));

            validPlacements.add(new Point(cube.getPoint().x(), cube.getPoint().y() - 1, cube.getPoint().z()));
            validPlacements.add(new Point(cube.getPoint().x(), cube.getPoint().y() + 1, cube.getPoint().z()));

            validPlacements.add(new Point(cube.getPoint().x(), cube.getPoint().y(), cube.getPoint().z() - 1));
            validPlacements.add(new Point(cube.getPoint().x(), cube.getPoint().y(), cube.getPoint().z() + 1));
        });

        cubes.forEach(cube -> validPlacements.remove(cube.getPoint()));

        return validPlacements;
    }

    public int getVolume() {
        return this.cubes.size();
    }

    public int centerMassToCenterGridDistanceHash() {
        int xSum = 0;
        int ySum = 0;
        int zSum = 0;

        int maxX = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;

        int maxY = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;

        int maxZ = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;

        for (Cube cube : cubes) {
            Point point = cube.getPoint();
            if (minX > point.x()) minX = point.x();
            if (maxX < point.x()) maxX = point.x();

            if (minY > point.y()) minY = point.y();
            if (maxY < point.y()) maxY = point.y();

            if (minZ > point.z()) minZ = point.z();
            if (maxZ < point.z()) maxZ = point.z();

            xSum += cube.getPoint().x();
            ySum += cube.getPoint().y();
            zSum += cube.getPoint().z();
        }

        xSum = xSum - (cubes.size() * minX);
        ySum = ySum - (cubes.size() * minY);
        zSum = zSum - (cubes.size() * minZ);

        long centerMassX = Math.round(((double) xSum / cubes.size()) * SCALE);
        long centerMassY = Math.round(((double) ySum / cubes.size()) * SCALE);
        long centerMassZ = Math.round(((double) zSum / cubes.size()) * SCALE);

        long centerGridX = Math.round(((double) (maxX - minX) / 2) * SCALE);
        long centerGridY = Math.round(((double) (maxY - minY) / 2) * SCALE);
        long centerGridZ = Math.round(((double) (maxZ - minZ) / 2) * SCALE);

        long dx = Math.abs(centerMassX - centerGridX);
        long dy = Math.abs(centerMassY - centerGridY);
        long dz = Math.abs(centerMassZ - centerGridZ);

        long manhattanDistance = dx + dy + dz;
        return Long.hashCode(Math.round(manhattanDistance * SCALE));
    }

    @Override
    public int hashCode() {
        return cubes.stream().mapToInt(Cube::hashCode).sum();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Polycube other)) {
            return false;
        }

        if (this.centerMassToCenterGridDistanceHash() != other.centerMassToCenterGridDistanceHash()) {
            return false;
        }

        boolean[][][] thisGrid = this.toGrid();
        boolean[][][] otherGrid = other.toGrid();

        if (!this.getCloseBenPairs(thisGrid).equals(other.getCloseBenPairs(otherGrid))) {
            return false;
        }

        if (!this.getFarBenPairs(thisGrid).equals(other.getFarBenPairs(otherGrid))) {
            return false;
        }

        if (Options.ROTATIONS_ON) {
            boolean areEqual = RotationComparator.trueEquals(thisGrid, otherGrid);
            if (Options.DEBUG_ON && !areEqual) {
                System.out.println("------------------------");
                System.out.println(this);
                System.out.println(other);
                System.out.println("------------------------");
            }
            return areEqual;
        }

        return true;
    }

    public boolean[][][] toGrid() {
        int maxX = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;

        int maxY = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;

        int maxZ = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;

        for (Cube cube : cubes) {
            Point point = cube.getPoint();
            if (minX > point.x()) minX = point.x();
            if (maxX < point.x()) maxX = point.x();

            if (minY > point.y()) minY = point.y();
            if (maxY < point.y()) maxY = point.y();

            if (minZ > point.z()) minZ = point.z();
            if (maxZ < point.z()) maxZ = point.z();
        }

        int dx = maxX - minX;
        int dy = maxY - minY;
        int dz = maxZ - minZ;


        boolean[][][] grid = new boolean[dx + 1][dy + 1][dz + 1];

        for (Cube cube : cubes) {
            int x = cube.getPoint().x() - minX;
            int y = cube.getPoint().y() - minY;
            int z = cube.getPoint().z() - minZ;
            grid[x][y][z] = true;
        }

        return grid;
    }

    @Override
    public String toString() {
        boolean[][][] grid = this.toGrid();

        StringBuilder sb = new StringBuilder();

        sb.append("\n~~~~Start PolyCube~~~\n\n");

        sb.append("Volume: ").append(this.cubes.size());
        sb.append("\nFar Bens Pairs: ").append(this.getFarBenPairs(grid));
        sb.append("\nClose Bens Pairs: ").append(this.getCloseBenPairs(grid));
        sb.append("\nHashcode: ").append(this.hashCode());
        sb.append("\nCenter Mass hash: ").append(this.centerMassToCenterGridDistanceHash());
        sb.append("\n");

        int count = 1;
        for (Cube cube : cubes) {
            sb.append("\nCube ").append(count).append("/").append(this.cubes.size()).append(": ");
            sb.append("\n").append(cube.toString()).append("\n");
            count++;
        }

        sb.append("\n");

        for (int z = 0; z < grid[0][0].length; z++) {
            sb.append("Layer ").append(z).append(":\n");

            for (boolean[][] cubes : grid) {
                for (int x = 0; x < grid[0].length; x++) {
                    if (cubes[x][z]) {
                        sb.append("[").append("#").append("]");
                    } else {
                        sb.append("[ ]");
                    }
                }
                sb.append("\n");
            }
            sb.append("\n");
        }

        sb.append("~~~~End PolyCube~~~");

        return sb.toString();
    }

    //Called Bens numbers because I could not find a good name for this metric.
    //this finds how many visible faces there are when viewed from each of the 6 directions
    //BUT - it only considers faces that are on the layer that is furthest from the viewing side that that contains a visible face.
    //Hard to explain but spin a real polycube around in your hand and look at it from any 90 degree angle. Bens number is the count of visible faces from the layer furthest from your face that still have a visible face.
    private Set<BenPair> getFarBenPairs(final boolean[][][] grid) {
        Set<BenPair> benPairs = new HashSet<>();

        BenPair xBenPair = new BenPair(getFarNumberFromLowX(grid), getFarNumberFromHighX(grid));
        BenPair yBenPair = new BenPair(getFarNumberFromLowY(grid), getFarNumberFromHighY(grid));
        BenPair zBenPair = new BenPair(getFarNumberFromLowZ(grid), getFarNumberFromHighZ(grid));

        benPairs.add(xBenPair);
        benPairs.add(yBenPair);
        benPairs.add(zBenPair);

        return benPairs;
    }

    public Set<BenPair> getCloseBenPairs(final boolean[][][] grid) {
        Set<BenPair> benPairs = new HashSet<>();

        BenPair xBenPair = new BenPair(getCloseNumberFromLowX(grid), getCloseNumberFromHighX(grid));
        BenPair yBenPair = new BenPair(getCloseNumberFromLowY(grid), getCloseNumberFromHighY(grid));
        BenPair zBenPair = new BenPair(getCloseNumberFromLowZ(grid), getCloseNumberFromHighZ(grid));

        benPairs.add(xBenPair);
        benPairs.add(yBenPair);
        benPairs.add(zBenPair);

        return benPairs;
    }

    private int getCloseNumberFromHighX(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int x = xLen - 1; x >= 0; x--) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int y = 0; y < yLen; y++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z]) {
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

    private int getCloseNumberFromLowX(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int x = 0; x < xLen; x++) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int y = 0; y < yLen; y++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z]) {
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

    private int getFarNumberFromLowX(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int x = 0; x < xLen; x++) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int y = 0; y < yLen; y++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z] && hasClearPathToBoundary(x, y, z, "low x", grid)) {
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

    private int getFarNumberFromHighX(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int x = xLen - 1; x >= 0; x--) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int y = 0; y < yLen; y++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z] && hasClearPathToBoundary(x, y, z, "high x", grid)) {
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

    private int getCloseNumberFromHighY(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int y = yLen - 1; y >= 0; y--) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z]) {
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

    private int getCloseNumberFromLowY(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int y = 0; y < yLen; y++) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z]) {
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

    private int getFarNumberFromLowY(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int y = 0; y < yLen; y++) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z] && hasClearPathToBoundary(x, y, z, "low y", grid)) {
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

    private int getFarNumberFromHighY(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int y = yLen - 1; y >= 0; y--) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int z = 0; z < zLen; z++) {
                    if (grid[x][y][z] && hasClearPathToBoundary(x, y, z, "high y", grid)) {
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

    private int getCloseNumberFromHighZ(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int z = zLen - 1; z >= 0; z--) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int y = 0; y < yLen; y++) {
                    if (grid[x][y][z]) {
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

    private int getCloseNumberFromLowZ(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int z = 0; z < zLen; z++) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int y = 0; y < yLen; y++) {
                    if (grid[x][y][z]) {
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

    private int getFarNumberFromLowZ(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int z = 0; z < zLen; z++) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int y = 0; y < yLen; y++) {
                    if (grid[x][y][z] && hasClearPathToBoundary(x, y, z, "low z", grid)) {
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

    private int getFarNumberFromHighZ(final boolean[][][] grid) {
        int xLen = grid.length;
        int yLen = grid[0].length;
        int zLen = grid[0][0].length;

        for (int z = zLen - 1; z >= 0; z--) {
            int cubesInCurrentLayer = 0;
            boolean foundTargetLayer = false;

            for (int x = 0; x < xLen; x++) {
                for (int y = 0; y < yLen; y++) {
                    if (grid[x][y][z] && hasClearPathToBoundary(x, y, z, "high z", grid)) {
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

    private boolean hasClearPathToBoundary(int x, int y, int z, String direction, final boolean[][][] grid) {
        switch (direction) {
            case "low x":
                for (int i = x + 1; i < grid.length; i++) {
                    if (grid[i][y][z]) return false;
                }
                break;
            case "high x":
                for (int i = 0; i < x; i++) {
                    if (grid[i][y][z]) return false;
                }
                break;
            case "low y":
                for (int j = y + 1; j < grid[0].length; j++) {
                    if (grid[x][j][z]) return false;
                }
                break;
            case "high y":
                for (int j = 0; j < y; j++) {
                    if (grid[x][j][z]) return false;
                }
                break;
            case "low z":
                for (int k = z + 1; k < grid[0][0].length; k++) {
                    if (grid[x][y][k]) return false;
                }
                break;
            case "high z":
                for (int k = 0; k < z; k++) {
                    if (grid[x][y][k]) return false;
                }
                break;
        }
        return true;
    }

}
