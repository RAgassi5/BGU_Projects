package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator {

    /**
     * Generates an empty maze with the specified number of rows and columns. 
     * The start and goal positions are randomly set along the perimeter of the maze.
     * All cells in the maze are initialized with a default value of 0.
     *
     * @param rows    the number of rows in the maze
     * @param columns the number of columns in the maze
     * @return a Maze instance
     */
    @Override
    public Maze generate(int rows, int columns) {
        Maze newMaze = new Maze(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                newMaze.addCoordinate(i, j, 0);
            }
        }
        Position start = randomPerimeter(rows, columns);
        Position goal = randomPerimeterDistinct(rows, columns, start);

        newMaze.setStartPosition(start.getRowIndex(), start.getColumnIndex());
        newMaze.updatePositionValue(start.getRowIndex(), start.getColumnIndex(), 0);
        newMaze.setGoalPosition(goal.getRowIndex(), goal.getColumnIndex());
        newMaze.updatePositionValue(goal.getRowIndex(), goal.getColumnIndex(), 0);
        return newMaze;
    }
}
