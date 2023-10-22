package model;

import java.io.Serializable;

public class Cube implements Comparable<Cube>, Serializable {

    private int hashCode;

    private int euclideanDistancesSum;
    private int manhattanDistancesSum;

    public Cube() {
        this.hashCode = 0;
        this.euclideanDistancesSum = 0;
        this.manhattanDistancesSum = 0;
    }

    public Cube(Cube cube) {
        this.hashCode = cube.hashCode;
        this.euclideanDistancesSum = cube.euclideanDistancesSum;
        this.manhattanDistancesSum = cube.manhattanDistancesSum;
    }

    public Cube clone() {
        return new Cube(this);
    }

    public void addDistances(double euclideanDistance, int manhattanDistance) {
        this.hashCode = 0;
        euclideanDistancesSum += Double.hashCode(euclideanDistance);
        manhattanDistancesSum += manhattanDistance;
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = (String.valueOf(manhattanDistancesSum) + euclideanDistancesSum).hashCode();
        }
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cube other)) {
            return false;
        }
        return euclideanDistancesSum == other.euclideanDistancesSum && manhattanDistancesSum == other.manhattanDistancesSum;
    }

    @Override
    public int compareTo(Cube other) {
        return this.hashCode() - other.hashCode();
    }
}