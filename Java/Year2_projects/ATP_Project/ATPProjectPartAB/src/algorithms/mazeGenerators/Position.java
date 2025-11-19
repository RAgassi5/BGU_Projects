package algorithms.mazeGenerators;

import java.io.Serializable;

public class Position implements Serializable {
    private static final long serialVersionUID = 1L;
    private int row;
    private int column;
    private int value;

    public Position(int row, int column, int value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }


    public int getRowIndex() {
        return this.row;
    }

    public int getColumnIndex() {
        return this.column;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" + row + "," + column + "}";
    }

}
