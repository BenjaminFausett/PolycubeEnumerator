package model.records;

import java.util.ArrayList;

public record CubeMetrics(ArrayList<Distance> distancesFromCubes) implements Comparable<CubeMetrics> {

    @Override
    public String toString() {
        return distancesFromCubes.toString();
    }

    @Override
    public int compareTo(CubeMetrics other) {
        for(int i = 0; i < distancesFromCubes.size(); i++) {
            int compare = this.distancesFromCubes.get(i).compareTo(other.distancesFromCubes.get(i));
            if(compare != 0) {
                return compare;
            }
        }
        return 0;
    }
}
