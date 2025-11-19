package Client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final InetAddress serverIP;
    private final int serverPort;
    private final IClientStrategy strategy;

    public Client(InetAddress serverIP, int serverPort, IClientStrategy strategy) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.strategy = strategy;
    }

    /**
     * Opens a socket to the server, runs client strategy, and closes the socket.
     */
    public void communicateWithServer() {
        try (Socket socket = new Socket(serverIP, serverPort);
             InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            strategy.clientStrategy(in, out);
            System.out.println("Connecting to server, server_IP = " + serverIP + " in port : " + serverPort );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
