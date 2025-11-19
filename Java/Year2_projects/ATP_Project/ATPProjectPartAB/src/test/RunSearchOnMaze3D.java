package test;

import algorithms.maze3D.IMaze3DGenerator;
import algorithms.maze3D.MyMaze3DGenerator;
import algorithms.maze3D.Maze3D;
import algorithms.maze3D.SearchableMaze3D;

import algorithms.search.*;

import java.util.ArrayList;

public class RunSearchOnMaze3D {

    public static void main(String[] args) {
        IMaze3DGenerator gen = new MyMaze3DGenerator();
        Maze3D maze = gen.generate(6, 6, 6);   // קטן מספיק להדפסה
        maze.print();

        SearchableMaze3D searchableMaze = new SearchableMaze3D(maze);

        solveProblem(searchableMaze, new BreadthFirstSearch());
        solveProblem(searchableMaze, new DepthFirstSearch());
        solveProblem(searchableMaze, new BestFirstSearch());
    }

    private static void solveProblem(ISearchable domain,
                                     ISearchingAlgorithm searcher) {
        Solution solution = searcher.solve(domain);
        System.out.printf("'%s' algorithm - nodes evaluated: %d%n",
                searcher.getName(), searcher.getNumberOfNodesEvaluated());

        System.out.println("Solution path:");
        ArrayList<AState> path = solution.getSolutionPath();
        for (int i = 0; i < path.size(); i++) {
            System.out.printf("%d. %s%n", i, path.get(i));
        }
        System.out.println();
    }
}
