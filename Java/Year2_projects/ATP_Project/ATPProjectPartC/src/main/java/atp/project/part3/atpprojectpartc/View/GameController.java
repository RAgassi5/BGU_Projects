package atp.project.part3.atpprojectpartc.View;

import atp.project.part3.atpprojectpartc.Model.IModel;
import atp.project.part3.atpprojectpartc.ViewModel.MyViewModel;
import algorithms.mazeGenerators.Position;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;



public class GameController {
    @FXML
    private MazeDisplayer mazeDisplayer;
    @FXML
    private Button btnShowSolution;
    @FXML
    private Button btnReturnMenu;
    @FXML
    private MenuItem menuSave;
    @FXML
    private ImageView BGImage;
    private MyViewModel viewModel;
    private IModel model;
    private MediaPlayer musicPlayer;
    private int startRow, startCol, goalRow, goalCol;

    private final Image[] courts = new Image[]{
            new Image(getClass().getResourceAsStream("/atp/project/part3/atpprojectpartc/images/CelticsCourt.png")),
            new Image(getClass().getResourceAsStream("/atp/project/part3/atpprojectpartc/images/HornetsCourt.png")),
            new Image(getClass().getResourceAsStream("/atp/project/part3/atpprojectpartc/images/SpursCourt.png")),
            new Image(getClass().getResourceAsStream("/atp/project/part3/atpprojectpartc/images/Lakers.png")),
            new Image(getClass().getResourceAsStream("/atp/project/part3/atpprojectpartc/images/Wolves.png")),
            new Image(getClass().getResourceAsStream("/atp/project/part3/atpprojectpartc/images/Knicks.png")),
            new Image(getClass().getResourceAsStream("/atp/project/part3/atpprojectpartc/images/WarriorsCourt.png"))
    };

    /**
     * Override of JavaFX Controller initialize() method.
     * Loads and starts playing the background music for the game.
     */
    @FXML
    public void initialize() {
        // Load and play background music
        String musicPath = getClass()
                .getResource("/atp/project/part3/atpprojectpartc/sounds/GameMusic.mp3")
                .toExternalForm();
        Media media = new Media(musicPath);
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(0.5);
        musicPlayer.play();

    }

    /**
     * Connects the ViewModel and Model to this controller and sets up event handlers.
     */
    public void setViewModel(MyViewModel vm, IModel model) {
        this.viewModel = vm;
        this.model = model;
        this.viewModel.setModel(model);
        menuSave.setOnAction(e -> handleSave());
        btnReturnMenu.setOnAction(e -> {
            if (musicPlayer != null) {
                musicPlayer.stop();
            }
            try {
                onReturn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        mazeDisplayer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
            }
        });
    }

    /**
     * Starts a new game with the given maze dimensions.
     * Sets up the background, generates the maze, and prepares the game controls.
     */
    public void initGame(int rows, int cols) {
        Platform.runLater(() -> {
                    BGImage.fitWidthProperty().bind(mazeDisplayer.widthProperty());
                    BGImage.fitHeightProperty().bind(mazeDisplayer.heightProperty());
                    BGImage.setImage(shuffleBG());
                }
        );

        viewModel.rows.set(rows);
        viewModel.cols.set(cols);

        viewModel.addObserver((o, arg) -> {
            if ("maze".equals(arg)) {
                int[][] grid = viewModel.mazeProperty().get();
                mazeDisplayer.drawMaze(grid);

                // initialize start & goal
                Position start = viewModel.getStartPosition();
                startRow = start.getRowIndex();
                startCol = start.getColumnIndex();
                Position goal = viewModel.getGoalPosition();
                goalRow = goal.getRowIndex();
                goalCol = goal.getColumnIndex();

                //place goal image
                mazeDisplayer.setGoalPosition(goalRow, goalCol);

                // place player at the start position
                mazeDisplayer.setPlayerPosition(startRow, startCol);

                wireControls();

            } else if ("solution".equals(arg)) {
                mazeDisplayer.drawSolution(viewModel.solutionProperty().get().getSolutionPath());
                Platform.runLater(() -> mazeDisplayer.requestFocus());
            }
        });

        viewModel.generateMaze();
    }

