package model.util;

import model.records.Point;

import java.util.HashMap;


/**
 * The purpose of this class is to create, store, and fetch all needed point objects without ever creating duplicate points. This allows for a lower memory footprint.
 */
public class PointFactory {

    private final HashMap<String, Point> allPoints;

    private PointFactory() {
        this.allPoints = new HashMap<>();
    }

    public static PointFactory getInstance() {
        return Holder.POINT_FACTORY_INSTANCE;
    }

    public Point get(int x, int y, int z) {
        String coordinateKey = x + " " + y + " " + z;
        final Point point = allPoints.get(coordinateKey);

        if (point == null) {
            final Point newPoint = new Point((byte) x, (byte) y, (byte) z);
            allPoints.put(coordinateKey, newPoint);

            return newPoint;
        } else {
            return point;
        }
    }

    private abstract static class Holder {
        private static final PointFactory POINT_FACTORY_INSTANCE = new PointFactory();
    }
}
