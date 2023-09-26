package model;

public class Coordinate {

    private final double x;
    private final double y;
    private final double z;

    public Coordinate(double x, double y, double z) {
        this.x = (double)Math.round(x * 100000d) / 100000d;
        this.y = (double)Math.round(y * 100000d) / 100000d;
        this.z = (double)Math.round(z * 100000d) / 100000d;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public String toString() {
        return "Coordinate(" + x + " " + y + " " + z + ")";
    }

    @Override
    public int hashCode() {
        return ("" + x + y + z).hashCode();
    }

}