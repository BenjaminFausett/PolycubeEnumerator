package model;

import java.util.*;
import java.util.stream.Collectors;

public class Polycube {

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
    public Polycube() {
        this.cubes = new ArrayList<>();
        this.cubes.add(new LinkedCube(100, 100, 100));
        this.volume = 1;
    }

    private Polycube(Polycube polycube) {
        this.volume = polycube.volume;
        this.cubes = polycube.cubes.get(0).cloneStructure();

    }

    public Polycube(Polycube polycube, Point newCubePoint) {
        this(polycube);
        this.addCube(newCubePoint);
    }

    public Polycube clone() {
        return new Polycube(this);
    }

    public void addCube(Point point) {
        this.volume += 1;

        LinkedCube newCube = new LinkedCube(point);

        for (LinkedCube cube : cubes) {
            int dx = Math.abs(cube.getPoint().x() - newCube.getPoint().x());
            int dy = Math.abs(cube.getPoint().y() - newCube.getPoint().y());
            int dz = Math.abs(cube.getPoint().z() - newCube.getPoint().z());

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

            if(cube.getNegativeZ() == null) validPlacements.add(new Point(cube.getPoint().x(), cube.getPoint().y(), cube.getPoint().z() - 1));
            if(cube.getPositiveZ() == null) validPlacements.add(new Point(cube.getPoint().x(), cube.getPoint().y(), cube.getPoint().z() + 1));
        });

        return validPlacements;
    }

    public int getVolume() {
        return this.volume;
    }

    private int getMaxX() {
        return this.cubes.stream().mapToInt(cube -> cube.getPoint().x()).max().orElse(0);
    }

    private int getMinX() {
        return this.cubes.stream().mapToInt(cube -> cube.getPoint().x()).min().orElse(0);
    }

    private int getMaxY() {
        return this.cubes.stream().mapToInt(cube -> cube.getPoint().y()).max().orElse(0);
    }

    private int getMinY() {
        return this.cubes.stream().mapToInt(cube -> cube.getPoint().y()).min().orElse(0);
    }

    private int getMaxZ() {
        return this.cubes.stream().mapToInt(cube -> cube.getPoint().z()).max().orElse(0);
    }

    private int getMinZ() {
        return this.cubes.stream().mapToInt(cube -> cube.getPoint().z()).min().orElse(0);
    }



    @Override
    public int hashCode() {
        return cubes.stream().mapToInt(LinkedCube::hashCode).sum();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Polycube other)) {
            return false;
        }

        Set<Integer> points = cubes.stream().map(LinkedCube::hashCode).collect(Collectors.toSet());
        Set<Integer> otherPoints = other.cubes.stream().map(LinkedCube::hashCode).collect(Collectors.toSet());

        return points.equals(otherPoints);
    }

    @Override
    public String toString() {
        int maxX = this.getMaxX();
        int minX = this.getMinX();
        int dx = maxX - minX;

        int maxY = this.getMaxY();
        int minY = this.getMinY();
        int dy = maxY - minY;

        int maxZ = this.getMaxZ();
        int minZ = this.getMinZ();
        int dz = maxZ - minZ;


        boolean[][][] grid = new boolean[dx+1][dy+1][dz+1];

        for(LinkedCube cube: cubes) {
            int x = cube.getPoint().x() - minX;
            int y = cube.getPoint().y() - minY;
            int z = cube.getPoint().z() - minZ;
            grid[x][y][z] = true;
        }


        StringBuilder sb = new StringBuilder();

        sb.append("\n~~~~Start PolyCube~~~\n\n");

        sb.append("Volume: ").append(volume);
        sb.append("\nHashcode: ").append(this.hashCode());
        sb.append("\n");

        int count = 1;
        for(LinkedCube cube: cubes) {
            sb.append("\nCube ").append(count).append("/").append(volume).append(": ");
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
