package model;


import model.records.Point;

import java.io.Serializable;

public class Cube implements Comparable<Cube>, Serializable {

    private final Point point;
    private short euclideanDistancesSum;
    private short manhattanDistancesSum;
    private int hashCode;

    public Cube(int x, int y, int z) {
        this.point = new Point((short) x, (short) y, (short) z);
        this.euclideanDistancesSum = 0;
        this.manhattanDistancesSum = 0;
        this.hashCode = 0;
    }

    public Cube(Point point) {
        this(point.x(), point.y(), point.z());
    }

    public Cube(Cube cube) {
        this.point = new Point(cube.point.x(), cube.point.y(), cube.point.z());
        this.euclideanDistancesSum = cube.euclideanDistancesSum;
        this.manhattanDistancesSum = cube.manhattanDistancesSum;
        this.hashCode = cube.hashCode;
    }

    public Cube clone() {
        return new Cube(this);
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = (String.valueOf(manhattanDistancesSum) + euclideanDistancesSum).hashCode();
        }
        return this.hashCode;
    }

    public void addManhattanDistance(int distance) {
        this.hashCode = 0;
        this.manhattanDistancesSum += distance;
    }

    public void addEuclideanDistancesSum(int distance) {
        this.hashCode = 0;
        this.euclideanDistancesSum += distance;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public int compareTo(Cube other) {
        return this.hashCode() - other.hashCode();
    }

    @Override
    public String toString() {
        String cube = "";

        cube += "Point: " + point.toString();
        cube += "\nManhattan Sum: " + this.manhattanDistancesSum;
        cube += "\nEuclidean Sum: " + this.euclideanDistancesSum;
        cube += "\nHashcode: " + this.hashCode;

        return cube;
    }
}
