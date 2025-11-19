package algorithms.maze3D;

import algorithms.search.*;

import java.util.ArrayList;
import java.util.List;

public class SearchableMaze3D implements ISearchable {
    private final Maze3D maze;
    private final int depth;
    private final int rows;
    private final int columns;

    /**
     * Constructs a SearchableMaze3D object using the given 3D maze.
     * Initializes the dimensions (depth, rows, and columns) of the maze.
     *
     * @param m the 3D maze to be used for creating the SearchableMaze3D instance
     */
    public SearchableMaze3D(Maze3D m){ maze=m; depth =m.getStructure().length;
        rows =m.getStructure()[0].length; columns =m.getStructure()[0][0].length; }

    /**
     * Retrieves the starting state of the maze.
     *
     * @return the starting state of the maze as an AState object
     */
    @Override public AState getStartingState(){
        Position3D p=maze.getStartPosition();
        return new Maze3DState(p,0);
    }

    /**
     * Retrieves the goal state of the maze as an AState object.
     *
     * @return the goal state of the maze as an AState object
     */
    @Override public AState getGoalState(){
        Position3D p=maze.getGoalPosition();
        return new Maze3DState(p,0);
    }

    /**
     * Retrieves all possible states that can be reached from the given state in the 3D maze.
     * Valid states are determined by checking adjacent positions in all six possible directions
     * (up, down, left, right, forward, backward) within the maze boundaries. A position is
     * considered reachable if it is within the bounds of the maze and not a wall (value 0).
     *
     * @param currentState the current state from which to calculate all possible reachable states
     * @return a list of all valid adjacent states that can be reached from the given state
     */
    @Override public List<AState> getAllPossibleStates(AState currentState){
        Position3D p=((Maze3DState)currentState).getPosition();
        int[][] dir={{1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}};
        List<AState> res=new ArrayList<>();
        for(int[] d:dir){
            int nd=p.getDepthIndex()+d[0], nr=p.getRowIndex()+d[1], nc=p.getColumnIndex()+d[2];
            if((nd >= 0) && (nd < depth) && (nr >= 0) && (nr < rows) && (nc >= 0) && (nc < columns) && (maze.get(nd, nr, nc) == 0)){
                res.add(new Maze3DState(new Position3D(nd,nr,nc),10));
            }
        }
        return res;
    }
}
