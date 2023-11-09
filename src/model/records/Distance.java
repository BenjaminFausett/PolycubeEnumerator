package model.records;

public record Distance(int manhattanDistance, int euclideanDistance) implements Comparable<Distance> {

    @Override
    public String toString() {
        return manhattanDistance + " " + euclideanDistance;
    }

    @Override
    public int compareTo(Distance other) {
        if (this.manhattanDistance != other.manhattanDistance) {
            return this.manhattanDistance - other.manhattanDistance;
        }
        return this.euclideanDistance - other.euclideanDistance;
    }

    @Override
    public int hashCode() {
        return (manhattanDistance + euclideanDistance) + (manhattanDistance * euclideanDistance);
    }
}
