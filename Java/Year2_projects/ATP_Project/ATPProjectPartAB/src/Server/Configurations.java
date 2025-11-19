package Server;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class Configurations {
    private static final String configFile = "config.properties";
    private static Configurations configInstance;
    private final Properties configProperties;

    private Configurations() {
        configProperties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                System.err.println("Could not find configuration file: " + configFile);
                return;
            }
            configProperties.load(input);
        } catch (IOException e) {
            System.err.println("Could not load configuration file: " + configFile);
        }
    }

    public static synchronized Configurations getConfigFile() {
        if (configInstance == null) {
            configInstance = new Configurations();
        }
        return configInstance;
    }

    public int getNumOfThreadsInThreadPool() {
        String poolSize = configProperties.getProperty("threadPoolSize", "5");
        try {
            return Integer.parseInt(poolSize);
        } catch (NumberFormatException e) {
            return 5;
        }
    }

    public String getMazeGeneratingAlgorithm() {
        return configProperties.getProperty("mazeGeneratingAlgorithm", "MyMazeGenerator");
    }

    public String getMazeSearchingAlgorithm() {
        return configProperties.getProperty("mazeSearchingAlgorithm", "BreadthFirstSearch");
    }
}