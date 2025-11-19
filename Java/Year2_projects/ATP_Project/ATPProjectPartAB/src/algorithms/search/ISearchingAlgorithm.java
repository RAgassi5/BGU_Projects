package algorithms.search;

/**
 * Defines a generic search algorithm interface.
 */
public interface ISearchingAlgorithm {
    Solution solve(ISearchable domain);
    int getNumberOfNodesEvaluated();
    String getName();
}