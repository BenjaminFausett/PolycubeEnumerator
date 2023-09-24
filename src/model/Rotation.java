package model;

public record Rotation(int xRotations, int yRotations, int zRotations) {

    private static final Rotation[] ALL_ROTATIONS = {
            new Rotation(0, 0, 0), new Rotation(0, 1, 0), new Rotation(0, 2, 0), new Rotation(0, 3, 0),
            new Rotation(1, 0, 0), new Rotation(1, 1, 0), new Rotation(1, 2, 0), new Rotation(1, 3, 0),
            new Rotation(2, 0, 0), new Rotation(2, 1, 0), new Rotation(2, 2, 0), new Rotation(2, 3, 0),
            new Rotation(1, 0, 1), new Rotation(1, 1, 1), new Rotation(1, 2, 1), new Rotation(1, 3, 1),
            new Rotation(3, 0, 1), new Rotation(3, 1, 1), new Rotation(3, 2, 1), new Rotation(3, 3, 1),
            new Rotation(1, 0, 3), new Rotation(1, 1, 3), new Rotation(1, 2, 3), new Rotation(1, 3, 3)
    };

    public Rotation(int xRotations, int yRotations, int zRotations) {
        this.xRotations = xRotations % 4;
        this.yRotations = yRotations % 4;
        this.zRotations = zRotations % 4;
    }

    public static Rotation[] getAllRotations() {
        return ALL_ROTATIONS;
    }
}
