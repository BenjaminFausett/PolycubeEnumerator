package model;

public class Coordinate {

    private final double x;
    private final double y;
    private final double z;

    public Coordinate(double x, double y, double z) {
        double factor = Math.pow(10, 4);
        this.x = Math.floor(x * factor) / factor;
        this.y = Math.floor(y * factor) / factor;
        this.z = Math.floor(z * factor) / factor;
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