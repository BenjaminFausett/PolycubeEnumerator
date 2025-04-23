package model;

import config.Config;
import model.records.Cube;
import model.util.CubeFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Polycube {

    private final ArrayList<Cube> cubes;

    /**
     * Creates the one and only perfect MonoCube
     */
    public Polycube() {
        this.cubes = new ArrayList<>();
        this.cubes.add(CubeFactory.get(0, 0, 0));
    }

    /**
     * Creates a deep copy of the passed polycube
     */
    private Polycube(Polycube polycube) {
        this.cubes = new ArrayList<>(polycube.cubes);
    }

    /**
     * Creates a deep copy of the passed polycube and then adds a new cube at the given point
     */
    public Polycube(Polycube polycube, Cube newCubeCube) {
        this(polycube);
        this.addCube(newCubeCube);
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
     * Returns a set of all cubes which are allowed to be added to this polycube
     */
    public Set<Cube> getValidCubesToAdd() {
        Set<Cube> validNewCubes = new HashSet<>();

        cubes.forEach(cube -> {
            validNewCubes.add(CubeFactory.get((cube.x() - 1), cube.y(), cube.z()));
            validNewCubes.add(CubeFactory.get((cube.x() + 1), cube.y(), cube.z()));

            validNewCubes.add(CubeFactory.get(cube.x(), (cube.y() - 1), cube.z()));
            validNewCubes.add(CubeFactory.get(cube.x(), (cube.y() + 1), cube.z()));

            validNewCubes.add(CubeFactory.get(cube.x(), cube.y(), (cube.z() - 1)));
            validNewCubes.add(CubeFactory.get(cube.x(), cube.y(), (cube.z() + 1)));
        });

        validNewCubes.removeIf(cubes::contains);
        return validNewCubes;
    }

    private void addCube(Cube cube) {
        this.cubes.add(cube);
    }

    public List<Cube> getCubes() {
        return this.cubes;
    }

    public long longHashCode() {
        long hashCodeSum = 0;

        for (Cube cube1 : cubes) {
            int manhattanDistanceSum = 0;
            long euclideanDistanceHashSum = 0;

            for (Cube cube2 : cubes) {
                int dx = Math.abs(cube1.x() - cube2.x());
                int dy = Math.abs(cube1.y() - cube2.y());
                int dz = Math.abs(cube1.z() - cube2.z());

                int manhattanDistance = (dx + dy + dz);
                double euclideanDistance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
                int euclideanDistanceHash = Double.hashCode(Math.round(euclideanDistance * Config.DECIMAL_SCALING) / Config.DECIMAL_SCALING);

                manhattanDistanceSum += manhattanDistance;
                euclideanDistanceHashSum += euclideanDistanceHash;
            }
            hashCodeSum += (String.valueOf(manhattanDistanceSum) + euclideanDistanceHashSum).hashCode();
        }
        return hashCodeSum;
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

    private boolean[][][] toGrid() {//for debugging
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

        for (Cube cube : cubes) {
            int x = (cube.x() - minX);
            int y = (cube.y() - minY);
            int z = (cube.z() - minZ);
            grid[x][y][z] = true;
        }

        return grid;
    }
}