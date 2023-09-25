package model;

public class Coordinate {

    private int x;
    private int y;
    private int z;

    public Coordinate(int x, int y, int z, Rotation rotation, int cubeLength) {
        int temp;

        // Apply rotations around the X-axis
        for (int i = 0; i < rotation.xRotations(); i++) {
            temp = y;
            y = cubeLength - 1 - z;
            z = temp;
        }

        // Apply rotations around the Y-axis
        for (int i = 0; i < rotation.yRotations(); i++) {
            temp = x;
            x = z;
            z = cubeLength - 1 - temp;
        }

        // Apply rotations around the Z-axis
        for (int i = 0; i < rotation.zRotations(); i++) {
            temp = x;
            x = cubeLength - 1 - y;
            y = temp;
        }

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    private void transformCoordinates(Rotation rotation, int cubeLength) {
        int newX = this.x, newY = this.y, newZ = this.z;
        int temp;

        // Apply rotations around the X-axis
        for (int i = 0; i < rotation.xRotations(); i++) {
            temp = newY;
            newY = cubeLength - 1 - newZ;
            newZ = temp;
        }

        // Apply rotations around the Y-axis
        for (int i = 0; i < rotation.yRotations(); i++) {
            temp = newX;
            newX = newZ;
            newZ = cubeLength - 1 - temp;
        }

        // Apply rotations around the Z-axis
        for (int i = 0; i < rotation.zRotations(); i++) {
            temp = newX;
            newX = cubeLength - 1 - newY;
            newY = temp;
        }

        this.x = newX;
        this.y = newY;
        this.z = newZ;
    }

    public String toString() {
        return "Coordinate(" + x + " " + y + " " + z + ")";
    }

}