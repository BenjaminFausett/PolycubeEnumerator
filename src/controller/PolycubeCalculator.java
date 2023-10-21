package controller;

import model.Coordinate;
import model.Polycube;
import model.PolycubeRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public class PolycubeCalculator {

    public static void calculatePolycubes(int n) {
        PolycubeRepository polycubeRepository = new PolycubeRepository();

        for (int i = 2; i < n + 1; i++) {
            Instant start = Instant.now();
            List<Polycube> polycubes = polycubeRepository.getPolycubes(i - 1);

            for(Polycube polycube: polycubes) {
                Collection<Coordinate> coordinates = polycube.getValidNewCubePlacements();
                for(Coordinate coordinate: coordinates) {
                    Polycube candidateCube = new Polycube(polycube, coordinate);
                    if (!polycubeRepository.exists(candidateCube)) {
                        polycubeRepository.add(candidateCube);
                    }
                }
            }

            System.out.println("Finished polycubes of size " + (i) + " in " + (Instant.now().getEpochSecond() - start.getEpochSecond()) + " seconds.");
        }

        System.out.println(polycubeRepository);
    }
}