    /**
     * Sets up keyboard and mouse controls for player movement.
     */
    private void wireControls() {
        // enable keyboard
        mazeDisplayer.setFocusTraversable(true);
        Platform.runLater(() -> mazeDisplayer.requestFocus());
        mazeDisplayer.setOnKeyPressed(this::handleKeyPress);

        // enable mouse dragging
        mazeDisplayer.setOnMouseClicked(this::handleMouseJump);
        mazeDisplayer.setOnMouseDragged(this::handleMouseJump);
    }

    /**
     * Handles keyboard input for player movement.
     * Supports arrow keys, numpad keys, and diagonal movement.
     */
    private void handleKeyPress(KeyEvent evt) {
        boolean moved = true;
        switch (evt.getCode()) {
            case UP:
            case NUMPAD8:
                mazeDisplayer.movePlayerBy(-1, 0);
                break;
            case DOWN:
            case NUMPAD2:
                mazeDisplayer.movePlayerBy(1, 0);
                break;
            case LEFT:
            case NUMPAD4:
                mazeDisplayer.movePlayerBy(0, -1);
                break;
            case RIGHT:
            case NUMPAD6:
                mazeDisplayer.movePlayerBy(0, 1);
                break;
            case NUMPAD7:
                mazeDisplayer.movePlayerBy(-1, -1);
                break;
            case NUMPAD9:
                mazeDisplayer.movePlayerBy(-1, 1);
                break;
            case NUMPAD1:
                mazeDisplayer.movePlayerBy(1, -1);
                break;
            case NUMPAD3:
                mazeDisplayer.movePlayerBy(1, 1);
                break;
            default:
                moved = false;
        }
        if (moved) {
            evt.consume();
            checkGoal();
        }
    }

    /**
     * Handles mouse clicks and drags for player movement.
     * Only allows movement to adjacent cells including diagonals.
     */
    private void handleMouseJump(MouseEvent e) {
        double cell = mazeDisplayer.getCellSize();
        int targetCol = (int) (e.getX() / cell);
        int targetRow = (int) (e.getY() / cell);

        int pr = mazeDisplayer.getPlayerRow();
        int pc = mazeDisplayer.getPlayerCol();
        if (pr < 0 || pc < 0) return;

        int dR = targetRow - pr;
        int dC = targetCol - pc;

        if (Math.abs(dR) <= 1 && Math.abs(dC) <= 1) {
            mazeDisplayer.movePlayerBy(dR, dC);
            checkGoal();
        }
        mazeDisplayer.requestFocus();
        e.consume();
    }

    /**
     * Returns keyboard focus to the maze displayer for continued input handling.
     */
    private void evtRequestFocus() {
        mazeDisplayer.requestFocus();
    }

    /**
     * Checks if the player has reached the goal position.
     * If so, stops the music and shows the victory celebration.
     */
    private void checkGoal() {
        if (mazeDisplayer.getPlayerRow() == goalRow &&
                mazeDisplayer.getPlayerCol() == goalCol) {
            if (musicPlayer != null) {
                musicPlayer.stop();
            }
            endVideo();

        }
    }

