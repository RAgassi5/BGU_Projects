package algorithms.mazeGenerators;

/**
 * Interface for maze generation algorithms.
 *
 */
public interface IMazeGenerator {

    /**
     * Creates a new maze with the specified dimensions.
     * 
     * @param rows    number of rows in the maze
     * @param columns number of columns in the maze
     * @return a new instance of maze
     */
    Maze generate(int rows, int columns);

    /**
     * measure the running time of generating each maze
     *
     * @param rows    maze height
     * @param columns maze width
     * @return generation time in milliseconds
     */
    long measureAlgorithmTimeMillis(int rows, int columns);

}
