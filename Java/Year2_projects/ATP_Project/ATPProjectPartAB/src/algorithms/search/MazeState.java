package algorithms.search;

import algorithms.mazeGenerators.Position;
import java.util.Objects;

/**
 * AState implementation for maze positions.
 */
public class MazeState extends AState {
    private final Position position;

    public MazeState(Position position, int cost) {
        super(cost);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MazeState)) return false;
        MazeState other = (MazeState) o;
        return position.getRowIndex() == other.position.getRowIndex()
                && position.getColumnIndex() == other.position.getColumnIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(position.getRowIndex(), position.getColumnIndex());
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