    /**
     * Loads and starts a game from a saved maze file.
     * Sets up the background and loads the maze with saved player position.
     */
    public void initGameFromFile(String path) {
        Platform.runLater(() -> {
                    BGImage.fitWidthProperty().bind(mazeDisplayer.widthProperty());
                    BGImage.fitHeightProperty().bind(mazeDisplayer.heightProperty());
                    BGImage.setImage(shuffleBG());
                }
        );

        try {
            viewModel.loadMaze(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load maze", e);
        }

        int[][] grid = viewModel.mazeProperty().get();


        mazeDisplayer.drawMaze(grid);
        Position start = viewModel.getStartPosition();
        Position goal = viewModel.getGoalPosition();
        goalRow = goal.getRowIndex();
        goalCol = goal.getColumnIndex();
        mazeDisplayer.setGoalPosition(goalRow, goalCol);
        Position p = viewModel.getPlayerPosition();
        if (p != null) {
            mazeDisplayer.setPlayerPosition(p.getRowIndex(), p.getColumnIndex());
        }


        viewModel.addObserver((obs, arg) -> {
            if ("maze".equals(arg)) {
                int[][] g = viewModel.mazeProperty().get();
                mazeDisplayer.drawMaze(g);
                Position s = viewModel.getStartPosition();
                Position gl = viewModel.getGoalPosition();
                mazeDisplayer.setGoalPosition(gl.getRowIndex(), gl.getColumnIndex());
                Position pos = viewModel.getPlayerPosition();
                if (pos != null) {
                    mazeDisplayer.setPlayerPosition(pos.getRowIndex(), pos.getColumnIndex());
                }
            } else if ("solution".equals(arg)) {
                mazeDisplayer.drawSolution(
                        viewModel.solutionProperty()
                                .get()
                                .getSolutionPath()
                );

                Platform.runLater(() -> mazeDisplayer.requestFocus());
            }
        });

        wireControls();
    }

    /**
     * FXML event handler for the Show Solution button.
     * Requests the solution from the ViewModel and displays it.
     */
    @FXML
    private void onShowSolution() {
        try {
            viewModel.solveMaze();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens a file save dialog and saves the current maze with player position.
     */
    private void handleSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Maze");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files", "*.maze", "*.dat"));
        Stage stage = (Stage) menuSave.getParentPopup().getOwnerWindow();
        var file = chooser.showSaveDialog(stage);
        if (file != null) {
            try {
                Position p = new Position(mazeDisplayer.getPlayerRow(), mazeDisplayer.getPlayerCol(), 0);
                viewModel.saveMaze(file.getAbsolutePath(), p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * FXML event handler for returning to the main menu.
     * Loads the main menu view and applies the CSS styling.
     */
    @FXML
    private void onReturn() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = loader.load();
        MyViewController mc = loader.getController();
        mc.setViewModel(viewModel, model);
        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                getClass()
                        .getResource("/atp/project/part3/atpprojectpartc/styles/app.css")
                        .toExternalForm()
        );

        Stage stage = (Stage) btnReturnMenu.getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Randomly selects one of the basketball court background images.
     */
    private Image shuffleBG() {
        return courts[(int) (Math.random() * courts.length)];
    }

    /**
     * Shows the victory celebration video when player completes the maze.
     * Creates a modal popup with the video and a skip button.
     */
    private void endVideo() {
        //Create the popup stage
        final Stage popupStage = new Stage(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);

        //Build media player + its presentation
        String url = getClass()
                .getResource("/atp/project/part3/atpprojectpartc/videos/finishMaze.mp4")
                .toExternalForm();
        MediaPlayer videoPlayer = new MediaPlayer(new Media(url));
        MediaView mediaView = new MediaView(videoPlayer);
        mediaView.setPreserveRatio(true);
        VBox root = new VBox(10, mediaView);
        root.setAlignment(Pos.CENTER);
        //create a skip video button
        Button skip = new Button("Skip Championship Celebration");
        skip.setOnAction(e -> {
            videoPlayer.stop();
            popupStage.close();
            try {
                onReturn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        root.getChildren().add(skip);

        Scene scene = new Scene(root, 800, 600);
        mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(20));
        mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(60));
        videoPlayer.setOnEndOfMedia(() -> {
            popupStage.close();
            try {
                onReturn();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        scene.getStylesheets().add(
                getClass().getResource("/atp/project/part3/atpprojectpartc/styles/app.css").toExternalForm()
        );

        popupStage.setScene(scene);
        popupStage.show();
        videoPlayer.play();
    }
}