package repository;

import model.Polycube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PolycubeRepository {

    private final ConcurrentHashMap<Integer, List<Polycube>> monoCubes;    //1
    private final ConcurrentHashMap<Integer, List<Polycube>> diCubes;      //2
    private final ConcurrentHashMap<Integer, List<Polycube>> triCubes;     //3
    private final ConcurrentHashMap<Integer, List<Polycube>> tetraCubes;   //4
    private final ConcurrentHashMap<Integer, List<Polycube>> pentaCubes;   //5
    private final ConcurrentHashMap<Integer, List<Polycube>> hexaCubes;    //6
    private final ConcurrentHashMap<Integer, List<Polycube>> heptaCubes;   //7
    private final ConcurrentHashMap<Integer, List<Polycube>> octaCubes;    //8
    private final ConcurrentHashMap<Integer, List<Polycube>> nonaCubes;    //9
    private final ConcurrentHashMap<Integer, List<Polycube>> decaCubes;    //10
    private final ConcurrentHashMap<Integer, List<Polycube>> undecaCubes;  //11
    private final ConcurrentHashMap<Integer, List<Polycube>> dodecaCubes;  //12
    private final ConcurrentHashMap<Integer, List<Polycube>> tridecaCubes; //13
    private final ConcurrentHashMap<Integer, List<Polycube>> tetradecaCubes;//14
    private final ConcurrentHashMap<Integer, List<Polycube>> pentadecaCubes; //15
    private final ConcurrentHashMap<Integer, List<Polycube>> hexadecaCubes;  //16
    private final ConcurrentHashMap<Integer, List<Polycube>> heptadecaCubes; //17
    private final ConcurrentHashMap<Integer, List<Polycube>> octadecaCubes;  //18
    private final ConcurrentHashMap<Integer, List<Polycube>> nonadecaCubes;  //19
    private final ConcurrentHashMap<Integer, List<Polycube>> icosiCubes;     //20

    public PolycubeRepository() {
        monoCubes = new ConcurrentHashMap<>();
        diCubes = new ConcurrentHashMap<>();
        triCubes = new ConcurrentHashMap<>();
        tetraCubes = new ConcurrentHashMap<>();
        pentaCubes = new ConcurrentHashMap<>();
        hexaCubes = new ConcurrentHashMap<>();
        heptaCubes = new ConcurrentHashMap<>();
        octaCubes = new ConcurrentHashMap<>();
        nonaCubes = new ConcurrentHashMap<>();
        decaCubes = new ConcurrentHashMap<>();
        undecaCubes = new ConcurrentHashMap<>();
        dodecaCubes = new ConcurrentHashMap<>();
        tridecaCubes = new ConcurrentHashMap<>();
        tetradecaCubes = new ConcurrentHashMap<>();
        pentadecaCubes = new ConcurrentHashMap<>();
        hexadecaCubes = new ConcurrentHashMap<>();
        heptadecaCubes = new ConcurrentHashMap<>();
        octadecaCubes = new ConcurrentHashMap<>();
        nonadecaCubes = new ConcurrentHashMap<>();
        icosiCubes = new ConcurrentHashMap<>();

        Polycube monoCube = new Polycube();
        ArrayList<Polycube> monoCubeList = new ArrayList<>();
        monoCubeList.add(monoCube);
        monoCubes.put(monoCube.hashCode(), monoCubeList);
    }

    public void addIfUnique(Polycube polycube) {
        int key = polycube.hashCode();

        this.getPolycubeMap(polycube.getVolume()).compute(key, (k, polycubeList) -> {
            if (polycubeList == null) {
                polycubeList = new ArrayList<>();
                polycubeList.add(polycube);
            } else {//todo i might be able to do all the rotations here, and after each rotation check it against all polycubes already in the list. that would remove redoing the rotation when comparing the insert poly to the saved polys
                if (polycubeList.stream().noneMatch(polycube::equals)) {
                    polycubeList.add(polycube);
                }
            }
            return polycubeList;
        });
    }

    public List<Polycube> getPolycubes(int n) {
        return this.getPolycubeMap(n).values().stream().flatMap(List::stream).toList();
    }

    private ConcurrentHashMap<Integer, List<Polycube>> getPolycubeMap(int n) {
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
        return new ConcurrentHashMap<>();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        for (int i = 1; i <= 20; i++) {
            List<Polycube> polycubes = this.getPolycubes(i);
            if (!polycubes.isEmpty()) {
                s.append(String.format("%-5s %d\n", "1", polycubes.size()));
            }
        }
        return s.toString();
    }

    public void clearPolyCubes(int n) {
        this.getPolycubeMap(n).clear();
    }
}
