package atp.project.part3.atpprojectpartc.View;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * Custom JavaFX control for rendering a maze, its solution, and a player marker.
 */
public class MazeDisplayer extends Canvas {
    private static final Color WALL_COLOR = Color.rgb(30, 30, 30, 0.8);
    private static final Color PATH_COLOR = Color.TRANSPARENT;
    private final DoubleProperty cellSize = new SimpleDoubleProperty(20);
    private int[][] currentGrid;
    private AState[] currentSolution;
    private Integer playerRow = null, playerCol = null;
    private Integer goalRow = null, goalCol = null;
    private Image goalImage;
    private Image pathImage;
    private Image playerImage;
    private final DoubleProperty spriteX = new SimpleDoubleProperty(0);
    private final  DoubleProperty spriteY = new SimpleDoubleProperty(0);


    /**
     * Constructor that sets up the maze display canvas with zoom controls and images.
     */
    public MazeDisplayer() {
        setFocusTraversable(true);
        // Zoom handler: Ctrl + mouse wheel
        setOnScroll((ScrollEvent e) -> {
            if (e.isControlDown()) {
                double factor = e.getDeltaY() > 0 ? 1.1 : 0.9;
                double newSize = Math.min(Math.max(cellSize.get() * factor, 5), 100);
                cellSize.set(newSize);
                e.consume();
            }
        });
        // Redraw whenever cellSize changes
        cellSize.addListener((obs, oldV, newV) -> {
            updateSpritePosition();
            redraw();
        });
        goalImage = new Image(getClass().getResourceAsStream("/atp/project/part3/atpprojectpartc/images/Trophy.png"));
        pathImage = new Image(getClass().getResourceAsStream("/atp/project/part3/atpprojectpartc/images/basketball1.png"));
        playerImage = new Image(getClass().getResourceAsStream("/atp/project/part3/atpprojectpartc/images/Lebron2.png"));
        spriteX.addListener((o,a,b)-> redraw());
        spriteY.addListener((o,a,b)-> redraw());

    }

    /**
     * Takes a maze grid and displays it on the canvas.
     * Clears any previous solution and player position.
     */
    public void drawMaze(int[][] grid) {
        this.currentGrid = grid;
        // clear any previous solution when drawing a new maze
        this.currentSolution = null;
        // clear any previous player position
        this.playerRow = this.playerCol = null;
        this.goalCol = this.goalRow = null;
        redraw();
    }

    /**
     * Takes a solution path and displays it as basketball icons on the maze.
     */
    public void drawSolution(AState[] path) {
        this.currentSolution = path;
        redraw();
    }

    /**
     * Takes a solution path from a List and displays it on the maze.
     */
    public void drawSolution(java.util.List<? extends AState> path) {
        if (path != null) {
            drawSolution(path.toArray(new AState[0]));
        }
    }

    /**
     * Draws the entire maze including walls, paths, solution, goal, and player.
     */
    private void redraw() {
        if (currentGrid == null) return;
        GraphicsContext gc = getGraphicsContext2D();
        double cell = cellSize.get();
        int R = currentGrid.length;
        int C = currentGrid[0].length;
        // Resize canvas
        setWidth(C * cell);
        setHeight(R * cell);
        // Clear
        gc.clearRect(0, 0, getWidth(), getHeight());
        // Draw cells
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                gc.setFill(currentGrid[r][c] == 1 ? WALL_COLOR : PATH_COLOR);
                gc.fillRect(c * cell, r * cell, cell, cell);
            }
        }
        gc.setStroke(WALL_COLOR);
        gc.setLineWidth(1);
        gc.strokeRect(0.5, 0.5, C * cell - 1, R * cell - 1);
        // Solution overlay
        if (currentSolution != null) {
            double w = cell * 0.5;                // icon width = half the cell
            double h = w;                         // square icon
            double off = (cell - w) / 2;          // center in cell
            for (AState state : currentSolution) {
                if (state instanceof MazeState) {
                    Position pos = ((MazeState) state).getPosition();
                    gc.drawImage(
                            pathImage,
                            pos.getColumnIndex() * cell + off,
                            pos.getRowIndex() * cell + off,
                            w, h
                    );
                }
            }
        }

        //Goal overlay
        if (goalRow != null && goalCol != null) {
            double d = cell * 0.99 ;
            double off = (cell - d) / 2;
            gc.drawImage(goalImage, goalCol * cell + off, goalRow * cell + off, d, d);
        }

        // Player overlay
        if (playerRow != null && playerCol != null) {
            double d   = cell * 2;              // 150% of cell
            double off = (cell - d) / 2;

            gc.drawImage(playerImage, spriteX.get(), spriteY.get(), d, d);
        }
    }


    /**
     * Gets the current size of each maze cell in pixels.
     */
    public double getCellSize() {
        return cellSize.get();
    }

    /**
     * Places the player at a specific position if that cell is a valid path.
     */
    public void setPlayerPosition(int row, int col) {
        if (currentGrid == null) return;
        int R = currentGrid.length, C = currentGrid[0].length;
        if (row < 0 || row >= R || col < 0 || col >= C) return;
        if (currentGrid[row][col] != 0) return;    // must be path, not wall
        this.playerRow = row;
        this.playerCol = col;
        updateSpritePosition();
        redraw();
    }

    /**
     * Moves the player by the given amount with smooth animation.
     * Only moves if the target cell is valid and not a wall.
     */
    public void movePlayerBy(int dRow, int dCol) {
        if (playerRow == null || playerCol == null) return;

        // 1) get old pixel coordinates
        double oldX = spriteX.get(), oldY = spriteY.get();

        // 2) get new row/col
        int newRow = playerRow + dRow, newCol = playerCol + dCol;
        if (newRow < 0 || newRow >= currentGrid.length ||
                newCol < 0 || newCol >= currentGrid[0].length ||
                currentGrid[newRow][newCol] != 0) {
            return;
        }

        // 3) update position
        playerRow = newRow;
        playerCol = newCol;

        double cell = cellSize.get(), d = cell * 2, off = (cell - d)/2;
        double newX = newCol * cell + off;
        double newY = newRow * cell + off;

        Timeline tl = new Timeline(new KeyFrame(Duration.ZERO,new KeyValue(spriteX, oldX), new KeyValue(spriteY, oldY)
        ),
                new KeyFrame(Duration.millis(150), new KeyValue(spriteX, newX),new KeyValue(spriteY, newY)
                )
        );
        tl.play();
    }

    /**
     * Gets the current player row position.
     * @return player row or -1 if no player is set
     */
    public int getPlayerRow() {
        return playerRow == null ? -1 : playerRow;
    }

    /**
     * Gets the current player column position.
     * @return player column or -1 if no player is set
     */
    public int getPlayerCol() {
        return playerCol == null ? -1 : playerCol;
    }

    /**
     * Places the goal trophy at the specified position.
     */
    public void setGoalPosition(int row, int col) {
        this.goalRow = row;
        this.goalCol = col;
        redraw();
    }
    
    /**
     * Updates the pixel position of the player sprite based on current cell size.
     */
    private void updateSpritePosition() {
        if (playerRow == null || playerCol == null) return;
        double cell = cellSize.get();
        double d    = cell * 2.0;
        double off  = (cell - d) / 2.0;
        spriteX.set(playerCol * cell + off);
        spriteY.set(playerRow * cell + off);
    }

}
