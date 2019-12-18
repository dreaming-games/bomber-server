package server.main;

import server.manager.GameRunner;

import java.io.IOException;
import java.util.logging.*;

public class EasyLogger {
    public static Logger getLogger(String name, GameRunner gameRunner) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);

        // Write to std err
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SuperBasicFormatter(gameRunner));
        logger.addHandler(consoleHandler);

        // Write to log file
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("server.log", false);
            fileHandler.setFormatter(new SuperBasicFormatter(gameRunner));
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logger;
    }

    private static class SuperBasicFormatter extends Formatter {
        private final GameRunner runner;
        private SuperBasicFormatter(GameRunner runner) {
            this.runner = runner;
        }

        @Override
        public String format(LogRecord record) {
            return "(" + runner.runningGames() + (runner.joiningGame() ? "^" : "~") + ") ["
                    + record.getLevel() + ", " + record.getLoggerName() + "] - " + record.getMessage() + "\n";
        }
    }
}
