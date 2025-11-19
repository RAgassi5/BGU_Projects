package Client;

import java.io.InputStream;
import java.io.OutputStream;

public interface IClientStrategy {
    /**
     * @param inFromServer input stream from the server
     * @param outToServer   output stream to the server
     */
    void clientStrategy(InputStream inFromServer, OutputStream outToServer);
}
