package controller;

import config.Config;
import model.Polycube;
import model.records.Point;
import repository.PolycubeRepository;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PolycubeEnumerator {

    public static void calculatePolycubes(int n) throws IOException {
        Instant start = Instant.now();
        PolycubeRepository polycubeRepository = new PolycubeRepository();

        int largestBackupN = polycubeRepository.getLargestCompletedN();

        if (largestBackupN >= n) {
            Set<Polycube> polycubes = polycubeRepository.getPolycubes(largestBackupN);
            System.out.printf("%-5s %d%n", largestBackupN, polycubes.size());

        } else {

            for (int i = largestBackupN; i < n; i++) {
                Set<Polycube> polycubes = polycubeRepository.getPolycubes(i);

                System.out.printf("%-5s %d%n", i, polycubes.size());

                polycubes.parallelStream().forEach(polycube -> {
                    Set<Point> points = polycube.getValidNewCubePoints();
                    points.forEach(point -> {
                        Polycube candidateCube = new Polycube(polycube, point);
                        if (!polycubeRepository.exists(candidateCube)) {
                            polycubeRepository.add(candidateCube);
                        }
                    });
                });

                polycubeRepository.backupPolyCubes(i + 1);
                polycubeRepository.clearPolyCubes(i);
            }

            HashSet<Polycube> polycubes = polycubeRepository.getPolycubes(n);
            System.out.printf("%-5s %d%n", n, polycubes.size());

            if (Config.DEBUG_ON) {
                Set<Integer> hashes = polycubes.stream().map(Polycube::hashCode).collect(Collectors.toSet());

                double collisionRate = (1 - ((double) hashes.size() / polycubes.size())) * 100;

                DecimalFormat formatter = new DecimalFormat("#.###");
                System.out.println("Hash Collision Rate: " + formatter.format(collisionRate) + "%");
            }
        }

        Duration duration = Duration.between(start, Instant.now());
        double seconds = duration.getSeconds() + (double) duration.getNano() / 1_000_000_000;
        String formattedDuration = String.format("%.1f", seconds);

        System.out.println("\nn = " + n + " finished in " + formattedDuration + "s");
    }
}
