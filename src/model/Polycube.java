package model;

import com.github.quickhull3d.Point3d;
import com.github.quickhull3d.QuickHull3D;
import config.Config;
import model.records.Cube;
import model.records.Point3D;
import model.records.VisibleFaces;
import model.records.VisibleFaces2;

import java.util.*;

import static config.Config.DECIMAL_SCALING;

public class Polycube {

    private final ArrayList<Cube> cubes;

    /**
     * Creates the one and only perfect MonoCube
     */
    public Polycube() {
        this.cubes = new ArrayList<>();
        this.cubes.add(new Cube(0, 0, 0));
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
    public Polycube(Polycube polycube, Point3D newCubeLocation) {
        this(polycube);
        this.addCube(newCubeLocation);
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
    public Set<Point3D> getValidCubesToAdd() {
        Set<Point3D> validNewCubes = new HashSet<>();

        cubes.forEach(cube -> {
            validNewCubes.add(new Point3D((cube.x() - 1), cube.y(), cube.z()));
            validNewCubes.add(new Point3D((cube.x() + 1), cube.y(), cube.z()));

            validNewCubes.add(new Point3D(cube.x(), (cube.y() - 1), cube.z()));
            validNewCubes.add(new Point3D(cube.x(), (cube.y() + 1), cube.z()));

            validNewCubes.add(new Point3D(cube.x(), cube.y(), (cube.z() - 1)));
            validNewCubes.add(new Point3D(cube.x(), cube.y(), (cube.z() + 1)));
        });

        cubes.forEach(cube -> validNewCubes.remove(cube.getPoint()));
        return validNewCubes;
    }

    public String getNeighborCounts() {
        Map<Cube, Integer> countMap = new HashMap<>();

        cubes.forEach(cube -> {
            countMap.merge(new Cube((cube.x() - 1), cube.y(), cube.z()), 1, Integer::sum);
            countMap.merge(new Cube((cube.x() + 1), cube.y(), cube.z()), 1, Integer::sum);

            countMap.merge(new Cube(cube.x(), (cube.y() - 1), cube.z()), 1, Integer::sum);
            countMap.merge(new Cube(cube.x(), (cube.y() + 1), cube.z()), 1, Integer::sum);

            countMap.merge(new Cube(cube.x(), cube.y(), (cube.z() - 1)), 1, Integer::sum);
            countMap.merge(new Cube(cube.x(), cube.y(), (cube.z() + 1)), 1, Integer::sum);
        });

        return countMap.values().stream().sorted().toList().toString();
    }

    private void addCube(Point3D point) {
        this.cubes.add(new Cube(point));
    }

    public List<Cube> getCubes() {
        return this.cubes;
    }

    public long longHashCode() {
        long hashCodeSum = 0;

        for (Cube cube1 : cubes) {
            int manhattanDistanceSum = 0;
            int euclideanDistanceHashSum = 0;

            for (Cube cube2 : cubes) {
                int dx = Math.abs(cube1.x() - cube2.x());
                int dy = Math.abs(cube1.y() - cube2.y());
                int dz = Math.abs(cube1.z() - cube2.z());

                int manhattanDistance = (dx + dy + dz);
                double euclideanDistance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
                int euclideanDistanceHash = Long.hashCode(Math.round(euclideanDistance * Config.DECIMAL_SCALING));

                manhattanDistanceSum += manhattanDistance;
                euclideanDistanceHashSum += euclideanDistanceHash;
            }
            hashCodeSum += (String.valueOf(manhattanDistanceSum) + euclideanDistanceHashSum).hashCode();
        }
        return hashCodeSum;
    }

    public String getBensNumbers() {//finds how many cubes are in each layer. order of cubes per axis is preserved.
        Map<Integer, Integer> xMap = new TreeMap<>();
        Map<Integer, Integer> yMap = new TreeMap<>();
        Map<Integer, Integer> zMap = new TreeMap<>();

        for (Cube cube : cubes) {
            xMap.merge(cube.x(), 1, Integer::sum);
            yMap.merge(cube.y(), 1, Integer::sum);
            zMap.merge(cube.z(), 1, Integer::sum);
        }

        ArrayList<Integer> xValues = new ArrayList<>(xMap.values());
        ArrayList<Integer> xValuesReverse = new ArrayList<>(xMap.values());
        Collections.reverse(xValuesReverse);
        ArrayList<Integer> yValues = new ArrayList<>(yMap.values());
        ArrayList<Integer> yValuesReverse = new ArrayList<>(yMap.values());
        Collections.reverse(yValuesReverse);
        ArrayList<Integer> zValues = new ArrayList<>(zMap.values());
        ArrayList<Integer> zValuesReverse = new ArrayList<>(zMap.values());
        Collections.reverse(zValuesReverse);

        ArrayList<String> strings = new ArrayList<>();

        if (xValues.hashCode() > xValuesReverse.hashCode()) {
            strings.add(xValues.toString());
        } else {
            strings.add(xValuesReverse.toString());
        }

        if (yValues.hashCode() > yValuesReverse.hashCode()) {
            strings.add(yValues.toString());
        } else {
            strings.add(yValuesReverse.toString());
        }

        if (zValues.hashCode() > zValuesReverse.hashCode()) {
            strings.add(zValues.toString());
        } else {
            strings.add(zValuesReverse.toString());
        }

        Collections.sort(strings);
        return strings.toString();
    }

    public String getViewableFacesPerLayer() { //if i could get the VisibleFaces object to include the largest number of how many of the cubes on the layer are connected to eachother, then i would get past the porygon cube.
        // i dont know how to do it but im sure it can be done, its probalby just doing to be slow as hell, but dont worry about speed right now, if i make the perfect string, then i can work on speed.
        TreeMap<Integer, Integer> visibleFaceMapPosX = new TreeMap<>();
        TreeMap<Integer, Integer> visibleFaceMapNegX = new TreeMap<>();
        TreeMap<Integer, Integer> visibleFaceMapPosY = new TreeMap<>();
        TreeMap<Integer, Integer> visibleFaceMapNegY = new TreeMap<>();
        TreeMap<Integer, Integer> visibleFaceMapPosZ = new TreeMap<>();
        TreeMap<Integer, Integer> visibleFaceMapNegZ = new TreeMap<>();

        for (Cube cube : cubes) {
            if (isNeighborEmpty(cube, 1, 0, 0)) {
                visibleFaceMapPosX.merge(cube.x(), 1, Integer::sum);
            }
            if (isNeighborEmpty(cube, -1, 0, 0)) {
                visibleFaceMapNegX.merge(cube.x(), 1, Integer::sum);
            }
            if (isNeighborEmpty(cube, 0, 1, 0)) {
                visibleFaceMapPosY.merge(cube.y(), 1, Integer::sum);
            }
            if (isNeighborEmpty(cube, 0, -1, 0)) {
                visibleFaceMapNegY.merge(cube.y(), 1, Integer::sum);
            }
            if (isNeighborEmpty(cube, 0, 0, 1)) {
                visibleFaceMapPosZ.merge(cube.z(), 1, Integer::sum);
            }
            if (isNeighborEmpty(cube, 0, 0, -1)) {
                visibleFaceMapNegZ.merge(cube.z(), 1, Integer::sum);
            }
        }

        ArrayList<VisibleFaces> visibleFacePosX = new ArrayList<>();
        ArrayList<VisibleFaces> visibleFaceNegX = new ArrayList<>();
        ArrayList<VisibleFaces> visibleFacePosY = new ArrayList<>();
        ArrayList<VisibleFaces> visibleFaceNegY = new ArrayList<>();
        ArrayList<VisibleFaces> visibleFacePosZ = new ArrayList<>();
        ArrayList<VisibleFaces> visibleFaceNegZ = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : visibleFaceMapPosX.entrySet()) {
            visibleFacePosX.add(new VisibleFaces(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<Integer, Integer> entry : visibleFaceMapNegX.entrySet()) {
            visibleFaceNegX.add(new VisibleFaces(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<Integer, Integer> entry : visibleFaceMapPosY.entrySet()) {
            visibleFacePosY.add(new VisibleFaces(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<Integer, Integer> entry : visibleFaceMapNegY.entrySet()) {
            visibleFaceNegY.add(new VisibleFaces(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<Integer, Integer> entry : visibleFaceMapPosZ.entrySet()) {
            visibleFacePosZ.add(new VisibleFaces(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<Integer, Integer> entry : visibleFaceMapNegZ.entrySet()) {
            visibleFaceNegZ.add(new VisibleFaces(entry.getKey(), entry.getValue()));
        }

        ArrayList<String> stringList = new ArrayList<>();
        ArrayList<String> stringListX = new ArrayList<>();
        ArrayList<String> stringListY = new ArrayList<>();
        ArrayList<String> stringListZ = new ArrayList<>();

        stringListX.add(getCanonicalVisibleFacesString(visibleFacePosX));
        stringListX.add(getCanonicalVisibleFacesString(visibleFaceNegX));
        stringListY.add(getCanonicalVisibleFacesString(visibleFacePosY));
        stringListY.add(getCanonicalVisibleFacesString(visibleFaceNegY));
        stringListZ.add(getCanonicalVisibleFacesString(visibleFacePosZ));
        stringListZ.add(getCanonicalVisibleFacesString(visibleFaceNegZ));

        Collections.sort(stringListX);
        Collections.sort(stringListY);
        Collections.sort(stringListZ);

        stringList.add(stringListX.toString());
        stringList.add(stringListY.toString());
        stringList.add(stringListZ.toString());

        Collections.sort(stringList);

        return stringList.toString();
    }

    public String getViewableFacesPerLayer2() {
        TreeMap<Integer, ViewableFacesLayer> visibleFaceMapPosX = new TreeMap<>();
        TreeMap<Integer, ViewableFacesLayer> visibleFaceMapNegX = new TreeMap<>();
        TreeMap<Integer, ViewableFacesLayer> visibleFaceMapPosY = new TreeMap<>();
        TreeMap<Integer, ViewableFacesLayer> visibleFaceMapNegY = new TreeMap<>();
        TreeMap<Integer, ViewableFacesLayer> visibleFaceMapPosZ = new TreeMap<>();
        TreeMap<Integer, ViewableFacesLayer> visibleFaceMapNegZ = new TreeMap<>();

        for (Cube cube : cubes) {
            if (isNeighborEmpty(cube, 1, 0, 0)) {
                if(visibleFaceMapPosX.containsKey(cube.x())) {
                    visibleFaceMapPosX.get(cube.x()).addVisibleFace(cube.y(), cube.z());
                } else {
                    ViewableFacesLayer layer = new ViewableFacesLayer();
                    layer.addVisibleFace(cube.y(), cube.z());
                    visibleFaceMapPosX.put(cube.x(), layer);
                }
            }
            if (isNeighborEmpty(cube, -1, 0, 0)) {
                if(visibleFaceMapNegX.containsKey(cube.x())) {
                    visibleFaceMapNegX.get(cube.x()).addVisibleFace(cube.y(), cube.z());
                } else {
                    ViewableFacesLayer layer = new ViewableFacesLayer();
                    layer.addVisibleFace(cube.y(), cube.z());
                    visibleFaceMapNegX.put(cube.x(), layer);
                }
            }

            if (isNeighborEmpty(cube, 0, 1, 0)) {
                if(visibleFaceMapPosY.containsKey(cube.y())) {
                    visibleFaceMapPosY.get(cube.y()).addVisibleFace(cube.x(), cube.z());
                } else {
                    ViewableFacesLayer layer = new ViewableFacesLayer();
                    layer.addVisibleFace(cube.x(), cube.z());
                    visibleFaceMapPosY.put(cube.y(), layer);
                }
            }
            if (isNeighborEmpty(cube, 0, -1, 0)) {
                if(visibleFaceMapNegY.containsKey(cube.y())) {
                    visibleFaceMapNegY.get(cube.y()).addVisibleFace(cube.x(), cube.z());
                } else {
                    ViewableFacesLayer layer = new ViewableFacesLayer();
                    layer.addVisibleFace(cube.x(), cube.z());
                    visibleFaceMapNegY.put(cube.y(), layer);
                }
            }

            if (isNeighborEmpty(cube, 0, 0, 1)) {
                if(visibleFaceMapPosZ.containsKey(cube.z())) {
                    visibleFaceMapPosZ.get(cube.z()).addVisibleFace(cube.x(), cube.y());
                } else {
                    ViewableFacesLayer layer = new ViewableFacesLayer();
                    layer.addVisibleFace(cube.x(), cube.y());
                    visibleFaceMapPosZ.put(cube.z(), layer);
                }
            }
            if (isNeighborEmpty(cube, 0, 0, -1)) {
                if(visibleFaceMapNegZ.containsKey(cube.z())) {
                    visibleFaceMapNegZ.get(cube.z()).addVisibleFace(cube.x(), cube.y());
                } else {
                    ViewableFacesLayer layer = new ViewableFacesLayer();
                    layer.addVisibleFace(cube.x(), cube.y());
                    visibleFaceMapNegZ.put(cube.z(), layer);
                }
            }
        }

        ArrayList<VisibleFaces2> visibleFacePosX = new ArrayList<>();
        ArrayList<VisibleFaces2> visibleFaceNegX = new ArrayList<>();
        ArrayList<VisibleFaces2> visibleFacePosY = new ArrayList<>();
        ArrayList<VisibleFaces2> visibleFaceNegY = new ArrayList<>();
        ArrayList<VisibleFaces2> visibleFacePosZ = new ArrayList<>();
        ArrayList<VisibleFaces2> visibleFaceNegZ = new ArrayList<>();

        for (Map.Entry<Integer, ViewableFacesLayer> entry : visibleFaceMapPosX.entrySet()) {
            visibleFacePosX.add(new VisibleFaces2(entry.getKey(), entry.getValue().getCount(), entry.getValue().getLargestConnectedGroupSize()));
        }
        for (Map.Entry<Integer, ViewableFacesLayer> entry : visibleFaceMapNegX.entrySet()) {
            visibleFaceNegX.add(new VisibleFaces2(entry.getKey(), entry.getValue().getCount(), entry.getValue().getLargestConnectedGroupSize()));
        }
        for (Map.Entry<Integer, ViewableFacesLayer> entry : visibleFaceMapPosY.entrySet()) {
            visibleFacePosY.add(new VisibleFaces2(entry.getKey(), entry.getValue().getCount(), entry.getValue().getLargestConnectedGroupSize()));
        }
        for (Map.Entry<Integer, ViewableFacesLayer> entry : visibleFaceMapNegY.entrySet()) {
            visibleFaceNegY.add(new VisibleFaces2(entry.getKey(), entry.getValue().getCount(), entry.getValue().getLargestConnectedGroupSize()));
        }
        for (Map.Entry<Integer, ViewableFacesLayer> entry : visibleFaceMapPosZ.entrySet()) {
            visibleFacePosZ.add(new VisibleFaces2(entry.getKey(), entry.getValue().getCount(), entry.getValue().getLargestConnectedGroupSize()));
        }
        for (Map.Entry<Integer, ViewableFacesLayer> entry : visibleFaceMapNegZ.entrySet()) {
            visibleFaceNegZ.add(new VisibleFaces2(entry.getKey(), entry.getValue().getCount(), entry.getValue().getLargestConnectedGroupSize()));
        }

        ArrayList<String> stringList = new ArrayList<>();
        ArrayList<String> stringListX = new ArrayList<>();
        ArrayList<String> stringListY = new ArrayList<>();
        ArrayList<String> stringListZ = new ArrayList<>();

        stringListX.add(getCanonicalVisibleFacesString2(visibleFacePosX));
        stringListX.add(getCanonicalVisibleFacesString2(visibleFaceNegX));
        stringListY.add(getCanonicalVisibleFacesString2(visibleFacePosY));
        stringListY.add(getCanonicalVisibleFacesString2(visibleFaceNegY));
        stringListZ.add(getCanonicalVisibleFacesString2(visibleFacePosZ));
        stringListZ.add(getCanonicalVisibleFacesString2(visibleFaceNegZ));

        Collections.sort(stringListX);
        Collections.sort(stringListY);
        Collections.sort(stringListZ);

        stringList.add(stringListX.toString());
        stringList.add(stringListY.toString());
        stringList.add(stringListZ.toString());

        Collections.sort(stringList);

        return stringList.toString();
    }

    private String getCanonicalVisibleFacesString(List<VisibleFaces> list) {
        for (int i = 0; i < list.size(); i++) {
            VisibleFaces original = list.get(i);
            VisibleFaces reversedElem = list.get(list.size() - i - 1);

            int compare = original.compareTo(reversedElem);
            if (compare < 0) {
                return list.toString();
            } else if (compare > 0) {
                Collections.reverse(list);
                return list.toString();
            }
        }
        return list.toString();
    }

    private String getCanonicalVisibleFacesString2(List<VisibleFaces2> list) {
        for (int i = 0; i < list.size(); i++) {
            VisibleFaces2 original = list.get(i);
            VisibleFaces2 reversedElem = list.get(list.size() - i - 1);

            int compare = original.compareTo(reversedElem);
            if (compare < 0) {
                return list.toString();
            } else if (compare > 0) {
                Collections.reverse(list);
                return list.toString();
            }
        }
        return list.toString();
    }

    private boolean isNeighborEmpty(Cube cube, int dx, int dy, int dz) {
        return !cubes.contains(new Cube(cube.x() + dx, cube.y() + dy, cube.z() + dz));
    }

    private String getHullMetrics() {
        QuickHull3D quickHull = new QuickHull3D();
        Point3d[] points = new Point3d[cubes.size()];

        for(int i = 0; i < cubes.size(); i++) {
            Cube cube = cubes.get(i);
            points[i] = new Point3d(cube.x(), cube.y(), cube.z());
        }
        try {
            quickHull.build(points);

            return quickHull.getNumFaces() + "_" + quickHull.getNumVertices();
        } catch(Exception ignore) {
            return " ";
        }
    }

    public double getCenterDistance() {//distance from center of mass to center of bounding box
        double sumX = 0;
        double sumY = 0;
        double sumZ = 0;

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        double maxZ = -Double.MAX_VALUE;

        for (Cube cube : cubes) {
            sumX += cube.x();
            sumY += cube.y();
            sumZ += cube.z();

            if (cube.x() < minX) minX = cube.x();
            if (cube.y() < minY) minY = cube.y();
            if (cube.z() < minZ) minZ = cube.z();
            if (cube.x() > maxX) maxX = cube.x();
            if (cube.y() > maxY) maxY = cube.y();
            if (cube.z() > maxZ) maxZ = cube.z();

        }

        double centerMassX = sumX / cubes.size();
        double centerMassY = sumY / cubes.size();
        double centerMassZ = sumZ / cubes.size();

        double centerBoundingBoxX = (minX + maxX) / 2;
        double centerBoundingBoxY = (minY + maxY) / 2;
        double centerBoundingBoxZ = (minZ + maxZ) / 2;

        double dx = centerMassX - centerBoundingBoxX;
        double dy = centerMassY - centerBoundingBoxY;
        double dz = centerMassZ - centerBoundingBoxZ;


        double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));

        return round(distance);
    }

    private static double round(double d) {
        return Math.round(d * DECIMAL_SCALING) / DECIMAL_SCALING;
    }

    public List<Point3D> getCubePoints() {
        return this.cubes.stream().map(Cube::getPoint).toList();
    }

    public String getKey() {
        String s = "";

        s += longHashCode();
        s += getViewableFacesPerLayer2();
        s += getHullMetrics();
        //s += getBensNumbers();
        //s += getNeighborCounts();
        //s += getCenterDistance();

        return s;
    }

    @Override
    public String toString() {//for debugging
        boolean[][][] grid = this.toGrid();

        StringBuilder sb = new StringBuilder();

        sb.append("\n~~~~Start PolyCube~~~\n\n");

        sb.append("Volume: ").append(this.cubes.size());
        sb.append("\nKey: ").append(this.getKey());
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