package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import java.io.*;

/**
 * Server strategy to generate a maze, compress it, and send to the client.
 */
public class ServerStrategyGenerateMaze implements IServerStrategy {
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        try (
                ObjectInputStream in = new ObjectInputStream(inFromClient);
                ObjectOutputStream out = new ObjectOutputStream(outToClient)
        ) {
            out.flush();
            int[] mazeDimensions = (int[]) in.readObject();
            int rows = mazeDimensions[0];
            int cols = mazeDimensions[1];

            // Get configured maze generator and create new instance
            String mazeGeneratingAlgorithm = Configurations.getConfigFile().getMazeGeneratingAlgorithm();
            IMazeGenerator generator = (IMazeGenerator) Class.forName("algorithms.mazeGenerators." + mazeGeneratingAlgorithm).getDeclaredConstructor().newInstance();

            // Generate the maze with requested dimensions
            Maze maze = generator.generate(rows, cols);

            // Compress the maze before sending
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (MyCompressorOutputStream compressor = new MyCompressorOutputStream(byteArrayOutputStream)) {
                compressor.write(maze.toByteArray());
            }

            // Send the compressed maze back to client
            byte[] myCompressedMaze = byteArrayOutputStream.toByteArray();
            out.writeObject(myCompressedMaze);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}