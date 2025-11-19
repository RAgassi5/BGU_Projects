package algorithms.mazeGenerators;

import java.io.*;

/**
 * The 2D maze itself - our main data structure.
 * 
 * Stores the grid of cells (0=path, 1=wall), plus the special
 * start and goal positions. Think of it as the "model" in MVC -
 * just holds the data, while other classes handle generation
 * and solving.
 */
public class Maze implements Serializable{
    private static final long serialVersionUID = 1L;

    private int rowsSize;

    private int columnsSize;

    private Position[][] structure;

    private Position startPosition;

    private Position goalPosition;

    /**
     * Creates a new empty maze with the given dimensions.
     * 
     * Note: This just initializes the structure - it doesn't
     * actually create paths or walls. That's handled by the
     * generator algorithms.
     *
     * @param rows    height of the maze
     * @param columns width of the maze
     */
    public Maze(int rows, int columns) {
        this.rowsSize = rows;
        this.columnsSize = columns;
        this.structure = new Position[rows][columns];
    }


    /**
     * @return maze height (number of rows)
     */
    public int getRowsSize() {
        return this.rowsSize;
    }

    /**
     * @return maze width (number of columns)
     */
    public int getColumnsSize() {
        return this.columnsSize;
    }

    /**
     * Displays the maze in the console.
     * 
     * Uses 'S' for start position, 'E' for end/goal position,
     * and the cell values (0/1) for all other positions.
     * Helpful for debugging and visualizing small mazes.
     */
    public void print() {
        System.out.println("Current Maze Layout:");
        for (int i = 0; i < rowsSize; i++) {
            for (int j = 0; j < columnsSize; j++) {
                if (i == startPosition.getRowIndex() && j == startPosition.getColumnIndex()) {
                    System.out.print("S" + " ");
                } else if (i == goalPosition.getRowIndex() && j == goalPosition.getColumnIndex()) {
                    System.out.print("E ");
                } else {
                    System.out.print(structure[i][j].getValue() + " ");

                }

            }
            System.out.println();
        }
    }

    /**
     * Updates a cell in the maze - creates it if needed.
     * 
     * This is a bit smarter than addCoordinate() since it checks if
     * the position already exists and just updates the value if it does.
     * Used during maze generation to carve paths.
     *
     * @param row    cell row
     * @param column cell column
     * @param value  new value (0=path, 1=wall)
     */
    public void updatePositionValue(int row, int column, int value) {
        if (structure[row][column] == null)
            structure[row][column] = new Position(row, column, value);
        else structure[row][column].setValue(value);

    }

    /**
     * Creates a new cell at the specified location.
     * 
     * Unlike updatePositionValue(), this always creates a new Position
     * without checking if one already exists. Slightly faster when
     * initializing the maze from scratch.
     *
     * @param row    cell row
     * @param column cell column
     * @param value  cell value (0=path, 1=wall)
     */
    public void addCoordinate(int row, int column, int value) {
        structure[row][column] = new Position(row, column, value);
    }

    /**
     * Gets the value at a specific cell.
     * 
     * Quick way to check if a cell is a wall (1) or path (0)
     * without having to get the whole Position object.
     *
     * @param row    cell row
     * @param column cell column
     * @return 0 for path, 1 for wall
     */
    public int getValue(int row, int column) {
        return structure[row][column].getValue();
    }

    // Special position getters/setters

    /**
     * @return starting position (where search algorithms begin)
     */
    public Position getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return goal position (where we're trying to reach)
     */
    public Position getGoalPosition() {
        return this.goalPosition;
    }

    /**
     * Sets the maze entry point.
     * Note: Always creates the position with value 0 (path)
     * since you can't start on a wall!
     *
     * @param row    start row
     * @param column start column
     */
    public void setStartPosition(int row, int column) {
        this.startPosition = new Position(row, column, 0);
    }

    /**
     * Sets the maze exit point.
     * Also ensures it's a path (value 0) so the maze is solvable.
     *
     * @param row    goal row
     * @param column goal column
     */
    public void setGoalPosition(int row, int column) {
        this.goalPosition = new Position(row, column, 0);
    }
    public Maze(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bais);

        // read dimensions
        this.rowsSize = dis.readInt();
        this.columnsSize = dis.readInt();
        this.structure = new Position[rowsSize][columnsSize];
        // read start/goal
        int sr = dis.readInt();
        int sc = dis.readInt();
        int gr = dis.readInt();
        int gc = dis.readInt();
        this.startPosition = new Position(sr, sc, 0);
        this.goalPosition = new Position(gr, gc, 0);
        // read grid
        for (int i = 0; i < rowsSize; i++) {
            for (int j = 0; j < columnsSize; j++) {
                int v = dis.readByte();
                structure[i][j] = new Position(i, j, v);
            }
        }
    }
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream currentBytes = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(currentBytes);

        // dimensions
        dos.writeInt(rowsSize);
        dos.writeInt(columnsSize);
        // start/goal positions
        dos.writeInt(startPosition.getRowIndex());
        dos.writeInt(startPosition.getColumnIndex());
        dos.writeInt(goalPosition.getRowIndex());
        dos.writeInt(goalPosition.getColumnIndex());
        // grid values
        for (int i = 0; i < rowsSize; i++) {
            for (int j = 0; j < columnsSize; j++) {
                int val = (structure[i][j] != null ? structure[i][j].getValue() : 1);
                dos.writeByte(val);
            }
        }
        dos.flush();
        return currentBytes.toByteArray();
    }
}
