package model.records;

public record Point3D(int x, int y, int z) {

    @Override
    public int hashCode() {
        return ((x * 31) + (y * 31) + (z * 31)) ^ 3;
    }
}
