package controller;

import model.Polycube;

import java.util.Comparator;
import java.util.Optional;
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
}
