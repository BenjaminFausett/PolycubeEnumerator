import controller.PolycubeEnumerator;
import model.Coordinate;
import model.Polycube;
import model.PolycubeRepository;
import model.Rotation;

import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        normal();
    }

    public static void normal() {
        Polycube monoCube = new Polycube();

        PolycubeRepository polycubeRepository = new PolycubeRepository();
        polycubeRepository.add(monoCube);

        for (int i = 1; i < 9; i++) {
            Instant start = Instant.now();
            List<Polycube> polycubes = polycubeRepository.getPolycubes(i);
            for(Polycube polycube: polycubes) {
                List<Coordinate> coordinates = polycube.getValidNewCubePlacements();
                for(Coordinate coordinate: coordinates) {
                    Polycube candidateCube = new Polycube(polycube, coordinate);
                    candidateCube = PolycubeEnumerator.findCanonicalRotation(candidateCube);
                    if(!polycubeRepository.exists(candidateCube)) {
                        polycubeRepository.add(candidateCube);
                    }
                }
            }
            System.out.println("Finished polycubes of size " + (i + 1) + " in " + (Instant.now().getEpochSecond() - start.getEpochSecond()) + " seconds.");
        }

        System.out.println(polycubeRepository);
    }

    public static void test() {
        Polycube monoCube = new Polycube();

        List<Coordinate> coordinates = monoCube.getValidNewCubePlacements();

        Polycube diCube1 = new Polycube(monoCube, coordinates.get(1));
        Polycube diCube2 = new Polycube(monoCube, coordinates.get(0));

        System.out.println(diCube1.hashCode());
        System.out.println(diCube1);
        System.out.println(diCube1.getRotation());

        System.out.println("performing CanonicalRotation");
        //System.out.println(PolycubeEnumerator.test(diCube1));
        diCube2 = PolycubeEnumerator.findCanonicalRotation(diCube2);

        System.out.println(diCube1.hashCode());
        System.out.println(diCube1);


        System.out.println(diCube2.hashCode());
        System.out.println(diCube2);
        System.out.println(diCube2.getRotation());
    }
}