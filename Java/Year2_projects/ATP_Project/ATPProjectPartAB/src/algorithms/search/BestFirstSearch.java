package algorithms.search;

import algorithms.maze3D.SearchableMaze3D;

import java.util.*;

/**
 * This class solves the maze with Best-First Search.
 * The algorithm works by:
 * 1. Maintaining a queue of states
 * 2. Always going for the state with the lowest cost first
 * 3. For each state, checking all possible next states
 * 4. Adding states to the queue if they're new or have a better path
 */
public class BestFirstSearch extends BreadthFirstSearch {

    @Override
    public Solution solve(ISearchable domain) {
        if( !(domain instanceof SearchableMaze) && !(domain instanceof SearchableMaze3D)){
            throw new IllegalArgumentException("BestFirstSearch requires a SearchableMaze");
        }
        nodesEvaluated = 0;
        // Priority queue sorts states by cost - always get cheapest first
        PriorityQueue<AState> open = new PriorityQueue<>(Comparator.comparingInt(AState::getCost));
        Map<AState, AState> parentMap = new HashMap<>();  // Tracks path for solution
        Map<AState, Integer> costMap = new HashMap<>();   // Tracks best cost to each state

        // Initializing
        AState start = domain.getStartingState();
        AState goal = domain.getGoalState();
        open.add(start);
        costMap.put(start, 0);

        // Main search loop
        while (!open.isEmpty()) {
            // Get the lowest-cost state to explore next
            AState current = open.poll();
            nodesEvaluated++;

            // Found the goal? We're done!
            if (current.equals(goal)) {
                return buildSolution(current, parentMap);
            }

            // Check all possible next states
            for (AState succ : domain.getAllPossibleStates(current)) {
                int newCost = costMap.get(current) + succ.getCost();

                // If we found a new state or a better path to a known state
                if (!costMap.containsKey(succ) || newCost < costMap.get(succ)) {
                    costMap.put(succ, newCost);
                    succ.setCameFrom(current);
                    parentMap.put(succ, current);
                    open.add(succ);
                }
            }
        }

        // No solution found
        return new Solution(Collections.emptyList());
    }

    /**
     * Reconstructs the solution path from goal to start.
     */
    private Solution buildSolution(AState state, Map<AState, AState> parentMap) {
        LinkedList<AState> path = new LinkedList<>();
        AState curr = state;  // Start from the goal

        // Follow parents from back to start
        while (curr != null) {
            path.addFirst(curr);  // Add to front to get correct order
            curr = parentMap.get(curr);  // Move to parent
        }

        return new Solution(path);
    }


    @Override
    public String getName() {
        return "BestFirstSearch";
    }
}
