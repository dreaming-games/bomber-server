package server;

import java.io.IOException;
import java.util.logging.*;

public class EasyLogger {
    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);

        // Write to std err
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SuperBasicFormatter());
        logger.addHandler(consoleHandler);

        // Write to log file
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("server.log", true);
            fileHandler.setFormatter(new SuperBasicFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logger;
    }

    private static class SuperBasicFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return "[" + record.getLoggerName() + "] - " +
                    record.getMessage() + "\n";
        }
    }
}
