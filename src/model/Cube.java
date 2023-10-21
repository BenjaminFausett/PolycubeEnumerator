package model;

public class Cube implements Comparable<Cube> {

    private int hashCode;
    private int neighbors;

    public Cube(int neighbors) {
        this.neighbors = neighbors;
        this.hashCode = 0;
    }

    public Cube(Cube cube) {
        this.hashCode = cube.hashCode;
        this.neighbors = cube.neighbors;
    }

    public Cube clone() {
        return new Cube(this);
    }

    public void addToDistances(double distance) {
        this.hashCode += Double.hashCode(distance);
    }

    public void incrementNeighborCount() {
        this.neighbors += 1;
    }

    @Override
    public int hashCode() {
        return this.hashCode + (neighbors * 31);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cube other)) {
            return false;
        }
        return this.hashCode() == other.hashCode() && this.neighbors == other.neighbors;
    }

    @Override
    public int compareTo(Cube other) {
        return this.hashCode() - other.hashCode();
    }

    public int getNeighborCounts() {
        return this.neighbors;
    }
}