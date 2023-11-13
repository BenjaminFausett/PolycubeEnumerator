package controller;

import config.Config;
import model.Polycube;
import model.records.Point3D;
import repository.PolycubeRepository;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class PolycubeEnumerator {

    public static void calculatePolycubes(int n) {
        Instant start = Instant.now();
        PolycubeRepository polycubeRepository = new PolycubeRepository();

        for (int i = 1; i < n; i++) {
            List<Polycube> polycubes = polycubeRepository.getPolycubes(i);

            System.out.printf("%-5s %d%n", i, polycubes.size());

            polycubes.parallelStream().forEach(polycube -> {
                Set<Point3D> points = polycube.getValidCubesToAdd();
                points.forEach(point -> {
                    Polycube candidateCube = new Polycube(polycube, point);
                    polycubeRepository.addIfUnique(candidateCube);
                });
            });

            polycubeRepository.clearPolyCubes(i);
        }

        List<Polycube> polycubes = polycubeRepository.getPolycubes(n);
        System.out.printf("%-5s %d%n", n, polycubes.size());

        if (Config.DEBUG_ON) {
            int hashCount = polycubeRepository.getPolycubeMap(n).keySet().size();

            double collisionRate = (1 - ((double) hashCount / polycubes.size())) * 100;

            DecimalFormat formatter = new DecimalFormat("#.###");
            System.out.println("Key Collision Rate: " + formatter.format(collisionRate) + "%");
            System.out.println("Key Collision Count: " + (polycubes.size() - hashCount));
            System.out.println("Unique keys: " + hashCount);

            polycubeRepository.getPolycubeMap(n).values().stream().filter(list -> list.size() > 1).findFirst().ifPresent(System.out::println);
        }


        Duration duration = Duration.between(start, Instant.now());
        double seconds = duration.getSeconds() + (double) duration.getNano() / 1_000_000_000;
        String formattedDuration = String.format("%.1f", seconds);

        System.out.println("\nn = " + n + " finished in " + formattedDuration + "s");
    }
}
