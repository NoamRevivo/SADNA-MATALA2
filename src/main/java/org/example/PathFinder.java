package org.example;
import java.awt.Point;
import java.util.*;

public class PathFinder
{
    //קבועים
    private static final int[] ROW_DIRECTIONS = {-1, 1, 0, 0};
    private static final int[] COL_DIRECTIONS = {0, 0, 1, -1};
    private static final int NUM_DIRECTIONS = 4;
    //
    private MazeModel maze;

    public PathFinder(MazeModel maze) {
        this.maze = maze;
    }

    public List<Point> findSolution() {
        int width = maze.getWidth();
        int height = maze.getHeight();

        Point startPoint = new Point(0, 0);
        Point endPoint = new Point(width - 1, height - 1);
        if (maze.isWall(startPoint.x, startPoint.y) || maze.isWall(endPoint.x, endPoint.y)) {
            return Collections.emptyList();
        }
        boolean[][] visited = new boolean[height][width];
        Point[][] parentMap = new Point[height][width];
        Queue<Point> queue = new LinkedList<>();

        queue.add(startPoint);
        visited[startPoint.y][startPoint.x] = true;

        boolean isSolutionFound = false;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(endPoint)) {
                isSolutionFound = true;
                break;
            }
            for (int i = 0; i < NUM_DIRECTIONS; i++) {
                int nextX = current.x + COL_DIRECTIONS[i];
                int nextY = current.y + ROW_DIRECTIONS[i];

                if (!maze.isWall(nextX, nextY) && !visited[nextY][nextX]) {
                    visited[nextY][nextX] = true;
                    parentMap[nextY][nextX] = current;
                    queue.add(new Point(nextX, nextY));
                }
            }
        }
        if (!isSolutionFound) {
            return Collections.emptyList();
        }
        List<Point> finalPath = new ArrayList<>();
        Point step = endPoint;
        while (step != null) {
            finalPath.add(step);
            step = parentMap[step.y][step.x];
        }
        Collections.reverse(finalPath);
        return finalPath;
    }
}