package model.util;

import model.Polycube;
import model.records.Cube;
import model.records.Point3D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class does the slow but necessary true equality check between two polycubes via translation, rotation, and reflection.
 */
public class PolycubeComparator {

    /**
     * Compares if the candidatePolycube is equal to any of the polycubes.
     * Equality is checked by rotating the reflecting the candidatePolycube to get all possible orientations and then comparing against the list.
     *
     * @param candidatePolycube The polycube to check if it is equal to any of the polycube list.
     * @param polycubes         The polycube list that the candidatePolycube will be compared against.
     * @return True if the candidatePolycube is equal to at least one of the polycubes in the polycube list, otherwise false.
     */
    public static boolean equalsAny(final Polycube candidatePolycube, final List<Polycube> polycubes) {
        List<Point3D> candidatePoint = candidatePolycube.getCubePoints();
        List<Point3D> candidatePointReflected = reflectAcrossX(candidatePoint);

        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    for (Polycube polycube : polycubes) {
                        Set<Point3D> cubes = normalize(polycube.getCubePoints());
                        if (normalize(candidatePoint).containsAll(cubes) || normalize(candidatePointReflected).containsAll(cubes)) {
                            return true;
                        }
                    }
                    candidatePoint = rotateX(candidatePoint);
                    candidatePointReflected = rotateX(candidatePointReflected);
                }
                candidatePoint = rotateY(candidatePoint);
                candidatePointReflected = rotateY(candidatePointReflected);
            }
            candidatePoint = rotateZ(candidatePoint);
            candidatePointReflected = rotateZ(candidatePointReflected);
        }
        return false;
    }

    /**
     * Performs a translation of the coordinates to shift the represented polycubes corner to 0, 0, 0
     */
    private static Set<Point3D> normalize(List<Point3D> cubes) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        for (Point3D cube : cubes) {
            if (cube.x() < minX) minX = cube.x();
            if (cube.y() < minY) minY = cube.y();
            if (cube.z() < minZ) minZ = cube.z();
        }

        Set<Point3D> normalizedCubes = new HashSet<>();
        for (Point3D cube : cubes) {
            normalizedCubes.add(new Point3D(cube.x() - minX, cube.y() - minY, cube.z() - minZ));
        }

        return normalizedCubes;
    }

    private static List<Point3D> rotateX(List<Point3D> cubes) {
        List<Point3D> rotatedCubes = new ArrayList<>();
        for (Point3D cube : cubes) {
            rotatedCubes.add(new Point3D(cube.x(), -cube.z(), cube.y()));
        }
        return rotatedCubes;
    }

    private static List<Point3D> rotateY(List<Point3D> cubes) {
        List<Point3D> rotatedCubes = new ArrayList<>();
        for (Point3D cube : cubes) {
            rotatedCubes.add(new Point3D(cube.z(), cube.y(), -cube.x()));
        }
        return rotatedCubes;
    }

    private static List<Point3D> rotateZ(List<Point3D> cubes) {
        List<Point3D> rotatedCubes = new ArrayList<>();
        for (Point3D cube : cubes) {
            rotatedCubes.add(new Point3D(-cube.y(), cube.x(), cube.z()));
        }
        return rotatedCubes;
    }

    private static List<Point3D> reflectAcrossX(List<Point3D> cubes) {
        List<Point3D> reflectedCubes = new ArrayList<>();
        for (Point3D cube : cubes) {
            reflectedCubes.add(new Point3D(-cube.x(), cube.y(), cube.z()));
        }
        return reflectedCubes;
    }

}
