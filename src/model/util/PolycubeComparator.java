package model.util;

import model.Polycube;
import model.records.Cube;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class does the slow but necessary true equality check between two polycubes via translation, rotation, and reflection
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
    public static boolean equalsAny(Polycube candidatePolycube, List<Polycube> polycubes) {
        List<Cube> candidateCubes = candidatePolycube.getCubes();
        List<Cube> candidateCubesReflected = reflectAcrossX(candidateCubes);

        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    for (Polycube polycube : polycubes) {
                        Set<Cube> cubes = normalize(polycube.getCubes());
                        if (normalize(candidateCubes).containsAll(cubes) || normalize(candidateCubesReflected).containsAll(cubes)) {
                            return true;
                        }
                    }
                    candidateCubes = rotateX(candidateCubes);
                    candidateCubesReflected = rotateX(candidateCubesReflected);
                }
                candidateCubes = rotateY(candidateCubes);
                candidateCubesReflected = rotateY(candidateCubesReflected);
            }
            candidateCubes = rotateZ(candidateCubes);
            candidateCubesReflected = rotateZ(candidateCubesReflected);
        }
        return false;
    }


    /**
     * Performs a translation of the coordinates to shift the represented polycubes corner to 0, 0, 0
     */
    private static Set<Cube> normalize(List<Cube> cubes) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        for (Cube cube : cubes) {
            if (cube.x() < minX) minX = cube.x();
            if (cube.y() < minY) minY = cube.y();
            if (cube.z() < minZ) minZ = cube.z();
        }

        Set<Cube> normalizedCubes = new HashSet<>();
        for (Cube cube : cubes) {
            normalizedCubes.add(CubeFactory.get(cube.x() - minX, cube.y() - minY, cube.z() - minZ));
        }

        return normalizedCubes;
    }

    private static List<Cube> rotateX(List<Cube> cubes) {
        List<Cube> rotatedCubes = new ArrayList<>();
        for (Cube cube : cubes) {
            rotatedCubes.add(CubeFactory.get(cube.x(), -cube.z(), cube.y()));
        }
        return rotatedCubes;
    }

    private static List<Cube> rotateY(List<Cube> cubes) {
        List<Cube> rotatedCubes = new ArrayList<>();
        for (Cube cube : cubes) {
            rotatedCubes.add(CubeFactory.get(cube.z(), cube.y(), -cube.x()));
        }
        return rotatedCubes;
    }

    private static List<Cube> rotateZ(List<Cube> cubes) {
        List<Cube> rotatedCubes = new ArrayList<>();
        for (Cube cube : cubes) {
            rotatedCubes.add(CubeFactory.get(-cube.y(), cube.x(), cube.z()));
        }
        return rotatedCubes;
    }

    private static List<Cube> reflectAcrossX(List<Cube> cubes) {
        List<Cube> reflectedCubes = new ArrayList<>();
        for (Cube cube : cubes) {
            reflectedCubes.add(CubeFactory.get(-cube.x(), cube.y(), cube.z()));
        }
        return reflectedCubes;
    }

}
