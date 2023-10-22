package model;

public class Cube implements Comparable<Cube> {

    private int hashCode;
    private int neighborCount;

    public Cube(int neighborCount) {
        this.hashCode = 0;
        this.neighborCount = neighborCount;
    }

    public Cube(Cube cube) {
        this.hashCode = cube.hashCode;
        this.neighborCount = cube.neighborCount;
    }

    public Cube clone() {
        return new Cube(this);
    }

    public void addToDistances(double distance) {
        this.hashCode += Double.hashCode(distance);
    }

    public void incrementNeighborCount() {
        this.neighborCount += 1;
    }

    @Override
    public int hashCode() {
        return (this.hashCode + String.valueOf(neighborCount)).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cube other)) {
            return false;
        }
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int compareTo(Cube other) {
        return this.hashCode() - other.hashCode();
    }

    public int getNeighborCount() {
        return this.neighborCount;
    }
}