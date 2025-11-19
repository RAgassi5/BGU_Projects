package IO;

import java.io.IOException;
import java.io.InputStream;


/**
 * A stream that decompresses data that was compressed
 * Works in pairs with SimpleCompressorOutputStream to restore original data.
*/
public class SimpleDecompressorInputStream extends InputStream {
    private final InputStream in;    // Source of compressed data
    private int count;              // How many times to repeat current value
    private byte currentByte;               // Current value being repeated


    /**
     * Creates a new decompressor that reads from the given input stream
     */
    public SimpleDecompressorInputStream(InputStream in) {
        this.in = in;
    }


/**
 * Reads and returns one byte of decompressed data.
 **/

 @Override
    public int read() throws IOException {
     // Need to read next count-value pair
     if (count == 0) {
            int inputByte = in.read(); // How many times to repeat
         if (inputByte < 0) return -1; // End of stream
         count = inputByte;
            int v = in.read();
            if (v < 0) throw new IOException("Bad input was inserted");
            currentByte = (byte) v;
        }
        count--;
        return currentByte & 0xFF; // Return unsigned byte value
 }

    @Override
    public void close() throws IOException {
        in.close();
    }
}