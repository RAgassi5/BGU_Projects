package algorithms.search;


import java.io.Serializable;

public abstract class AState implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int cost;
    protected AState cameFrom;

    protected AState(int cost) {
        this.cost = cost;
    }

    // Returns the cost to enter this state.
    public int getCost() {
        return cost;
    }

    // Sets the parent state in the solution path.
    public void setCameFrom(AState parent) {
        this.cameFrom = parent;
    }

    // Returns the parent's state
    public AState getCameFrom() {
        return cameFrom;
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}
