package controller;

import model.Coordinate;
import model.Polycube;
import model.PolycubeRepository;

import java.time.Duration;
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
//                        System.out.println(candidateCube);
//                        candidateCube.printMetrics();
                    }
                });
            });

            Duration duration = Duration.between(start, Instant.now());
            double seconds = duration.getSeconds() + (double) duration.getNano() / 1_000_000_000;
            String formattedDuration = String.format("%.1f", seconds);

            System.out.println((i) + " finished in " + formattedDuration + " seconds");
        }

        System.out.println(polycubeRepository);
    }
}
