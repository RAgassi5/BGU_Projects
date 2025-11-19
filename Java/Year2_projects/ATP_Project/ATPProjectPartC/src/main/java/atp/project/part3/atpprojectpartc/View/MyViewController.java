package atp.project.part3.atpprojectpartc.View;

import atp.project.part3.atpprojectpartc.Model.IModel;
import atp.project.part3.atpprojectpartc.ViewModel.MyViewModel;
import javafx.animation.RotateTransition;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.application.Platform;
import javafx.geometry.Insets;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;


public class MyViewController implements IView {
    @FXML
    private Button btnNewGame, btnOptions, btnHelp, btnAbout, btnExitMenu;
    @FXML
    private ImageView ballIcon;
    private MyViewModel viewModel;
    private IModel model;
    private MediaPlayer musicPlayer;

    /**
     * Override of IView.setViewModel().
     * Connects the ViewModel and Model to this controller.
     */
    public void setViewModel(MyViewModel vm, IModel model) {
        this.viewModel = vm;
        this.model = model;
        this.viewModel.setModel(model);
    }

    /**
     * Override of IView.initialize().
     * Sets up the main menu with rotating basketball animation and button handlers.
     */
    @FXML
    public void initialize() {
        initMusic();

        RotateTransition rt = new RotateTransition(Duration.seconds(2), ballIcon);
        rt.setByAngle(360);
        rt.setCycleCount(RotateTransition.INDEFINITE);
        rt.play();

        btnNewGame.setOnAction(e -> showNewOrLoadDialog());
        btnOptions.setOnAction(e -> onOptions());
        btnHelp.setOnAction(e -> onHelp());
        btnAbout.setOnAction(e -> onAbout());
        btnExitMenu.setOnAction(e -> onExitMenu());
    }

    /**
     * Loads and starts playing the main menu background music.
     */
    private void initMusic() {
        String resource = getClass()
                .getResource("/atp/project/part3/atpprojectpartc/sounds/MenuMusic.mp3")
                .toExternalForm();
        Media media = new Media(resource);
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(0.5);
        musicPlayer.play();
    }


    /**
     * Shows a dialog asking the user whether to start a new game or load an existing one.
     */
    @FXML
    private void showNewOrLoadDialog() {
        Alert dlg = new Alert(AlertType.NONE);
        dlg.setTitle("Start Game");
        dlg.setHeaderText("Choose an option:");
        ButtonType bNew = new ButtonType("New");
        ButtonType bLoad = new ButtonType("Load");
        ButtonType bCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dlg.getButtonTypes().setAll(bNew, bLoad, bCancel);
        Optional<ButtonType> res = dlg.showAndWait();
        if (!res.isPresent() || res.get() == bCancel) return;
        if (res.get() == bNew) startNewGame();
        else if (res.get() == bLoad) handleLoad();
    }

    /**
     * Handles starting a new game by getting maze dimensions from user and launching the game.
     */
    private void startNewGame() {
        // prompt for dimensions
        Pair<Integer, Integer> dims = promptForDimensions();
        if (dims == null) return;
        // load game scene
        try {
            if (musicPlayer != null) musicPlayer.stop();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameView.fxml"));
            Scene scene = new Scene(loader.load());
            GameController gc = loader.getController();
            gc.setViewModel(viewModel, model);
            gc.initGame(dims.getKey(), dims.getValue());
            ((Stage) btnNewGame.getScene().getWindow()).setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Unable to load game view.");
        }
    }

