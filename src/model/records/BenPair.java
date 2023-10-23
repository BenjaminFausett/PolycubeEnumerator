package model.records;

public record BenPair(int a, int b) {

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BenPair other)) {
            return false;
        }

        return (this.a == other.a && this.b == other.b) || (this.a == other.b && this.b == other.a);
    }

    @Override
    public int hashCode() {
        return this.a * this.b;
    }
}
