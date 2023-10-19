import model.Coordinate;
import model.Polycube;
import model.PolycubeRepository;

import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        normal();
    }

    //leaving it here for now. all that work to check if a cube has a void in it or not to try and reduce my cube numbers to the correct numbers and they are the exact same. the fact that it elimenated zero cubes makes me think my void checking method is broken.
    public static void normal() {
        Polycube monoCube = new Polycube();

        PolycubeRepository polycubeRepository = new PolycubeRepository();
        polycubeRepository.add(monoCube);

        for (int i = 2; i < 11; i++) {
            Instant start = Instant.now();
            List<Polycube> polycubes = polycubeRepository.getPolycubes(i - 1);


            polycubes.forEach(polycube -> {
                List<Coordinate> coordinates = polycube.getValidNewCubePlacements();
                coordinates.forEach(coordinate -> {
                    Polycube candidateCube = new Polycube(polycube, coordinate);
                    if (!polycubeRepository.exists(candidateCube)) {
                        polycubeRepository.add(candidateCube);
                        if(candidateCube.getVolume() == -1) {
                            candidateCube.printMetrics();
                            System.out.println(candidateCube);
                        }
                    }
                });
            });

            System.out.println("Finished polycubes of size " + (i) + " in " + (Instant.now().getEpochSecond() - start.getEpochSecond()) + " seconds.");
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

        System.out.println("performing CanonicalRotation");
        //System.out.println(PolycubeEnumerator.test(diCube1));

        System.out.println(diCube1.hashCode());
        System.out.println(diCube1);


        System.out.println(diCube2.hashCode());
        System.out.println(diCube2);
    }

}