    /**
     * Handles loading a saved maze file and starting the game with it.
     */
    private void handleLoad() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load Maze");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze files", "*.maze", "*.dat"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = chooser.showOpenDialog(((Stage) btnNewGame.getScene().getWindow()));
        if (file != null) {
            try {
                if (musicPlayer != null) musicPlayer.stop();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GameView.fxml"));
                Scene scene = new Scene(loader.load());
                GameController gc = loader.getController();
                gc.setViewModel(viewModel, model);
                gc.initGameFromFile(file.getAbsolutePath());
                ((Stage) btnNewGame.getScene().getWindow()).setScene(scene);
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Failed to load maze file.");
            }
        }
    }

    /**
     * Shows a dialog asking the user to enter maze dimensions for a new game.
     */
    private Pair<Integer, Integer> promptForDimensions() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.initOwner(((Stage) btnNewGame.getScene().getWindow()));
        dialog.setTitle("New Maze");
        dialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("OK", ButtonData.OK_DONE),
                ButtonType.CANCEL
        );
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField rowsField = new TextField();
        rowsField.setPromptText("Rows");
        TextField colsField = new TextField();
        colsField.setPromptText("Cols");

        grid.add(new Label("Rows:"), 0, 0);
        grid.add(rowsField, 1, 0);
        grid.add(new Label("Cols:"), 0, 1);
        grid.add(colsField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(rowsField::requestFocus);

        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonData.OK_DONE) {
                return new Pair<>(rowsField.getText(), colsField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> res = dialog.showAndWait();
        if (res.isPresent()) {
            try {
                int r = Integer.parseInt(res.get().getKey());
                int c = Integer.parseInt(res.get().getValue());
                return new Pair<>(r, c);
            } catch (NumberFormatException ex) {
                showError("Please enter valid integers for rows and columns.");
            }
        }
        return null;
    }


    /**
     * FXML event handler for the Options button.
     * Shows a dialog displaying the current game configuration properties.
     */
    @FXML
    private void onOptions() {
        Properties props = new Properties();
        try (InputStream in = getClass()
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (in == null) {
                showError("Could not find config.properties in resources!");
                return;
            }
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load config.properties:\n" + e.getMessage());
            return;
        }

        Dialog<Void> dlg = new Dialog<>();
        dlg.setTitle("Properties");
        dlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        TableView<Map.Entry<Object, Object>> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Map.Entry<Object, Object>, String> keyCol = new TableColumn<>("Configurations");
        keyCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getKey().toString())
        );

        TableColumn<Map.Entry<Object, Object>, String> valCol = new TableColumn<>("Set Value");
        valCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getValue().toString())
        );

        table.getColumns().addAll(keyCol, valCol);
        table.getItems().addAll(props.entrySet());

        dlg.getDialogPane().setContent(table);
        dlg.initOwner(btnOptions.getScene().getWindow());
        dlg.showAndWait();
    }


    /**
     * FXML event handler for the Help button.
     * Shows a dialog with game instructions loaded from a help text file.
     */
    @FXML
    private void onHelp() {
        String helpText;
        try (InputStream in = getClass()
                .getClassLoader()
                .getResourceAsStream("help.txt")) {
            if (in == null) {
                showError("Help file not found in resources!");
                return;
            }
            helpText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load help text: " + e.getMessage());
            return;
        }

        TextArea area = new TextArea(helpText);
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefWidth(500);
        area.setPrefHeight(400);

        Dialog<Void> dlg = new Dialog<>();
        dlg.setTitle("Game Help");
        dlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dlg.getDialogPane().setContent(area);
        dlg.initOwner(btnHelp.getScene().getWindow());
        dlg.showAndWait();
    }

    /**
     * FXML event handler for the About button.
     * Shows information about the game, developers, and technologies used.
     */
    @FXML
    private void onAbout() {
        String aboutText =
                "*** Think You Can Dribble Through the Unknown? ***\nChallenge your wits, navigate every twist, and outplay the clock—only the true champion claims the Finals trophy!" +
                        "\n\n" +
                        "===================\n"+
                "Race To The Finals\n" +
                        "===================\n\n" +
                        "Maze Generation Algorithm:\n" +
                        "- Uses MyMazeGenerator  to carve a perfect maze.\n\n" +
                        "Maze Solving Algorithm:\n" +
                        "- Uses Best-First Search to compute the shortest path.\n\n" +
                        "Client-Server Architecture:\n" +
                        "- Maze generation and solving are performed on a background server.\n" +
                        "- The JavaFX client communicates via sockets (Client / IClientStrategy).\n\n" +
                        "Built by:\n" +
                        "-> Roii Agassi\n    ID: 207455213\n" +
                        "-> Tomer Shoshani\n    ID: 211822457\n\n" +
                        "- Project for \"Advanced Techniques in Programming\" course\n\n" +
                        "Version: 1.0\n" +
                        "© 2025 Ben-Gurion University - Be'er Sheva";

        TextArea area = new TextArea(aboutText);
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefWidth(450);
        area.setPrefHeight(350);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About Race To The Finals");
        alert.setHeaderText("Application Details and Credits");
        alert.getDialogPane().setContent(area);
        alert.initOwner(btnAbout.getScene().getWindow());
        alert.getButtonTypes().setAll(ButtonType.CLOSE);
        alert.showAndWait();
    }

    /**
     * FXML event handler for the Exit button.
     * Stops the music and closes the application.
     */
    @FXML
    private void onExitMenu() {
        this.musicPlayer.stop();
        Platform.exit();
    }

    /**
     * Shows an error dialog to the user with the given message.
     */
    @FXML
    private void showError(String msg) {
        new Alert(AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}