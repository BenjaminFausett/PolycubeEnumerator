package model.records;

public class Cube {

    private final Point3D point;

    public Cube(int x, int y, int z) {
        this.point = new Point3D(x, y, z);
    }

    public Cube(Point3D point) {
        this.point = new Point3D(point.x(), point.y(), point.z());
    }

    public Cube(Cube cube) {
        this(cube.point);
    }

    public Cube clone() {
        return new Cube(this);
    }


    public int x() {
        return point.x();
    }

    public int y() {
        return point.y();
    }

    public int z() {
        return point.z();
    }

    public Point3D getPoint() {
        return this.point;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Cube other) {
            return this.x() == other.x() && this.y() == other.y() && this.z() == other.z();
        }
        return false;
    }
}