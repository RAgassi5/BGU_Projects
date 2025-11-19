package IO;

import java.io.IOException;
import java.io.OutputStream;

/**
 * compressor for Maze byte streams.
 */
public class MyCompressorOutputStream extends OutputStream {
    private final OutputStream destinationStream; // The output stream
    private int currentByte = -1; // Current byte being counted (-1 means no current byte)
    private int counter = 0; // Count of occurrences in a row

    /**
     * Creates a new compressor that writes to the output stream
     * @param destinationStream The stream where compressed data will be written
     */
    public MyCompressorOutputStream(OutputStream destinationStream) {
        this.destinationStream = destinationStream;
    }

    /**
     * Writes a single byte by converting it to a byte array
     */
    @Override
    public void write(int b) throws IOException {
        write(new byte[]{(byte)(b & 0xFF)}, 0, 1);
    }


    /**
     * Writer implementation
     */
    @Override
    public void write(byte[] b, int currentIndex, int len) throws IOException {
        for (int i = currentIndex; i < currentIndex + len; i++) {
            int value = b[i] & 0xFF; // Convert to unsigned byte value
            if (currentByte == -1) { // Start new sequence
                currentByte = value;
                counter = 1;
            } else if (value == currentByte && counter < 255) { // Continue current sequence
                counter++;
            } else {
                // End current sequence and start new one
                destinationStream.write(counter); // Write count
                destinationStream.write(currentByte); // Write value
                currentByte = value;
                counter = 1;
            }
        }
    }

    /**
     * Making sure all the data int the stream has passed
     */
    @Override
    public void flush() throws IOException {
        if (currentByte != -1) { // Write any pending sequence
            destinationStream.write(counter);
            destinationStream.write(currentByte);
            currentByte = -1;
            counter = 0;
        }
        destinationStream.flush();
    }

    /**
     * Flushes stuck data and closes the stream
     */
    @Override
    public void close() throws IOException {
        flush();
        destinationStream.close();
    }
}
