package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Multithreaded server that uses a thread pool to handle client sessions.
 */
public class Server {
    private int port;
    private int listeningTimer;
    private IServerStrategy serverStrategy;
    private volatile boolean isRunning;
    private ExecutorService threadPool;

    /**
     * Constructs a Server using pool size from Configurations.
     *
     * @param port                port to listen to
     * @param listeningTimer      timeout of server
     * @param serverStrategy      strategy to handle each client session
     */
    public Server(int port, int listeningTimer, IServerStrategy serverStrategy) {
        this.port = port;
        this.listeningTimer = listeningTimer;
        this.serverStrategy = serverStrategy;
        int poolSize = Configurations.getConfigFile().getNumOfThreadsInThreadPool();
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    /**
     * Starts the server loop in a new thread, connecting each client to the strategy using the thread pool.
     */
    public void start() {
        new Thread(() -> {
            try (ServerSocket newServerSocket = new ServerSocket(port)) {
                // Set timeout to allow checking stop condition
                newServerSocket.setSoTimeout(listeningTimer);
                System.out.println("Starting server at port = " + port);

                while (!isRunning) {
                    try {
                        // Wait for and accept new client connections
                        Socket clientSocket = newServerSocket.accept();
                        System.out.println("Client accepted: " + clientSocket);
                        // Handle each client in a separate thread from the thread pool
                        threadPool.execute(() -> handleClient(clientSocket));
                    } catch (SocketTimeoutException e) {
                        // Socket timeout - continue loop to check stop flag
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                shutdownThreadPool();
            }
        }, "Server-Acceptor-" + port).start();
    }

    private void handleClient(Socket clientSocket) {
        try {
            // Apply the strategy to handle the communication
            serverStrategy.applyStrategy(
                    clientSocket.getInputStream(),
                    clientSocket.getOutputStream()
            );
            // Closing the connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Signals the server to stop accepting new connections.
     */
    public void stop() {
        isRunning = true;
    }

    /**
     * Shuts down the thread pool gracefully.
     */
    private void shutdownThreadPool() {
        threadPool.shutdown();
        try {
            // Wait for existing tasks to terminate for up to 5 seconds
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                // Force shutdown if orderly shutdown fails
                threadPool.shutdownNow();
                if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    // Wait another 5 seconds for tasks to respond to being cancelled
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // Re-cancel if current thread also interrupted
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
