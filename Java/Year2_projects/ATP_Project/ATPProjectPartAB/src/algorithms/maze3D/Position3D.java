package algorithms.maze3D;

public class Position3D {
    private final int depth, row, column;

    public Position3D(int depth, int row, int column) {
        this.depth = depth;
        this.row = row;
        this.column = column;
    }

    /**
     * Retrieves the depth index of this Position3D instance.
     *
     * @return the depth index of the current Position3D instance
     */
    public int getDepthIndex()  { return depth; }

    /**
     * Retrieves the row index of this Position3D instance.
     *
     * @return the row index of the current Position3D instance
     */
    public int getRowIndex()    { return row; }

    /**
     * Retrieves the column index of this Position3D instance.
     *
     * @return the column index of the current Position3D instance
     */
    public int getColumnIndex() { return column; }

    /**
     * Compares this Position3D object with the specified object for equality.
     * Two Position3D objects are considered equal if they have the same depth, row, and column values.
     *
     * @param o the object to be compared with this Position3D for equality
     * @return true if the specified object is equal to this Position3D; false otherwise
     */
    @Override public boolean equals(Object o) {
        if (o instanceof Position3D) {
            Position3D p = (Position3D) o;
            return depth == p.depth && row == p.row && column == p.column;
        } else {
            return false;
        }
    }

    /**
     * Computes the hash code for this instance of Position3D.
     * The hash code is calculated based on the depth, row, and column
     *
     * @return an integer representing the hash code of this Position3D instance
     */
    @Override public int hashCode() { return depth*31*31 + row*31 + column; }

    /**
     * Returns a string representation of the Position3D object.
     * The format of the string is "{depth,row,column}" where depth, row, and column
     *
     * @return a string representation of this Position3D instance in the format "{depth,row,column}"
     */
    @Override public String toString() { return "{"+depth+","+row+","+column+"}"; }
}