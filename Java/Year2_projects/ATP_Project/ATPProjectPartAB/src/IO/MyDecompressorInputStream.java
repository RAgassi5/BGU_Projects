package IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Decompresses data that was compressed using run-length encoding.
 * Works in pairs with MyCompressorOutputStream to restore original data.
 */
public class MyDecompressorInputStream extends InputStream {
    private final InputStream dataInputStream;        // Source of compressed data
    private int count = 0;              // How many times to repeat current value
    private byte value = 0;             // Value to repeat


    /**
     * Creates a new decompressor that reads from the given input stream
     */
    public MyDecompressorInputStream(InputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    /**
     * Reads and returns one byte of decompressed data.
     */
    @Override
    public int read() throws IOException {
        // Reading the next count-value pair
        if (count == 0) {
            int c = dataInputStream.read(); // How many times to repeat
            if (c < 0) return -1; // End of stream
            count = c;
            int v = dataInputStream.read(); // What value to repeat
            if (v < 0) throw new IOException("Incomplete compressed data - missing value byte\n");
            value = (byte) v;
        }
        count--;
        return value & 0xFF; // Return unsigned byte value
    }

    /**
     * Reads multiple bytes of decompressed data at once---> Faster than reading one byte at a time.
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        // Input checks:
        if (b == null) throw new NullPointerException();
        if (off < 0 || len < 0 || len > b.length - off) throw new IndexOutOfBoundsException();
        if (len == 0) return 0;
        int totalRead = 0;
        while (totalRead < len) {
            // Reading the next count-value pair
            if (count == 0) {
                int c = dataInputStream.read(); // How many times to repeat
                if (c < 0) break; // End of stream
                count = c;
                int v = dataInputStream.read(); // What value to repeat
                if (v < 0) throw new IOException("Incomplete compressed data - missing value byte");
                value = (byte) v;
            }
            // Fill as much of remaining space as we can with current value
            int repeatCount = Math.min(count, len - totalRead);
            Arrays.fill(b, off + totalRead, off + totalRead + repeatCount, value);
            totalRead += repeatCount;
            count -= repeatCount;
        }
        return totalRead == 0 ? -1 : totalRead;
    }

    /**
     * Closes this input stream and releases any system resources associated with it.
     */
    @Override
    public void close() throws IOException {
        dataInputStream.close();
    }
}