package model.records;

import java.io.Serializable;

public record Point(short x, short y, short z) implements Serializable {
}