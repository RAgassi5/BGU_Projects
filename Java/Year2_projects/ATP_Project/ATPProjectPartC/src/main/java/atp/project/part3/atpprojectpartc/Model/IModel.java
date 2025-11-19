package atp.project.part3.atpprojectpartc.Model;

import java.util.Observer;
import java.io.IOException;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;


/**
 * Model interface for the maze application.
 * Defines operations for generating, solving, saving, and loading a maze,
 * and for registering observers to be notified of model changes.
 */
public interface IModel {
    /**
     * Creates a new maze with the given size.
     * @param rows number of rows in the maze
     * @param cols number of columns in the maze
     */
    void generateMaze(int rows, int cols);

    /**
     * Finds the solution path for the current maze.
     */
    void solveMaze() throws IOException;

    /**
     * Saves the current maze and player position to a file.
     * @param filePath where to save the file
     * @param playerPos current player position to save
     * @throws IOException if file writing fails
     */
    void saveMaze(String filePath, Position playerPos) throws IOException;

    /**
     * Loads a maze from a saved file.
     * @param filePath path to the maze file
     * @throws IOException if file reading fails
     */
    void loadMaze(String filePath) throws IOException;

    /**
     * Adds an observer that gets notified when the model changes.
     * @param o the observer to add
     */
    void assignObserver(Observer o);

    /**
     * Cleans up resources before the application closes.
     */
    void exit();

    /**
     * Gets the maze as a 2D array where 0 = open path, 1 = wall.
     * @return the maze grid
     */
    int[][] getMaze();

    /**
     * Gets the solution path if one has been calculated.
     * @return the solution or null if no solution exists
     */
    Solution getSolution();

    /**
     * Gets the starting position of the maze.
     * @return the start position
     */
    Position getStartPosition();
    
    /**
     * Gets the goal position that the player needs to reach.
     * @return the goal position
     */
    Position getGoalPosition();
    
    /**
     * Gets the saved player position from a loaded maze file.
     * @return the saved player position
     */
    Position getSavedPlayerPosition();
}
