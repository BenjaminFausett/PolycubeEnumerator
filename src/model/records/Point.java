package model.records;

import java.io.Serializable;

public record Point(int x, int y, int z) implements Serializable {
}