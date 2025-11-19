package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BestFirstSearchTest {

    @Test
    void getName_shouldReturnCorrectAlgorithmName() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        String algorithmName = bestFirstSearch.getName();
        assertEquals("BestFirstSearch", algorithmName, "Algorithm name should be 'BestFirstSearch'");
    }

    @Test
    void solve_shouldReturnValidSolutionForSimpleMaze() {
        MyMazeGenerator generator = new MyMazeGenerator();
        Maze maze = generator.generate(5, 5); // Generate a simple 5x5 maze
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        Solution solution = bestFirstSearch.solve(searchableMaze);
        assertNotNull(solution, "Solution should not be null");
        assertFalse(solution.getSolutionPath().isEmpty(), "Solution path should not be empty");
        assertEquals(searchableMaze.getStartingState(), solution.getSolutionPath().get(0), "Solution should start at the maze's starting position");
        assertEquals(searchableMaze.getGoalState(), solution.getSolutionPath().get(solution.getSolutionPath().size() - 1), "Solution should end at the maze's goal position");
    }

    @Test
    void solve_shouldReturnEmptySolutionForUnreachableGoal() {
        Maze maze = new Maze(3, 3); // A maze filled with walls
        for (int i = 0; i < maze.getRowsSize(); i++) {
            for (int j = 0; j < maze.getColumnsSize(); j++) {
                maze.addCoordinate(i, j, 1); // Set walls
            }
        }
        maze.setStartPosition(0, 0);
        maze.setGoalPosition(2, 2);

        SearchableMaze searchableMaze = new SearchableMaze(maze);
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        Solution solution = bestFirstSearch.solve(searchableMaze);
        assertNotNull(solution, "Solution should not be null");
        assertTrue(solution.getSolutionPath().isEmpty(), "Solution path should be empty when the goal is unreachable");
    }

    @Test
    void solve_shouldEvaluateFewerNodesThanBFSForSimpleMaze() {
        MyMazeGenerator generator = new MyMazeGenerator();
        Maze maze = generator.generate(100, 100); // Generate a larger maze
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch();
        bestFirstSearch.solve(searchableMaze);
        breadthFirstSearch.solve(searchableMaze);
        assertTrue(bestFirstSearch.getNumberOfNodesEvaluated() <= breadthFirstSearch.getNumberOfNodesEvaluated(),
                "BestFirstSearch should have fewer or equal nodes than BreadthFirstSearch for the same maze");
    }

    @Test
    void solve_shouldThrowExceptionForNullInput() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        assertThrows(IllegalArgumentException.class, () -> bestFirstSearch.solve(null), "solve() should throw IllegalArgumentException when input is null");
    }

}