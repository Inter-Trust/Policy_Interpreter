package es.umu.intertrust.policyinterpreter.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public class LoggingUtils {

    private static final Logger logger = Logger.getLogger(LoggingUtils.class.getName());

    public static final String DEFAULT_PATH = "/logging.properties";

    public static void readConfiguration() {
        readConfiguration(DEFAULT_PATH);
    }

    public static void readConfiguration(String path) {
        try {
            InputStream configuration = LoggingUtils.class.getResourceAsStream(path);
            if (configuration != null) {
                LogManager.getLogManager().readConfiguration(configuration);
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Unable to read logging configuration: {0}", ex.getMessage());
        } catch (SecurityException ex) {
            logger.log(Level.WARNING, "Unable to read logging configuration: {0}", ex.getMessage());
        }
    }
}
