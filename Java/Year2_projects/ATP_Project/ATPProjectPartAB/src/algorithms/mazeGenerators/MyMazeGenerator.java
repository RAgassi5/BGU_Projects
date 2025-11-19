package algorithms.mazeGenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Maze generator using DFS algorithm.
 * Creates a maze with a path from start to goal.
 */
public class MyMazeGenerator extends AMazeGenerator {

    @Override
    public Maze generate(int rows, int columns) {
        Maze maze = new Maze(rows, columns);

        // Fill with walls
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                maze.addCoordinate(i, j, 1);
            }
        }

        Random rand = new Random();
        // Calculate perimeter
        int perim = 2 * (rows + columns) - 4;

        // Pick random start on perimeter
        int s = rand.nextInt(perim);
        int sRow, sCol;

        // Convert perimeter index to coordinates
        if (s < columns) {
            // Top edge
            sRow = 0; sCol = s;
        } else if (s < columns + rows - 1) {
            // Right edge
            sRow = s - columns + 1; sCol = columns - 1;
        } else if (s < columns + rows - 1 + columns - 1) {
            // Bottom edge
            sRow = rows - 1;
            sCol = columns - 1 - (s - (columns + rows - 1));
        } else {
            // Left edge
            sRow = rows - 1 - (s - (columns + rows - 1 + columns - 1));
            sCol = 0;
        }
        maze.setStartPosition(sRow, sCol);
        maze.updatePositionValue(sRow, sCol, 0);

        //carve maze
        boolean[][] visited = new boolean[rows][columns];
        visited[sRow][sCol] = true;

        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{sRow, sCol});

        // Directions: up, down, left, right (jump 2 cells because of walls)
        int[][] dirs = {{-2,0}, {2,0}, {0,-2}, {0,2}};

        while (!stack.isEmpty()) {
            int[] cur = stack.peek();

            // Find unvisited neighbors
            List<int[]> nbrs = new ArrayList<>();
            for (int[] d : dirs) {
                int nr = cur[0] + d[0], nc = cur[1] + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < columns && !visited[nr][nc]) {
                    nbrs.add(new int[]{nr, nc});
                }
            }

            if (!nbrs.isEmpty()) {
                // Pick a random neighbor
                int[] nxt = nbrs.get(rand.nextInt(nbrs.size()));

                // Carve path
                int wallR = (cur[0] + nxt[0]) / 2;
                int wallC = (cur[1] + nxt[1]) / 2;
                maze.updatePositionValue(wallR, wallC, 0);
                maze.updatePositionValue(nxt[0], nxt[1], 0);

                visited[nxt[0]][nxt[1]] = true;
                stack.push(nxt);
            } else {
                stack.pop();
            }
        }

        // Find goal position on perimeter
        List<int[]> cands = new ArrayList<>();

        // Check top and bottom rows
        for (int c = 0; c < columns; c++) {
            if (visited[0][c] && !(0 == sRow && c == sCol)) 
                cands.add(new int[]{0, c});

            if (visited[rows-1][c] && !(rows-1 == sRow && c == sCol)) 
                cands.add(new int[]{rows-1, c});
        }

        // Check left and right columns
        for (int r = 1; r < rows-1; r++) {
            if (visited[r][0] && !(r == sRow && 0 == sCol)) 
                cands.add(new int[]{r, 0});

            if (visited[r][columns-1] && !(r == sRow && columns-1 == sCol)) 
                cands.add(new int[]{r, columns-1});
        }

        // Set goal position
        int eRow, eCol;

        if (!cands.isEmpty()) {
            int[] pick = cands.get(rand.nextInt(cands.size()));
            eRow = pick[0];
            eCol = pick[1];
        } else {
            eRow = rows - 1;
            eCol = columns - 1;
            maze.updatePositionValue(eRow, eCol, 0);
        }

        maze.setGoalPosition(eRow, eCol);
        maze.updatePositionValue(eRow, eCol, 0);

        return maze;
    }
}
