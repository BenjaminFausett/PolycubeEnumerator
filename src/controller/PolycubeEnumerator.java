package controller;

import model.Polycube;
import model.Rotation;

import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

public class PolycubeEnumerator {

    //TODO fix bug causing this this to do 32 rotations, not 24.
    public static Polycube findCanonicalRotation(final Polycube polycube) {
        Optional<Polycube> canonicalPolycube = IntStream.range(0, 4).parallel()
                .boxed()
                .flatMap(x -> IntStream.range(0, 4).mapToObj(y -> new int[]{x, y}))
                .flatMap(xy -> IntStream.range(0, 2).mapToObj(z -> new int[]{xy[0], xy[1], z}))
                .map(xyz -> {
                    Polycube rotatedPolycube = polycube.clone();
                    rotatedPolycube.rotate(xyz[0], xyz[1], xyz[2]);
                    return rotatedPolycube;
                }).min(Comparator.comparingInt(Polycube::hashCode));
        return canonicalPolycube.orElse(null);
    }

    //the issue that im having in this class is that both of these methods just return the state of the cube as it was passed. they find all the rotations and do all the math, the hashcodes are all the same

    public static Rotation test(final Polycube polycube) {
        int min = Integer.MAX_VALUE;
        Rotation rotation = null;
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 4; z++) {
                for (int y = 0; y < 3; y++) {
                    Polycube p = polycube.clone();
                    p.rotate(x, y, z);
                    if(p.hashCode() < min) {
                        min = p.hashCode();
                        rotation = p.getRotation();
                    }
                }
            }
        }
        return rotation;
    }
}
