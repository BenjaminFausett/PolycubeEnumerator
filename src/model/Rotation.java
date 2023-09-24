package model;

public record Rotation(int xRotations, int yRotations, int zRotations) {

    public Rotation(int xRotations, int yRotations, int zRotations) {
        this.xRotations = xRotations % 4;
        this.yRotations = yRotations % 4;
        this.zRotations = zRotations % 4;
    }
}
