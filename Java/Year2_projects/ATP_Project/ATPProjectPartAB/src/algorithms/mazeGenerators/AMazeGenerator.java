package algorithms.mazeGenerators;

import java.util.Random;

/**
 * Abstract class for all maze generators.
 */
public abstract class AMazeGenerator implements IMazeGenerator {

    /**
     *
     * @param rows    maze height
     * @param columns maze width
     * @return a new instance of maze
     */
    public abstract Maze generate(int rows, int columns);

    /**
     * measures how long it takes to generate a maze.
     *
     * @param rows    maze height
     * @param columns maze width
     * @return total time in milliseconds
     */
    @Override
    public long measureAlgorithmTimeMillis(int rows, int columns) {
        long startTime = System.currentTimeMillis();
        this.generate(rows, columns);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    /**
     * Picks a random position on the edge of the maze.
     *
     * @param rows    maze height
     * @param columns maze width
     * @return random position on the maze perimeter
     */
    protected Position randomPerimeter(int rows, int columns) {
        Random rand = new Random();
        int border = rand.nextInt(4);
        int currentR;
        int currentC;
        switch (border) {
            case 0:
                //top border
                currentR = 0;
                currentC = rand.nextInt(columns);
                break;
            case 1:
                //bottom border
                currentR = rows - 1;
                currentC = rand.nextInt(columns);
                break;
            case 2:
                //left border
                currentR = rand.nextInt(rows);
                currentC = 0;
                break;
            default:
                //right border
                currentR = rand.nextInt(rows);
                currentC = columns - 1;

        }
        return new Position(currentR, currentC, 0);
    }

    /**
     *returns a random position on the edge of the maze while avoiding a specific coordinate on the maze
     * @param rows       maze height
     * @param columns    maze width
     * @param notEqualTo position to avoid
     * @return random perimeter position != notEqualTo
     */
    protected Position randomPerimeterDistinct(int rows, int columns, Position notEqualTo) {
        Position p;
        do {
            p = randomPerimeter(rows, columns);
        } while (p.equals(notEqualTo));
        return p;
    }
}
