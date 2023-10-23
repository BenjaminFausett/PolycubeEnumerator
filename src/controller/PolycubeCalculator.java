package controller;

import model.Point;
import model.Polycube;
import model.PolycubeRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class PolycubeCalculator {

    public static void calculatePolycubes(int n) throws IOException, ClassNotFoundException {
        Instant start = Instant.now();
        PolycubeRepository polycubeRepository = new PolycubeRepository();

        int largestBackupN = polycubeRepository.getLargestCompletedN();

        if(largestBackupN >= n) {
            List<Polycube> polycubes = polycubeRepository.getPolycubes(largestBackupN);
            System.out.printf("%-5s %d%n", largestBackupN, polycubes.size());

        } else {

            for (int i = largestBackupN; i < n; i++) {
                List<Polycube> polycubes = polycubeRepository.getPolycubes(i);

                System.out.printf("%-5s %d%n", i, polycubes.size());

                polycubes.parallelStream().forEach(polycube -> {
                    Set<Point> points = polycube.getValidNewCubePoint();
                    points.parallelStream().forEach(point -> {
                        Polycube candidateCube = new Polycube(polycube, point);
                        if (!polycubeRepository.exists(candidateCube)) {
                            polycubeRepository.add(candidateCube);
                            //System.out.println(candidateCube);
                        }
                    });
                });
                //TODO make backup saving and loading fast. use ActiveJ Serializer says google
                //polycubeRepository.backupPolyCubes(i + 1);
                polycubeRepository.clearPolyCubes(i);
            }

            List<Polycube> polycubes = polycubeRepository.getPolycubes(n);
            System.out.printf("%-5s %d%n", n, polycubes.size());

        }

        Duration duration = Duration.between(start, Instant.now());
        double seconds = duration.getSeconds() + (double) duration.getNano() / 1_000_000_000;
        String formattedDuration = String.format("%.1f", seconds);

        System.out.println("\nn = " + n +" finished in " + formattedDuration + "s");
    }
}
