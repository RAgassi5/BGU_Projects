package algorithms.search;


 // Abstract class tracking nodes count.

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {
    protected int nodesEvaluated = 0;

    @Override
    public int getNumberOfNodesEvaluated() {
        return nodesEvaluated;
    }
}