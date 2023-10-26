package model;

import config.Config;
import model.records.Point;
import model.util.RotationComparator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.shape.Reshape;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Polycube {

    private final ArrayList<Cube> cubes;

    /**
     * Creates the one and only perfect MonoCube
     */
    public Polycube() {
        this.cubes = new ArrayList<>();
        this.cubes.add(new Cube());
    }

    /**
     * Creates a deep copy of the passed polycube
     */
    private Polycube(Polycube polycube) {
        this.cubes = new ArrayList<>();
        for (Cube cube : polycube.cubes) {
            this.cubes.add(cube.clone());
        }
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

        cubes.forEach(cube -> {
            validPlacements.add(new Point((cube.x() - 1), cube.y(), cube.z()));
            validPlacements.add(new Point((cube.x() + 1), cube.y(), cube.z()));

            validPlacements.add(new Point(cube.x(), (cube.y() - 1), cube.z()));
            validPlacements.add(new Point(cube.x(), (cube.y() + 1), cube.z()));

            validPlacements.add(new Point(cube.x(), cube.y(), (cube.z() - 1)));
            validPlacements.add(new Point(cube.x(), cube.y(), (cube.z() + 1)));
        });

        cubes.forEach(cube -> validPlacements.remove(new Point(cube.x(), cube.y(), cube.z())));

        return validPlacements;
    }

    /**
     * Adds a cube to this polycube at the given point updates all cubes with the new manhattan and euclidean distances to each other cube
     */
    private void addCube(Point point) {
        Cube newCube = new Cube(point);

        for (Cube cube : cubes) {
            int dx = Math.abs(cube.x() - newCube.x());
            int dy = Math.abs(cube.y() - newCube.y());
            int dz = Math.abs(cube.z() - newCube.z());

            int manhattanDistance = (dx + dy + dz);
            double euclideanDistance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
            int euclideanDistanceHash = Double.hashCode(Math.round(euclideanDistance * Config.DECIMAL_SCALING) / Config.DECIMAL_SCALING);

            newCube.addManhattanDistance(manhattanDistance);
            newCube.addEuclideanDistancesSum(euclideanDistanceHash);

            cube.addManhattanDistance(manhattanDistance);
            cube.addEuclideanDistancesSum(euclideanDistanceHash);
        }

        this.cubes.add(newCube);
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

        for (Cube cube : cubes) {
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
        //INDArray array = Nd4j.zeros(dx + 1,dy + 1, dz + 1);

        for (Cube cube : cubes) {
            int x = (cube.x() - minX);
            int y = (cube.y() - minY);
            int z = (cube.z() - minZ);
            grid[x][y][z] = true;

            //array.putScalar(new int[]{x, y, z}, 1);
        }

        //Nd4j.getExecutioner().calculateOutputShape(Reshape.builder("rotate"))


        return grid;
    }

    /**
     * Returns the sum of all hash codes produced by the individual cubes that make up this polycube
     */
    @Override
    public int hashCode() {
        return cubes.stream().mapToInt(Cube::hashCode).sum();
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

        Set<Integer> thisHashes = cubes.stream().map(Cube::hashCode).collect(Collectors.toSet());
        Set<Integer> otherHashes = other.cubes.stream().map(Cube::hashCode).collect(Collectors.toSet());

        if (!thisHashes.equals(otherHashes)) {
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
}