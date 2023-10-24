package model;


import config.Config;
import model.records.Point;

import java.io.Serializable;

public class Cube implements Comparable<Cube>, Serializable {

    private final byte x;
    private final byte y;
    private final byte z;
    private int euclideanDistancesSum;
    private int manhattanDistancesSum;
    private int hashCode;

    public Cube(byte x, byte y, byte z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.euclideanDistancesSum = 0;
        this.manhattanDistancesSum = 0;
        this.hashCode = 0;
    }

    public Cube(Point point) {
        this(point.x(), point.y(), point.z());
    }

    public Cube(Cube cube) {
        this.x = cube.x;
        this.y = cube.y;
        this.z = cube.z;
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
    public int compareTo(Cube other) {
        return this.hashCode() - other.hashCode();
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
