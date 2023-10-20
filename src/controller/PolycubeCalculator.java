package controller;

import model.Coordinate;
import model.Polycube;
import model.PolycubeRepository;

import java.time.Instant;
import java.util.List;

public class PolycubeCalculator {

    public static void calculatePolycubes(int n) {
        Polycube monoCube = new Polycube();

        PolycubeRepository polycubeRepository = new PolycubeRepository();
        polycubeRepository.add(monoCube);

        for (int i = 2; i < n + 1; i++) {
            Instant start = Instant.now();
            List<Polycube> polycubes = polycubeRepository.getPolycubes(i - 1);


            polycubes.forEach(polycube -> {
                List<Coordinate> coordinates = polycube.getValidNewCubePlacements();
                coordinates.forEach(coordinate -> {
                    Polycube candidateCube = new Polycube(polycube, coordinate);
                    if (!polycubeRepository.exists(candidateCube)) {
                        polycubeRepository.add(candidateCube);

//                        if (candidateCube.getVolume() == -1) {
//                            candidateCube.printMetrics();
//                            System.out.println(candidateCube);
//                        }
                    }
                });
            });

            System.out.println("Finished polycubes of size " + (i) + " in " + (Instant.now().getEpochSecond() - start.getEpochSecond()) + " seconds.");
        }

        System.out.println(polycubeRepository);
    }
}
