package repository;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import config.Config;
import model.Polycube;
import model.records.Point;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PolycubeRepository {

    private final HashSet<Polycube> monoCubes;    //1
    private final HashSet<Polycube> diCubes;      //2
    private final HashSet<Polycube> triCubes;     //3
    private final HashSet<Polycube> tetraCubes;   //4
    private final HashSet<Polycube> pentaCubes;   //5
    private final HashSet<Polycube> hexaCubes;    //6
    private final HashSet<Polycube> heptaCubes;   //7
    private final HashSet<Polycube> octaCubes;    //8
    private final HashSet<Polycube> nonaCubes;    //9
    private final HashSet<Polycube> decaCubes;    //10
    private final HashSet<Polycube> undecaCubes;  //11
    private final HashSet<Polycube> dodecaCubes;  //12
    private final HashSet<Polycube> tridecaCubes; //13
    private final HashSet<Polycube> tetradecaCubes;//14
    private final HashSet<Polycube> pentadecaCubes; //15
    private final HashSet<Polycube> hexadecaCubes;  //16
    private final HashSet<Polycube> heptadecaCubes; //17
    private final HashSet<Polycube> octadecaCubes;  //18
    private final HashSet<Polycube> nonadecaCubes;  //19
    private final HashSet<Polycube> icosiCubes;     //20
    private int largestCompletedN;

    public PolycubeRepository() throws IOException {
        monoCubes = new HashSet<>();
        diCubes = new HashSet<>();
        triCubes = new HashSet<>();
        tetraCubes = new HashSet<>();
        pentaCubes = new HashSet<>();
        hexaCubes = new HashSet<>();
        heptaCubes = new HashSet<>();
        octaCubes = new HashSet<>();
        nonaCubes = new HashSet<>();
        decaCubes = new HashSet<>();
        undecaCubes = new HashSet<>();
        dodecaCubes = new HashSet<>();
        tridecaCubes = new HashSet<>();
        tetradecaCubes = new HashSet<>();
        pentadecaCubes = new HashSet<>();
        hexadecaCubes = new HashSet<>();
        heptadecaCubes = new HashSet<>();
        octadecaCubes = new HashSet<>();
        nonadecaCubes = new HashSet<>();
        icosiCubes = new HashSet<>();

        this.loadBackup();
    }

    public static void printBackupFileSizes() throws Exception {
        int n = 1;
        while (Files.exists(Paths.get(n + "_cubes.ser")) && n > 0) {
            FileInputStream fileIn = new FileInputStream(n + "_cubes.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<Polycube> polycubes = (List<Polycube>) in.readObject();

            System.out.printf("%-5s %d%n", n, polycubes.size());

            in.close();
            fileIn.close();
            n++;
        }
    }

    public synchronized void add(Polycube polycube) {
        this.getPolycubes(polycube.getVolume()).add(polycube);
    }

    public boolean exists(Polycube polycube) {
        return this.getPolycubes(polycube.getVolume()).contains(polycube);
    }

    public HashSet<Polycube> getPolycubes(int n) {
        switch (n) {
            case 1 -> {
                return monoCubes;
            }
            case 2 -> {
                return diCubes;
            }
            case 3 -> {
                return triCubes;
            }
            case 4 -> {
                return tetraCubes;
            }
            case 5 -> {
                return pentaCubes;
            }
            case 6 -> {
                return hexaCubes;
            }
            case 7 -> {
                return heptaCubes;
            }
            case 8 -> {
                return octaCubes;
            }
            case 9 -> {
                return nonaCubes;
            }
            case 10 -> {
                return decaCubes;
            }
            case 11 -> {
                return undecaCubes;
            }
            case 12 -> {
                return dodecaCubes;
            }
            case 13 -> {
                return tridecaCubes;
            }
            case 14 -> {
                return tetradecaCubes;
            }
            case 15 -> {
                return pentadecaCubes;
            }
            case 16 -> {
                return hexadecaCubes;
            }
            case 17 -> {
                return heptadecaCubes;
            }
            case 18 -> {
                return octadecaCubes;
            }
            case 19 -> {
                return nonadecaCubes;
            }
            case 20 -> {
                return icosiCubes;
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        for(int i = 1; i <= 20; i++) {
            Set<Polycube> polycubes = this.getPolycubes(i);
            if(!polycubes.isEmpty()) {
                s.append(String.format("%-5s %d\n", "1", polycubes.size()));
            }
        }
        return s.toString();
    }

    public int getLargestCompletedN() {
        return largestCompletedN;
    }

    public void backupPolyCubes(int n) throws IOException {
        if (!Config.SAVE_TO_CACHE) {
            return;
        }
        System.out.println("Started saving cache of cube size n = " + n);

        String filename = "polycube cache/cubes" + n + ".bin";

        new File("polycube cache").mkdirs();

        if (Files.notExists(Path.of(filename))) {
            Output output = new Output(new FileOutputStream(filename));
            Kryo kryo = new Kryo();
            kryo.register(HashSet.class);
            kryo.register(Polycube.class);
            kryo.register(ArrayList.class);
            kryo.register(Point.class);
            kryo.writeObject(output, this.getPolycubes(n));
            output.close();
        }

        System.out.println("Finished saving cache of cube size n = " + n);
    }

    private void loadBackup() throws IOException {
        int n = 20;
        while (Files.notExists(Paths.get("polycube cache/cubes" + n + ".bin")) && n > 0) {
            n--;
        }

        if (n == 0 || !Config.LOAD_FROM_CACHE) {//no backup so starting from 1. ill be a pal and generate all the cubes for n = 1 for you
            Polycube monoCube = new Polycube();
            monoCubes.add(monoCube);
            largestCompletedN = 1;
            return;
        }
        System.out.println("Found cache of cube size n = " + n);

        this.largestCompletedN = n;

        try {
            FileInputStream fileIn = new FileInputStream("polycube cache/cubes" + n + ".bin");
            Kryo kryo = new Kryo();
            kryo.register(HashSet.class);
            kryo.register(Polycube.class);
            kryo.register(ArrayList.class);
            kryo.register(Point.class);

            Input input = new Input(fileIn);
            Set<Polycube> polycubes = kryo.readObject(input, HashSet.class);

            input.close();
            fileIn.close();

            switch (n) {
                case 1 -> monoCubes.addAll(polycubes);
                case 2 -> diCubes.addAll(polycubes);
                case 3 -> triCubes.addAll(polycubes);
                case 4 -> tetraCubes.addAll(polycubes);
                case 5 -> pentaCubes.addAll(polycubes);
                case 6 -> hexaCubes.addAll(polycubes);
                case 7 -> heptaCubes.addAll(polycubes);
                case 8 -> octaCubes.addAll(polycubes);
                case 9 -> nonaCubes.addAll(polycubes);
                case 10 -> decaCubes.addAll(polycubes);
                case 11 -> undecaCubes.addAll(polycubes);
                case 12 -> dodecaCubes.addAll(polycubes);
                case 13 -> tridecaCubes.addAll(polycubes);
                case 14 -> tetradecaCubes.addAll(polycubes);
                case 15 -> pentadecaCubes.addAll(polycubes);
                case 16 -> hexadecaCubes.addAll(polycubes);
                case 17 -> heptadecaCubes.addAll(polycubes);
                case 18 -> octadecaCubes.addAll(polycubes);
                case 19 -> nonadecaCubes.addAll(polycubes);
                case 20 -> icosiCubes.addAll(polycubes);
            }
            System.out.println("Loaded cache of cube size n = " + n);
        } catch (IOException e) {
            throw e;
        }
    }

    public void clearPolyCubes(int n) {
        switch (n) {
            case 1 -> monoCubes.clear();
            case 2 -> diCubes.clear();
            case 3 -> triCubes.clear();
            case 4 -> tetraCubes.clear();
            case 5 -> pentaCubes.clear();
            case 6 -> hexaCubes.clear();
            case 7 -> heptaCubes.clear();
            case 8 -> octaCubes.clear();
            case 9 -> nonaCubes.clear();
            case 10 -> decaCubes.clear();
            case 11 -> undecaCubes.clear();
            case 12 -> dodecaCubes.clear();
            case 13 -> tridecaCubes.clear();
            case 14 -> tetradecaCubes.clear();
            case 15 -> pentadecaCubes.clear();
            case 16 -> hexadecaCubes.clear();
            case 17 -> heptadecaCubes.clear();
            case 18 -> octadecaCubes.clear();
            case 19 -> nonadecaCubes.clear();
            case 20 -> icosiCubes.clear();
        }
    }
}
