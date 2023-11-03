package model;

import model.records.Point;
import model.util.PointFactory;
import model.util.RotationComparator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Polycube {

    private final ArrayList<Point> cubes;

    /**
     * Creates the one and only perfect MonoCube
     */
    public Polycube() {
        PointFactory pointFactory = PointFactory.getInstance();
        this.cubes = new ArrayList<>();
        this.cubes.add(pointFactory.get(64, 64, 64));
    }

    /**
     * Creates a deep copy of the passed polycube
     */
    private Polycube(Polycube polycube) {
        this.cubes = new ArrayList<>();
        this.cubes.addAll(polycube.cubes);
    }

    /**
     * Creates a deep copy of the passed polycube and then adds a new cube at the given point
     */
    public Polycube(Polycube polycube, Point newCubePoint) {
        this(polycube);
        this.addCube(newCubePoint);
    }

    /**
     * Returns a deep copy of this polycube
     */
    public Polycube clone() {
        return new Polycube(this);
    }

    /**
     * Returns the number of cubes making up this polycube
     */
    public int getVolume() {
        return this.cubes.size();
    }

    /**
     * Returns a set of unique points, where each point is a valid location where a new cube could be added to this polycube
     */
    public Set<Point> getValidNewCubePoints() {
        Set<Point> validPlacements = new HashSet<>();
        PointFactory pointFactory = PointFactory.getInstance();

        cubes.forEach(cube -> {
            validPlacements.add(pointFactory.get((cube.x() - 1), cube.y(), cube.z()));
            validPlacements.add(pointFactory.get((cube.x() + 1), cube.y(), cube.z()));

            validPlacements.add(pointFactory.get(cube.x(), (cube.y() - 1), cube.z()));
            validPlacements.add(pointFactory.get(cube.x(), (cube.y() + 1), cube.z()));

            validPlacements.add(pointFactory.get(cube.x(), cube.y(), (cube.z() - 1)));
            validPlacements.add(pointFactory.get(cube.x(), cube.y(), (cube.z() + 1)));
        });

        cubes.forEach(validPlacements::remove);

        return validPlacements;
    }

    /**
     * Adds a cube to this polycube at the given point
     */
    private void addCube(Point point) {
        this.cubes.add(point);
    }

    /**
     * Transforms the list of cubes into a 3d array of booleans which can be more easily used to compute rotations
     */
    private boolean[][][] toGrid() {
        int maxX = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;

        int maxY = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;

        int maxZ = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;

        for (Point cube : cubes) {
            if (minX > cube.x()) minX = cube.x();
            if (maxX < cube.x()) maxX = cube.x();

            if (minY > cube.y()) minY = cube.y();
            if (maxY < cube.y()) maxY = cube.y();

            if (minZ > cube.z()) minZ = cube.z();
            if (maxZ < cube.z()) maxZ = cube.z();
        }

        int dx = (maxX - minX);
        int dy = (maxY - minY);
        int dz = (maxZ - minZ);

        boolean[][][] grid = new boolean[dx + 1][dy + 1][dz + 1];

        for (Point cube : cubes) {
            int x = (cube.x() - minX);
            int y = (cube.y() - minY);
            int z = (cube.z() - minZ);
            grid[x][y][z] = true;
        }

        return grid;
    }

    /**
     * Returns the sum of all hash codes produced by the individual cubes that make up this polycube
     */
    @Override
    public int hashCode() {


        return 0;
    }

    /**
     * Checks for true equality by first comparing if both polycubes have the same set of cube hashes. If the hashes don't match then returns false.
     * If the hashes do match then we begrudgingly begin calculating and comparing all possible rotations to check if the polycubes are truly equal or not.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Polycube other)) {
            return false;
        }

        return RotationComparator.trueEquals(this.toGrid(), other.toGrid());
    }

    @Override
    public String toString() {//for debugging
        boolean[][][] grid = this.toGrid();

        StringBuilder sb = new StringBuilder();

        sb.append("\n~~~~Start PolyCube~~~\n\n");

        sb.append("Volume: ").append(this.cubes.size());
        sb.append("\nHashcode: ").append(this.hashCode());
        sb.append("\n");

        int count = 1;
        for (Point cube : cubes) {
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
}