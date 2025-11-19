package Server;
import java.io.InputStream;
import java.io.OutputStream;

public interface IServerStrategy {
    /**
     * Implements one client-server conversation on the server side.
     * @param inFromClient   incoming stream from the client
     * @param outToClient    outgoing stream to the client
     */
    void applyStrategy(InputStream inFromClient, OutputStream outToClient);
}
