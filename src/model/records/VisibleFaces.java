package model.records;

public record VisibleFaces(int layer, int faces) implements Comparable<VisibleFaces> {

    @Override
    public String toString() {
        return String.valueOf(faces);
    }

    @Override
    public int compareTo(VisibleFaces other) {
        return this.faces - other.faces;
    }
}
