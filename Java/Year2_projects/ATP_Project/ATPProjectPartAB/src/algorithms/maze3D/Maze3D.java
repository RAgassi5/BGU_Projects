package algorithms.maze3D;

public class Maze3D {
    private final int depthSize, rowsSize, columnsSize;
    private final int[][][] structure;           // 0-path, 1-wall
    private Position3D start, goal;

    public Maze3D(int depth, int rows, int cols) {
        this.depthSize = depth;
        this.rowsSize = rows;
        this.columnsSize = cols;
        structure = new int[depth][rows][cols];
    }

    /**
     * Retrieves the 3D structure of the maze..
     *
     * @return a 3D integer array representing the maze structure
     */
    public int[][][] getStructure() {
        return structure;
    }

    /**
     * Retrieves the starting position of the 3D maze.
     *
     * @return a Position3D object representing the starting position of the maze
     */
    public Position3D getStartPosition() {
        return start;
    }

    /**
     * Retrieves the goal position of the 3D maze.
     *
     * @return a Position3D object representing the goal position in the maze
     */
    public Position3D getGoalPosition() {
        return goal;
    }

    /**
     * Sets the starting position of the 3D maze.
     * Updates the starting position with the specified depth, row, and column values.
     *
     * @param depth   the depth index of the starting position
     * @param rows    the row index of the starting position
     * @param columns the column index of the starting position
     */
    public void setStartPosition(int depth, int rows, int columns) {
        start = new Position3D(depth, rows, columns);
    }

    /**
     * Sets the goal position of the 3D maze.
     * Updates the goal position with the specified depth, row, and column values.
     *
     * @param depth   the depth index of the goal position
     * @param rows    the row index of the goal position
     * @param columns the column index of the goal position
     */
    public void setGoalPosition(int depth, int rows, int columns) {
        goal = new Position3D(depth, rows, columns);
    }

    /**
     * Sets a specific value in the 3D maze structure at the given depth, row, and column indices.
     *
     * @param depth   the depth index where the value is to be set
     * @param rows    the row index where the value is to be set
     * @param columns the column index where the value is to be set
     * @param val     the value to set at the specified position in the maze
     */
    public void set(int depth, int rows, int columns, int val) {
        structure[depth][rows][columns] = val;
    }

    /**
     * Retrieves the value at the specified depth, row, and column indices
     * from the 3D maze structure.
     *
     * @param depth   the depth index of the value to retrieve
     * @param rows    the row index of the value to retrieve
     * @param columns the column index of the value to retrieve
     * @return the value at the specified position in the maze structure
     */
    public int get(int depth, int rows, int columns) {
        return structure[depth][rows][columns];
    }

    /**
     * Prints a 3D representation of the maze layer-by-layer to the console.
     */
    public void print() {
        for (int d = 0; d < depthSize; d++) {
            System.out.println("Layer " + d + ":");
            for (int r = 0; r < rowsSize; r++) {
                for (int c = 0; c < columnsSize; c++) {
                    if (start.getDepthIndex() == d && start.getRowIndex() == r && start.getColumnIndex() == c)
                        System.out.print("S ");
                    else if (goal.getDepthIndex() == d && goal.getRowIndex() == r && goal.getColumnIndex() == c)
                        System.out.print("E ");
                    else System.out.print(structure[d][r][c] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
