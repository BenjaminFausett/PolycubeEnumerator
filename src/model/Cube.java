package model;

public class Cube implements Comparable<Cube> {

    private int hashCode;

    public Cube() {
        this.hashCode = 0;
    }

    public Cube(Cube cube) {
        this.hashCode = cube.hashCode;
    }

    public Cube clone() {
        return new Cube(this);
    }

    public void addToDistances(double distance) {
        this.hashCode += Double.hashCode(distance);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
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

}