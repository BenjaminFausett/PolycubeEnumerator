package model;

import java.util.Arrays;

public record Coordinate(int x, int y, int z) {

    @Override
    public String toString() {
        double[] values = {x, y, z};
        Arrays.sort(values);
        return Arrays.toString(values);
    }

    @Override
    public int hashCode() {
        double[] values = {x, y, z};
        Arrays.sort(values);
        return Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinate other)) {
            return false;
        }
        double[] values1 = {x, y, z};
        double[] values2 = {other.x, other.y, other.z};
        Arrays.sort(values1);
        Arrays.sort(values2);
        return Arrays.equals(values1, values2);
    }

}