package algorithms.mazeGenerators;

import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator {
    @Override
    public Maze generate(int rows, int columns) {

        Maze currentMaze = new Maze(rows, columns);
        randomPath(currentMaze, rows, columns);
        return currentMaze;
    }


    // generates a random path in the maze
    private void randomPath(Maze current, int rows, int columns) {
        Position start = randomPerimeter(rows, columns);
        Position goal = randomPerimeterDistinct(rows, columns, start);
        current.setStartPosition(start.getRowIndex(), start.getColumnIndex());
        current.updatePositionValue(start.getRowIndex(), start.getColumnIndex(), 0);
        current.setGoalPosition(goal.getRowIndex(), goal.getColumnIndex());
        current.updatePositionValue(goal.getRowIndex(), goal.getColumnIndex(), 0);


        // Fill the maze with walls
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                current.addCoordinate(i, j, 1);
            }
        }

        Random rand = new Random();
        // Start at top row
        int startX = start.getRowIndex();
        int startY = start.getColumnIndex();
        // End at bottom row
        int endX = goal.getRowIndex();
        int endY = goal.getColumnIndex();

        current.setStartPosition(startX, startY);
        current.setGoalPosition(endX, endY);

        int x = startX;
        int y = startY;
        current.updatePositionValue(x, y, 0); // start cell

        // Keep creating path until we reach E
        while (x != endX || y != endY) {
            boolean moveVertically = rand.nextBoolean(); //vertical/horizontal

            if (moveVertically && x != endX) {
                if (x < endX) x += 1;
                else x += -1;
            } else if (y != endY) {
                y += (y < endY) ? 1 : -1;
            } else {
                if (x < endX) x += 1;
                else x += -1;
            }

            current.updatePositionValue(x, y, 0);
        }
    }

}
