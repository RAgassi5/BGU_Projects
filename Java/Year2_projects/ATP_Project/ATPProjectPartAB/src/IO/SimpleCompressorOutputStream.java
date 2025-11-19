package IO;

import java.io.IOException;
import java.io.OutputStream;

/**
 *  A stream that compresses data.
 *  * Counts consecutive identical bytes and writes them as count-value pairs.
 */
public class SimpleCompressorOutputStream extends OutputStream {
    private final OutputStream out;
    private Byte currentByte;
    private int count;

    /**
     * Creates a new compressor that writes to the specified output stream
     */
    public SimpleCompressorOutputStream(OutputStream out) {
        this.out = out;
    }


    /**
     * Writes a single byte
     */
    @Override
    public void write(int byteValue) throws IOException {
        byte value = (byte) byteValue;
        if (currentByte == null) {
            // Start of first run
            currentByte = value;
            count = 1;
        } else if (currentByte == value && count < 255) {
            // Continue current run if not at max length
            count++;
        } else {
            // End current run and start new one
            out.write((byte) count); // Write run length
            out.write(currentByte); // Write repeated value
            currentByte = value;  // Start new run
            count = 1;
        }
    }


    /**
     * Compresses an entire byte array by processing each byte in a row.
     */
    @Override
    public void write(byte[] byteArray) throws IOException {
        // Process each byte
        for (byte singleByte : byteArray) {
            write(singleByte);
        }
        // Write final run if there is one
        if (currentByte != null) {
            out.write((byte) count);
            out.write(currentByte);
        }
        out.flush();
    }


    /**
     * Ensures any pending run is written before closing the stream
     */
    @Override
    public void close() throws IOException {
        // ensure any pending data is written
        if (currentByte != null) {
            out.write((byte) count);
            out.write(currentByte);
        }
        out.close();
    }
}