package model;

import model.records.Point2D;

import java.util.ArrayList;

public class ViewableFacesLayer {

    private final ArrayList<Point2D> visibleFaces;
    private int maxA = Integer.MIN_VALUE;
    private int maxB = Integer.MIN_VALUE;
    private int minA = Integer.MAX_VALUE;
    private int minB = Integer.MAX_VALUE;

    private static final int[] da = {0, 1, 0, -1};
    private static final int[] db = {1, 0, -1, 0};

    public ViewableFacesLayer() {
        visibleFaces = new ArrayList<>();
    }

    public void addVisibleFace(int a, int b) {
        maxA = Math.max(maxA, a);
        maxB = Math.max(maxB, b);
        minA = Math.min(minA, a);
        minB = Math.min(minB, b);
        visibleFaces.add(new Point2D(a, b));
    }

    public int getCount() {
        return this.visibleFaces.size();
    }

    public int getLargestConnectedGroupSize() {
        int aOffset = 0;
        int bOffset = 0;
        if(minA < 0) {
            aOffset += Math.abs(minA);
        } else {
            aOffset -= minA;
        }
        if(minB < 0) {
            bOffset += Math.abs(minB);
        } else {
            bOffset -= minB;
        }

        boolean[][] grid = new boolean[maxA + aOffset + 1][maxB + bOffset + 1];
        for (Point2D p : visibleFaces) {
            grid[p.a() + aOffset][p.b() + bOffset] = true;
        }

        boolean[][] visited = new boolean[maxA + aOffset + 1][maxB + bOffset + 1];
        int largestGroup = 0;

        for (int a = 0; a <= maxA + aOffset; a++) {
            for (int b = 0; b <= maxB + bOffset; b++) {
                if (grid[a][b] && !visited[a][b]) {
                    largestGroup = Math.max(largestGroup, dfs(grid, visited, a, b));
                }
            }
        }

        return largestGroup;
    }

    private static int dfs(boolean[][] grid, boolean[][] visited, int a, int b) {
        visited[a][b] = true;
        int count = 1;

        for (int i = 0; i < 4; i++) {
            int na = a + da[i];
            int nb = b + db[i];
            if (na >= 0 && nb >= 0 && na < grid.length && nb < grid[0].length && grid[na][nb] && !visited[na][nb]) {
                count += dfs(grid, visited, na, nb);
            }
        }
        return count;
    }

}