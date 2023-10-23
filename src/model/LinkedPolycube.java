package model;

import java.util.*;
import java.util.stream.Collectors;

public class LinkedPolycube {

    private static final boolean DEBUG_MODE = false;
    private static final boolean PERFECT_MODE = false;
    private static final int DECIMAL_ACCURACY = 12;
    private static final double SCALE = Math.pow(10, DECIMAL_ACCURACY);
    private static final int[][] DIRECTIONS = {
            {0, 1, 0},  // up
            {0, -1, 0}, // down
            {-1, 0, 0}, // left
            {1, 0, 0},  // right
            {0, 0, 1},  // forward
            {0, 0, -1}  // backward
    };

    private int volume;
    private List<LinkedCube> cubes;


    //Creates the one and only perfect MonoCube
    public LinkedPolycube() {
        this.cubes = new ArrayList<>();
        this.cubes.add(new LinkedCube(100, 100, 100));
        this.volume = 1;
    }

    private LinkedPolycube(LinkedPolycube polycube) {
        this.volume = polycube.volume;
        this.cubes = polycube.cubes.get(0).cloneStructure();

    }

    public LinkedPolycube(LinkedPolycube polycube, Point newCubePoint) {
        this(polycube);
        this.addCube(newCubePoint);
    }

    public LinkedPolycube clone() {
        return new LinkedPolycube(this);
    }

    public void addCube(Point point) {
        this.volume += 1;

        LinkedCube newCube = new LinkedCube(point);

        for (LinkedCube cube : cubes) {
            int dx = Math.abs(cube.getPoint().x() - point.x());
            int dy = Math.abs(cube.getPoint().x() - point.x());
            int dz = Math.abs(cube.getPoint().x() - point.x());

            if (dx == 1 && dy == 0 && dz == 0) {
                if (newCube.getPoint().x() > cube.getPoint().x()) {
                    newCube.setNegativeX(cube);
                    cube.setPositiveX(newCube);
                } else {
                    cube.setNegativeX(newCube);
                    newCube.setPositiveX(cube);
                }
            }
            if (dx == 0 && dy == 1 && dz == 0) {
                if (newCube.getPoint().y() > cube.getPoint().y()) {
                    newCube.setNegativeY(cube);
                    cube.setPositiveY(newCube);
                } else {
                    cube.setNegativeY(newCube);
                    newCube.setPositiveY(cube);
                }
            }
            if (dx == 0 && dy == 0 && dz == 1) {
                if (newCube.getPoint().z() > cube.getPoint().z()) {
                    newCube.setNegativeZ(cube);
                    cube.setPositiveZ(newCube);
                } else {
                    cube.setNegativeZ(newCube);
                    newCube.setPositiveZ(cube);
                }
            }


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


    private List<LinkedCube> getNeighbors(Point point) {
        List<LinkedCube> neighbors = new ArrayList<>();
        for (LinkedCube cube : cubes) {
            int dx = Math.abs(cube.getPoint().x() - point.x());
            int dy = Math.abs(cube.getPoint().x() - point.x());
            int dz = Math.abs(cube.getPoint().x() - point.x());

            if (dx == 1 && dy == 1 && dz == 1) {
                neighbors.add(cube);
            }
        }
        return neighbors;
    }

    public Set<Point> getValidNewCubePoint() {
        Set<Point> validPlacements = new HashSet<>();

        cubes.forEach(cube -> {
            if(cube.getNegativeX() == null) validPlacements.add(new Point(cube.getPoint().x() - 1, cube.getPoint().y(), cube.getPoint().z()));
            if(cube.getPositiveX() == null) validPlacements.add(new Point(cube.getPoint().x() + 1, cube.getPoint().y(), cube.getPoint().z()));

            if(cube.getNegativeY() == null) validPlacements.add(new Point(cube.getPoint().x(), cube.getPoint().y() - 1, cube.getPoint().z()));
            if(cube.getPositiveY() == null) validPlacements.add(new Point(cube.getPoint().x(), cube.getPoint().y() + 1, cube.getPoint().z()));

            if(cube.getNegativeZ() == null) validPlacements.add(new Point(cube.getPoint().x() - 1, cube.getPoint().y(), cube.getPoint().z() - 1));
            if(cube.getPositiveZ() == null) validPlacements.add(new Point(cube.getPoint().x() + 1, cube.getPoint().y(), cube.getPoint().z() + 1));
        });

        return validPlacements;
    }

    @Override
    public int hashCode() {
        return cubes.stream().mapToInt(LinkedCube::hashCode).sum();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LinkedPolycube other)) {
            return false;
        }

        Set<Integer> points = cubes.stream().map(LinkedCube::hashCode).collect(Collectors.toSet());
        Set<Integer> otherPoints = other.cubes.stream().map(LinkedCube::hashCode).collect(Collectors.toSet());

        return points.equals(otherPoints);
    }

}
