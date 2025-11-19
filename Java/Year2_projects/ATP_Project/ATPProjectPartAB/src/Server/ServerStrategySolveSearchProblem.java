package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.ISearchingAlgorithm;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import java.io.*;
import java.util.Arrays;

/**
 * Server strategy to solve a search problem
 */
public class ServerStrategySolveSearchProblem implements IServerStrategy {
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        try (
                ObjectInputStream in = new ObjectInputStream(inFromClient);
                ObjectOutputStream out = new ObjectOutputStream(outToClient)
        ) {
            out.flush();
            // receive maze
            Maze maze = (Maze) in.readObject();
            byte[] mazeBytes = maze.toByteArray();
            // get hash for saving maze to tmp directory
            int mazeID = Arrays.hashCode(mazeBytes);
            String tmpDir = System.getProperty("java.io.tmpdir");
            File solutionFile = new File(tmpDir, "maze_" + mazeID + ".MazeSolution");
            Solution MazeSolution;
            if (solutionFile.exists()) {
                // loading MazeSolution
                try (ObjectInputStream fileIn = new ObjectInputStream(
                        new FileInputStream(solutionFile))) {
                    MazeSolution = (Solution) fileIn.readObject();
                }
            } else {
                // solving maze and saving MazeSolution to file
                SearchableMaze searchableMaze = new SearchableMaze(maze);
                String chosenAlgorithm = Configurations.getConfigFile().getMazeSearchingAlgorithm();
                ISearchingAlgorithm searcher;
                switch (chosenAlgorithm) {
                    case "DepthFirstSearch":
                        searcher = new DepthFirstSearch();
                        break;
                    case "BestFirstSearch":
                        searcher = new BestFirstSearch();
                        break;
                    case "BreadthFirstSearch":
                    default:
                        searcher = new BreadthFirstSearch();
                        break;
                }
                MazeSolution = searcher.solve(searchableMaze);
                // write to file
                try (ObjectOutputStream outputFile = new ObjectOutputStream(
                        new FileOutputStream(solutionFile))) {
                    outputFile.writeObject(MazeSolution);
                    outputFile.flush();
                }
            }
            // send MazeSolution
            out.writeObject(MazeSolution);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}