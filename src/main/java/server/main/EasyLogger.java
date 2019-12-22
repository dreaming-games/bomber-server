package server.main;

import java.io.IOException;
import java.util.logging.*;

class EasyLogger {
    private static FileHandler fileHandler;

    static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);

        // Write to std err
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SuperBasicFormatter());
        logger.addHandler(consoleHandler);

        // Write to log file
        try {
            if (fileHandler == null) {
                fileHandler = new FileHandler("server.log", false);
                fileHandler.setFormatter(new SuperBasicFormatter());
            }
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logger;
    }

    private static class SuperBasicFormatter extends Formatter {
//        private final GameHandler runner;
//        private SuperBasicFormatter(GameHandler runner) {
//            this.runner = runner;
//        }

        @Override
        public String format(LogRecord record) {
            // Todo: show amount of games running in every log?
            // "(" + runner.runningGames() + (runner.joiningGame() ? "^" : "~") + ") ";
            return "[" + record.getLevel() + ", " + record.getLoggerName() + "] - " + record.getMessage() + "\n";
        }
    }
}
