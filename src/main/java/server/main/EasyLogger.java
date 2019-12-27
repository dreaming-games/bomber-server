package server.main;

import java.io.IOException;
import java.util.logging.*;

public class EasyLogger {
    private static ConsoleHandler consoleHandler;
    private static FileHandler fileHandler;

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);

        // Write to std err
        if (consoleHandler == null) {
            consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SuperBasicFormatter());
        }
        logger.addHandler(consoleHandler);

        // Write to log file
        try {
            if (fileHandler == null) {
                fileHandler = new FileHandler("server.log", false);
                fileHandler.setFormatter(new SuperBasicFormatter());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fileHandler);
        return logger;
    }

    private static class SuperBasicFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return "[" + record.getLevel() + ", " + record
                    .getLoggerName() + "] - " + record
                    .getMessage() + "\n";
        }
    }
}
