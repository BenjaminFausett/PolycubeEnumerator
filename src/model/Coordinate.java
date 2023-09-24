package model;

public class Coordinate {

    private int x;
    private int y;
    private int z;

    public Coordinate(int x, int y, int z, Rotation rotation, int cubeLength) {
        this.x = x;
        this.y = y;
        this.z = z;

        transformCoordinates(rotation, cubeLength);
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
        for (int i = 0; i < rotation.xRotations() % 4; i++) {
            temp = newY;
            newY = cubeLength - 1 - newZ;
            newZ = temp;
        }

        // Apply rotations around the Y-axis
        for (int i = 0; i < rotation.yRotations() % 4; i++) {
            temp = newX;
            newX = newZ;
            newZ = cubeLength - 1 - temp;
        }

        // Apply rotations around the Z-axis
        for (int i = 0; i < rotation.zRotations() % 4; i++) {
            temp = newX;
            newX = cubeLength - 1 - newY;
            newY = temp;
        }

        this.x = newX;
        this.y = newY;
        this.z = newZ;
    }

}