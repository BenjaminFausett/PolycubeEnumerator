package model;

import java.util.HashMap;

public class Cube implements Comparable<Cube> {

    private final HashMap<Double, Integer> distances;
    private int hashCode;

    public Cube() {
        this.hashCode = 0;
        this.distances = new HashMap<>();
    }

    public Cube(Cube cube) {
        this.hashCode = cube.hashCode;
        this.distances = new HashMap<>(cube.distances);
    }

    public Cube clone() {
        return new Cube(this);
    }

    public void addToDistances(double distance) {
        if (this.distances.containsKey(distance)) {
            this.distances.put(distance, this.distances.get(distance) + 1);
        } else {
            this.distances.put(distance, 1);
        }
        this.hashCode = this.distances.hashCode();
    }

    @Override
    public int hashCode() {
        if(this.hashCode == 0) {
            this.hashCode = this.distances.hashCode();
        }
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cube other)) {
            return false;
        }
        return this.distances.equals(other.distances);
    }

    @Override
    public int compareTo(Cube other) {
        return this.hashCode() - other.hashCode();
    }

    @Override
    public String toString() {
        return this.distances.toString();
    }
}