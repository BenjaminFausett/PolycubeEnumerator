package model;

import model.records.Point;

public class Cube {

    private final byte x;
    private final byte y;
    private final byte z;
    private int manhattanDistancesSum;
    private int euclideanDistancesSum;
    private int hashCode;

    public Cube() {
        this.x = 64;
        this.y = 64;
        this.z = 64;
    }

    public Cube(byte x, byte y, byte z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.manhattanDistancesSum = 0;
        this.euclideanDistancesSum = 0;
        this.hashCode = 0;
    }

    public Cube(Point point) {
        this((byte) point.x(), (byte) point.y(), (byte) point.z());
    }

    public Cube(Cube cube) {
        this.x = cube.x;
        this.y = cube.y;
        this.z = cube.z;
        this.manhattanDistancesSum = cube.manhattanDistancesSum;
        this.euclideanDistancesSum = cube.euclideanDistancesSum;
        this.hashCode = cube.hashCode;
    }

    public Cube clone() {
        return new Cube(this);
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            return (String.valueOf(manhattanDistancesSum) + euclideanDistancesSum).hashCode();
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

    public byte x() {
        return x;
    }

    public byte y() {
        return y;
    }

    public byte z() {
        return z;
    }

    @Override
    public String toString() {//for debugging
        String cube = "";

        cube += "Manhattan Sum: " + this.manhattanDistancesSum;
        cube += "\nEuclidean Sum: " + this.euclideanDistancesSum;
        cube += "\nHashcode: " + this.hashCode;

        return cube;
    }
}
