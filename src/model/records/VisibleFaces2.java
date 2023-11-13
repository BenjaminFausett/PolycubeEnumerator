package model.records;

public record VisibleFaces2(int layer, int faces, int maxGrouping) implements Comparable<VisibleFaces2> {

    @Override
    public String toString() {
        return faces + "" + maxGrouping;
    }

    @Override
    public int compareTo(VisibleFaces2 other) {
        if(this.faces != other.faces) {
            return this.faces - other.faces;
        }
        return this.maxGrouping - other.maxGrouping;
    }
}
