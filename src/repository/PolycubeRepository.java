package repository;

import model.Polycube;
import model.util.PolycubeComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PolycubeRepository {

    private final ConcurrentHashMap<Long, List<Polycube>> monoCubes;    //1
    private final ConcurrentHashMap<Long, List<Polycube>> diCubes;      //2
    private final ConcurrentHashMap<Long, List<Polycube>> triCubes;     //3
    private final ConcurrentHashMap<Long, List<Polycube>> tetraCubes;   //4
    private final ConcurrentHashMap<Long, List<Polycube>> pentaCubes;   //5
    private final ConcurrentHashMap<Long, List<Polycube>> hexaCubes;    //6
    private final ConcurrentHashMap<Long, List<Polycube>> heptaCubes;   //7
    private final ConcurrentHashMap<Long, List<Polycube>> octaCubes;    //8
    private final ConcurrentHashMap<Long, List<Polycube>> nonaCubes;    //9
    private final ConcurrentHashMap<Long, List<Polycube>> decaCubes;    //10
    private final ConcurrentHashMap<Long, List<Polycube>> undecaCubes;  //11
    private final ConcurrentHashMap<Long, List<Polycube>> dodecaCubes;  //12
    private final ConcurrentHashMap<Long, List<Polycube>> tridecaCubes; //13
    private final ConcurrentHashMap<Long, List<Polycube>> tetradecaCubes;//14
    private final ConcurrentHashMap<Long, List<Polycube>> pentadecaCubes; //15
    private final ConcurrentHashMap<Long, List<Polycube>> hexadecaCubes;  //16
    private final ConcurrentHashMap<Long, List<Polycube>> heptadecaCubes; //17
    private final ConcurrentHashMap<Long, List<Polycube>> octadecaCubes;  //18
    private final ConcurrentHashMap<Long, List<Polycube>> nonadecaCubes;  //19
    private final ConcurrentHashMap<Long, List<Polycube>> icosiCubes;     //20

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
        monoCubes.put(monoCube.longHashCode(), monoCubeList);
    }

    public void addIfUnique(Polycube candidatePolycube) {
        long key = candidatePolycube.longHashCode();

        this.getPolycubeMap(candidatePolycube.getVolume()).compute(key, (k, polycubeList) -> {
            if (polycubeList == null) {
                polycubeList = new ArrayList<>();
                polycubeList.add(candidatePolycube);
            } else {
                if (!PolycubeComparator.equalsAny(candidatePolycube, polycubeList)) {
                    polycubeList.add(candidatePolycube);
                }
            }
            return polycubeList;
        });
    }

    public List<Polycube> getPolycubes(int n) {
        return this.getPolycubeMap(n).values().stream().flatMap(List::stream).toList();
    }

    public ConcurrentHashMap<Long, List<Polycube>> getPolycubeMap(int n) {
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

    public void clearPolyCubes(int n) {
        this.getPolycubeMap(n).clear();
    }

    @Override
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
}
