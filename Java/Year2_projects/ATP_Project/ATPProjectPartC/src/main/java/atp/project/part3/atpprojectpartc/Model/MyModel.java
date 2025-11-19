package atp.project.part3.atpprojectpartc.Model;

import java.io.*;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;
import IO.SimpleCompressorOutputStream;
import IO.SimpleDecompressorInputStream;
import Client.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Concrete implementation of IModel using Part-2 library classes.
 * Stores the Maze object directly so we preserve start/goal positions.
 */
public class MyModel extends Observable implements IModel {
    private Maze maze;
    private Solution solution;
    private final InetAddress serverIP;
    private final int solvePort;
    private final int generatePort;
    private static final Logger GEN_LOGGER =
            LogManager.getLogger("Generate New Maze");
    private static final Logger SOLVE_LOGGER =
            LogManager.getLogger("Request Maze Solution");
    private Position lastLoadedPlayerPosition;

    /**
     * Constructor that sets up the server connection information.
     * @param serverIP the IP address of the server
     * @param generatePort port number for maze generation server
     * @param solvePort port number for maze solving server
     */
    public MyModel(InetAddress serverIP, int generatePort, int solvePort) {
        this.serverIP = serverIP;
        this.generatePort = generatePort;
        this.solvePort = solvePort;
    }

    /**
     * Override of IModel.generateMaze().
     * Creates a new maze by sending a request to the maze generation server.
     */
    @Override
    public void generateMaze(int rows, int cols) {
        GEN_LOGGER.info("Client {}:{} requested GENERATE maze ({}×{})",
                serverIP.getHostAddress(), generatePort, rows, cols);
        Client client = new Client(serverIP, generatePort, (inRaw, outRaw) -> {

            try {
                ObjectOutputStream oos = new ObjectOutputStream(outRaw);
                ObjectInputStream ois = new ObjectInputStream(inRaw);
                oos.flush();

                //send dimensions
                oos.writeObject(new int[]{rows, cols});
                oos.flush();

                //read compressed maze bytes
                byte[] compressed = (byte[]) ois.readObject();

                //decompress into the raw maze bytes
                ByteArrayInputStream bais = new ByteArrayInputStream(compressed);
                MyDecompressorInputStream mdin = new MyDecompressorInputStream(bais);
                byte[] rawMaze = mdin.readAllBytes();

                //reconstruct the Maze
                this.maze = new Maze(rawMaze);
                this.solution = null;

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        client.communicateWithServer();
        setChanged();
        notifyObservers("generate");
    }

    /**
     * Override of IModel.solveMaze().
     * Gets the solution from the maze solving server for the current maze.
     */
    @Override
    public void solveMaze() {
        if (maze == null) return;

        SOLVE_LOGGER.info("Player Requested maze solution");

        Client client = new Client(serverIP, solvePort, (inRaw, outRaw) -> {

            try {
                ObjectOutputStream oos = new ObjectOutputStream(outRaw);
                ObjectInputStream ois = new ObjectInputStream(inRaw);
                oos.flush();


                oos.writeObject(maze);
                oos.flush();


                Solution sol = (Solution) ois.readObject();
                this.solution = sol;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        client.communicateWithServer();
        setChanged();
        notifyObservers("solve");
    }

    /**
     * Override of IModel.saveMaze().
     * Saves the current maze and player position to a compressed file.
     */
    @Override
    public void saveMaze(String filePath, Position playerPos) throws IOException {
        byte[] data = maze.toByteArray();


        try (DataOutputStream dos = new DataOutputStream(
                new SimpleCompressorOutputStream(new FileOutputStream(filePath)))) {

            dos.writeInt(data.length);

            dos.write(data);

            dos.writeInt(playerPos.getRowIndex());
            dos.writeInt(playerPos.getColumnIndex());

            dos.flush();
        }
    }

    /**
     * Override of IModel.loadMaze().
     * Loads a maze from a compressed file including the saved player position.
     */
    @Override
    public void loadMaze(String filePath) throws IOException {
        try (DataInputStream dis = new DataInputStream(
                new SimpleDecompressorInputStream(new FileInputStream(filePath)))) {
            int length = dis.readInt();
            byte[] data = new byte[length];
            dis.readFully(data);
            this.maze = new Maze(data);

            int r = dis.readInt();
            int c = dis.readInt();
            lastLoadedPlayerPosition = new Position(r, c, 0);
        }

        this.solution = null;
        setChanged();
        notifyObservers("load");
    }

    /**
     * Override of IModel.assignObserver().
     * Adds an observer to get notified when this model changes.
     */
    @Override
    public void assignObserver(Observer o) {
        addObserver(o);
    }

    /**
     * Override of IModel.exit().
     * Notifies all observers that the application is closing.
     */
    @Override
    public void exit() {
        setChanged();
        notifyObservers("exit");
    }

    /**
     * Override of IModel.getMaze().
     * Converts the current Maze object into a 2D int array for the view to use.
     */
    @Override
    public int[][] getMaze() {
        if (maze == null) return null;
        int rows = maze.getRowsSize();
        int cols = maze.getColumnsSize();
        int[][] grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = maze.getValue(i, j);
            }
        }
        return grid;
    }

    /**
     * Override of IModel.getSolution().
     * Returns the most recently calculated solution.
     */
    @Override
    public Solution getSolution() {
        return solution;
    }

    /**
     * Override of IModel.getStartPosition().
     * Gets the starting position from the current maze.
     */
    @Override
    public Position getStartPosition() {
        return this.maze.getStartPosition();
    }

    /**
     * Override of IModel.getGoalPosition().
     * Gets the goal position from the current maze.
     */
    @Override
    public Position getGoalPosition() {
        return this.maze.getGoalPosition();
    }

    /**
     * Override of IModel.getSavedPlayerPosition().
     * Gets the player position that was saved in a loaded maze file.
     */
    @Override
    public Position getSavedPlayerPosition() {
        return lastLoadedPlayerPosition;
    }
}
