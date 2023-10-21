package model;

import java.util.HashSet;
import java.util.List;

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

    public PolycubeRepository() {
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

        monoCubes.add(new Polycube());
    }

    public void add(Polycube polycube) {
        switch (polycube.getVolume()) {
            case 1 -> monoCubes.add(polycube);
            case 2 -> diCubes.add(polycube);
            case 3 -> triCubes.add(polycube);
            case 4 -> tetraCubes.add(polycube);
            case 5 -> pentaCubes.add(polycube);
            case 6 -> hexaCubes.add(polycube);
            case 7 -> heptaCubes.add(polycube);
            case 8 -> octaCubes.add(polycube);
            case 9 -> nonaCubes.add(polycube);
            case 10 -> decaCubes.add(polycube);
            case 11 -> undecaCubes.add(polycube);
            case 12 -> dodecaCubes.add(polycube);
            case 13 -> tridecaCubes.add(polycube);
            case 14 -> tetradecaCubes.add(polycube);
            case 15 -> pentadecaCubes.add(polycube);
            case 16 -> hexadecaCubes.add(polycube);
            case 17 -> heptadecaCubes.add(polycube);
            case 18 -> octadecaCubes.add(polycube);
            case 19 -> nonadecaCubes.add(polycube);
            case 20 -> icosiCubes.add(polycube);
        }
    }

    public boolean exists(Polycube polycube) {
        switch (polycube.getVolume()) {
            case 1 -> {
                return monoCubes.contains(polycube);
            }
            case 2 -> {
                return diCubes.contains(polycube);
            }
            case 3 -> {
                return triCubes.contains(polycube);
            }
            case 4 -> {
                return tetraCubes.contains(polycube);
            }
            case 5 -> {
                return pentaCubes.contains(polycube);
            }
            case 6 -> {
                return hexaCubes.contains(polycube);
            }
            case 7 -> {
                return heptaCubes.contains(polycube);
            }
            case 8 -> {
                return octaCubes.contains(polycube);
            }
            case 9 -> {
                return nonaCubes.contains(polycube);
            }
            case 10 -> {
                return decaCubes.contains(polycube);
            }
            case 11 -> {
                return undecaCubes.contains(polycube);
            }
            case 12 -> {
                return dodecaCubes.contains(polycube);
            }
            case 13 -> {
                return tridecaCubes.contains(polycube);
            }
            case 14 -> {
                return tetradecaCubes.contains(polycube);
            }
            case 15 -> {
                return pentadecaCubes.contains(polycube);
            }
            case 16 -> {
                return hexadecaCubes.contains(polycube);
            }
            case 17 -> {
                return heptadecaCubes.contains(polycube);
            }
            case 18 -> {
                return octadecaCubes.contains(polycube);
            }
            case 19 -> {
                return nonadecaCubes.contains(polycube);
            }
            case 20 -> {
                return icosiCubes.contains(polycube);
            }
        }
        return false;
    }

    public List<Polycube> getPolycubes(int n) {
        switch (n) {
            case 1 -> {
                return monoCubes.stream().map(Polycube::clone).toList();
            }
            case 2 -> {
                return diCubes.stream().map(Polycube::clone).toList();
            }
            case 3 -> {
                return triCubes.stream().map(Polycube::clone).toList();
            }
            case 4 -> {
                return tetraCubes.stream().map(Polycube::clone).toList();
            }
            case 5 -> {
                return pentaCubes.stream().map(Polycube::clone).toList();
            }
            case 6 -> {
                return hexaCubes.stream().map(Polycube::clone).toList();
            }
            case 7 -> {
                return heptaCubes.stream().map(Polycube::clone).toList();
            }
            case 8 -> {
                return octaCubes.stream().map(Polycube::clone).toList();
            }
            case 9 -> {
                return nonaCubes.stream().map(Polycube::clone).toList();
            }
            case 10 -> {
                return decaCubes.stream().map(Polycube::clone).toList();
            }
            case 11 -> {
                return undecaCubes.stream().map(Polycube::clone).toList();
            }
            case 12 -> {
                return dodecaCubes.stream().map(Polycube::clone).toList();
            }
            case 13 -> {
                return tridecaCubes.stream().map(Polycube::clone).toList();
            }
            case 14 -> {
                return tetradecaCubes.stream().map(Polycube::clone).toList();
            }
            case 15 -> {
                return pentadecaCubes.stream().map(Polycube::clone).toList();
            }
            case 16 -> {
                return hexadecaCubes.stream().map(Polycube::clone).toList();
            }
            case 17 -> {
                return heptadecaCubes.stream().map(Polycube::clone).toList();
            }
            case 18 -> {
                return octadecaCubes.stream().map(Polycube::clone).toList();
            }
            case 19 -> {
                return nonadecaCubes.stream().map(Polycube::clone).toList();
            }
            case 20 -> {
                return icosiCubes.stream().map(Polycube::clone).toList();
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        if (!monoCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "1", monoCubes.size()));
        }
        if (!diCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "2", diCubes.size()));
        }
        if (!triCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "3", triCubes.size()));
        }
        if (!tetraCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "4", tetraCubes.size()));
        }
        if (!pentaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "5", pentaCubes.size()));
        }
        if (!hexaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "6", hexaCubes.size()));
        }
        if (!heptaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "7", heptaCubes.size()));
        }
        if (!octaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "8", octaCubes.size()));
        }
        if (!nonaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "9", nonaCubes.size()));
        }
        if (!decaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "10", decaCubes.size()));
        }
        if (!undecaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "11", undecaCubes.size()));
        }
        if (!dodecaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "12", dodecaCubes.size()));
        }
        if (!tridecaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "13", tridecaCubes.size()));
        }
        if (!tetradecaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "14", tetradecaCubes.size()));
        }
        if (!pentadecaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "15", pentadecaCubes.size()));
        }
        if (!hexadecaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "16", hexadecaCubes.size()));
        }
        if (!heptadecaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "17", heptadecaCubes.size()));
        }
        if (!octadecaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "18", octadecaCubes.size()));
        }
        if (!nonadecaCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "19", nonadecaCubes.size()));
        }
        if (!icosiCubes.isEmpty()) {
            s.append(String.format("%-5s %d\n", "20", icosiCubes.size()));
        }

        return s.toString();
    }
}
