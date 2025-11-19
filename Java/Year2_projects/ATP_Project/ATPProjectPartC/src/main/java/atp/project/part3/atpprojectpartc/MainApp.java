package atp.project.part3.atpprojectpartc;

import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import atp.project.part3.atpprojectpartc.Model.MyModel;
import atp.project.part3.atpprojectpartc.Model.IModel;
import atp.project.part3.atpprojectpartc.ViewModel.MyViewModel;
import atp.project.part3.atpprojectpartc.View.MyViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.InetAddress;

public class MainApp extends Application {
    private Server generateServer;
    private Server solveServer;

    /**
     * Override of JavaFX Application.start() method.
     * Sets up the maze generation and solving servers, then loads the main menu view.
     */
    @Override
    public void start(Stage stage) throws IOException {

        generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        generateServer.start();
        solveServer.start();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/atp/project/part3/atpprojectpartc/View/MyView.fxml")
        );
        Parent root = loader.load();

        IModel model = new MyModel(InetAddress.getLocalHost(), 5400, 5401);
        MyViewModel vm   = new MyViewModel();
        vm.setModel(model);

        MyViewController controller = loader.getController();
        controller.setViewModel(vm, model);
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(
                getClass().getResource(
                        "/atp/project/part3/atpprojectpartc/styles/app.css"
                ).toExternalForm()
        );
        stage.setScene(scene);
        stage.setTitle("ATP Maze Solver");
        stage.show();
    }

    /**
     * Main entry point for the application.
     * Launches the JavaFX application.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Override of JavaFX Application.stop() method.
     * Stops both servers when the application is closing.
     */
    @Override
    public void stop() {
        if (this.generateServer != null) generateServer.stop();
        if (this.solveServer    != null) solveServer.stop();
        System.out.println("Thank you for playing!\nServers stopped -> Exiting app now");
    }

}