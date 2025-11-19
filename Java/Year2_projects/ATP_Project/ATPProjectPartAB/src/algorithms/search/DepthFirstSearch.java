package algorithms.search;

import java.util.*;

/**
 * Implements depth-first search (DFS).
 */
public class DepthFirstSearch extends ASearchingAlgorithm {
    @Override
    public Solution solve(ISearchable domain) {
        nodesEvaluated = 0;
        Stack<AState> open = new Stack<>();
        Map<AState, AState> parentMap = new HashMap<>();
        Set<AState> closed = new HashSet<>();

        AState start = domain.getStartingState();
        AState goal = domain.getGoalState();
        open.push(start);
        closed.add(start);

        while (!open.isEmpty()) {
            AState current = open.pop();
            nodesEvaluated++;
            if (current.equals(goal)) {
                return buildSolution(current, parentMap);
            }
            for (AState succ : domain.getAllPossibleStates(current)) {
                if (!closed.contains(succ)) {
                    closed.add(succ);
                    parentMap.put(succ, current);
                    open.push(succ);
                }
            }
        }
        return new Solution(Collections.emptyList());
    }

    private Solution buildSolution(AState state, Map<AState, AState> parentMap) {
        LinkedList<AState> path = new LinkedList<>();
        AState curr = state;
        while (curr != null) {
            path.addFirst(curr);
            curr = parentMap.get(curr);
        }
        return new Solution(path);
    }

    @Override
    public String getName() {
        return "DepthFirstSearch";
    }
}