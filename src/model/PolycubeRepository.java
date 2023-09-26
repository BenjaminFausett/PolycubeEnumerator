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
        }
        return null;
    }

    public String toString() {
        String s = "";
        s += "1.  monoCubes count: " + monoCubes.size() + "\n";
        s += "2.  diCubes count: " + diCubes.size() + "\n";
        s += "3.  triCubes count: " + triCubes.size() + "\n";
        s += "4.  tetraCubes count: " + tetraCubes.size() + "\n";
        s += "5.  pentaCubes count: " + pentaCubes.size() + "\n";
        s += "6.  hexaCubes count: " + hexaCubes.size() + "\n";
        s += "7.  heptaCubes count: " + heptaCubes.size() + "\n";
        s += "8.  octaCubes count: " + octaCubes.size() + "\n";
        s += "9.  nonaCubes count: " + nonaCubes.size() + "\n";
        s += "10. decaCubes count: " + decaCubes.size() + "\n";

        return s;
    }
}
