package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that makes Maze work with ISearchable.
 */
public class SearchableMaze implements ISearchable {
    private final Maze maze;
    private final int rows;
    private final int columns;

    public SearchableMaze(Maze maze) {
        this.maze = maze;
        this.rows = maze.getRowsSize();
        this.columns = maze.getColumnsSize();
    }

    @Override
    public AState getStartingState() {
        return new MazeState(maze.getStartPosition(), 0);
    }

    @Override
    public AState getGoalState() {
        return new MazeState(maze.getGoalPosition(), 0);
    }

    @Override
    public List<AState> getAllPossibleStates(AState state) {
        MazeState ms = (MazeState) state;
        List<AState> neighbors = new ArrayList<>();
        int r = ms.getPosition().getRowIndex();
        int c = ms.getPosition().getColumnIndex();
        int[][] deltas = {{-1,0},{1,0},{0,-1},{0,1},{-1,-1},{-1,1},{1,-1},{1,1}};
        for (int[] d : deltas) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < columns) {
                if (maze.getValue(nr, nc) == 0) {
                    boolean diagonal = Math.abs(d[0]) + Math.abs(d[1]) == 2;
                    if (!diagonal || (maze.getValue(r + d[0], c) == 0 && maze.getValue(r, c + d[1]) == 0)) {
                        int moveCost = diagonal ? 15 : 10;
                        neighbors.add(new MazeState(new Position(nr, nc, 0), moveCost));
                    }
                }
            }
        }
        return neighbors;
    }
}
