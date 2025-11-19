package atp.project.part3.atpprojectpartc.ViewModel;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import algorithms.search.Solution;
import algorithms.mazeGenerators.Position;
import atp.project.part3.atpprojectpartc.Model.IModel;

/**
 * ViewModel for the maze application.
 * Observes the Model for updates and exposes JavaFX properties
 * for the View to bind to.
 */
public class MyViewModel extends Observable implements Observer {
    private IModel model;

    public final IntegerProperty rows = new SimpleIntegerProperty(10);
    public final IntegerProperty cols = new SimpleIntegerProperty(10);

    private final ObjectProperty<int[][]> maze = new SimpleObjectProperty<>();
    public ReadOnlyObjectProperty<int[][]> mazeProperty() { return maze; }

    private final ObjectProperty<Solution> solution = new SimpleObjectProperty<>();
    public ReadOnlyObjectProperty<Solution> solutionProperty() { return solution; }

    private final ObjectProperty<Position> startPosition = new SimpleObjectProperty<>();
    public ReadOnlyObjectProperty<Position> startPositionProperty() { return startPosition; }
    public Position getStartPosition() { return startPosition.get(); }

    private final ObjectProperty<Position> goalPosition = new SimpleObjectProperty<>();
    public ReadOnlyObjectProperty<Position> goalPositionProperty() { return goalPosition; }
    public Position getGoalPosition() { return goalPosition.get(); }

    private final ObjectProperty<Position> playerPosition = new SimpleObjectProperty<>();
    public ReadOnlyObjectProperty<Position> playerPositionProperty() {return playerPosition;}
    public Position getPlayerPosition() {return playerPosition.get();}
    
    /**
     * Connects the Model to this ViewModel and starts observing it for changes.
     */
    public void setModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
    }

    /**
     * Asks the Model to create a new maze using the current row and column settings.
     */
    public void generateMaze() {
        model.generateMaze(rows.get(), cols.get());
    }

    /**
     * Asks the Model to find the solution for the current maze.
     */
    public void solveMaze() throws IOException {
        model.solveMaze();
    }

    /**
     * Asks the Model to save the current maze and player position to a file.
     */
    public void saveMaze(String filePath, Position playerPos) throws IOException {
        model.saveMaze(filePath, playerPos);
    }

    /**
     * Asks the Model to load a maze from a file.
     */
    public void loadMaze(String filePath) throws IOException {
        model.loadMaze(filePath);
    }

    /**
     * Tells the Model to clean up before the application exits.
     */
    public void exit() {
        model.exit();
    }

    /**
     * Override of Observer.update() method.
     * Gets called when the Model changes and updates the ViewModel properties accordingly.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof String)) return;
        String event = (String) arg;
        switch (event) {
            case "generate":
            case "load":
                maze.set(model.getMaze());
                startPosition.set(model.getStartPosition());
                goalPosition.set(model.getGoalPosition());
                playerPosition.set(model.getSavedPlayerPosition());
                setChanged(); notifyObservers("maze");
                break;
            case "solve":
                solution.set(model.getSolution());
                setChanged(); notifyObservers("solution");
                break;
            case "exit":
                setChanged(); notifyObservers("exit");
                break;
        }
    }
}
