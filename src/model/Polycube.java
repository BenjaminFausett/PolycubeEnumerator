package model;

import config.Config;
import model.records.Point;
import model.util.RotationComparator;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Polycube implements Serializable {

    private final List<Cube> cubes;

    /**
     * Creates the one and only perfect MonoCube
     */
    public Polycube() {
        this.cubes = new ArrayList<>();
        this.cubes.add(new Cube((byte) 64, (byte) 64, (byte) 64));
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
            int dx = Math.abs(cube.x() - newCube.x());
            int dy = Math.abs(cube.y() - newCube.y());
            int dz = Math.abs(cube.z() - newCube.z());

            int manhattanDistance = dx + dy + dz;
            double euclideanDistance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
            int euclideanDistanceHash = Double.hashCode(Math.round(euclideanDistance * Config.DECIMAL_SCALING) / Config.DECIMAL_SCALING);

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
            validPlacements.add(new Point((byte) (cube.x() - 1), cube.y(), cube.z()));
            validPlacements.add(new Point((byte) (cube.x() + 1), cube.y(), cube.z()));

            validPlacements.add(new Point(cube.x(), (byte) (cube.y() - 1), cube.z()));
            validPlacements.add(new Point(cube.x(), (byte) (cube.y() + 1), cube.z()));

            validPlacements.add(new Point(cube.x(), cube.y(), (byte) (cube.z() - 1)));
            validPlacements.add(new Point(cube.x(), cube.y(), (byte) (cube.z() + 1)));
        });

        cubes.forEach(cube -> validPlacements.remove(new Point(cube.x(), cube.y(), cube.z())));

        return validPlacements;
    }

    public int getVolume() {
        return this.cubes.size();
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

        Set<Integer> thisHashes = cubes.stream().map(Cube::hashCode).collect(Collectors.toSet());
        Set<Integer> otherHashes = other.cubes.stream().map(Cube::hashCode).collect(Collectors.toSet());

        if(!thisHashes.equals(otherHashes)) {
            return false;
        }

        boolean[][][] thisGrid = this.toGrid();
        boolean[][][] otherGrid = other.toGrid();

        boolean areEqual = RotationComparator.trueEquals(thisGrid, otherGrid);
        if (Config.DEBUG_ON && !areEqual) {
            System.out.println("------------------------");
            System.out.println(this);
            System.out.println(other);
            System.out.println("------------------------");
        }

        return areEqual;
    }

    public boolean[][][] toGrid() {
        byte maxX = Byte.MIN_VALUE;
        byte minX = Byte.MAX_VALUE;

        byte maxY = Byte.MIN_VALUE;
        byte minY = Byte.MAX_VALUE;

        byte maxZ = Byte.MIN_VALUE;
        byte minZ = Byte.MAX_VALUE;

        for (Cube cube : cubes) {
            if (minX > cube.x()) minX = cube.x();
            if (maxX < cube.x()) maxX = cube.x();

            if (minY > cube.y()) minY = cube.y();
            if (maxY < cube.y()) maxY = cube.y();

            if (minZ > cube.z()) minZ = cube.z();
            if (maxZ < cube.z()) maxZ = cube.z();
        }

        byte dx = (byte) (maxX - minX);
        byte dy = (byte) (maxY - minY);
        byte dz = (byte) (maxZ - minZ);


        boolean[][][] grid = new boolean[dx + 1][dy + 1][dz + 1];

        for (Cube cube : cubes) {
            byte x = (byte) (cube.x() - minX);
            byte y = (byte) (cube.y() - minY);
            byte z = (byte) (cube.z() - minZ);
            grid[x][y][z] = true;
        }

        return grid;
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