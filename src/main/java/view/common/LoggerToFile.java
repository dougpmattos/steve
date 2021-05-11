package view.common;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerToFile {

    public Logger logger;
    FileHandler logFile;
    private static LoggerToFile instance;

    public static LoggerToFile getInstance() {

        if (LoggerToFile.instance == null){
            LoggerToFile.instance = new LoggerToFile();
        }

        return instance;

    }

    public LoggerToFile(){

        logger = Logger.getLogger("steve_log");

        try {

            logFile = new FileHandler("SteveLog.log", true);
            logger.addHandler(logFile);
            SimpleFormatter formatter = new SimpleFormatter();
            logFile.setFormatter(formatter);

        } catch (IOException e) {
            logger.severe(e.getMessage());
        }

    }

    public Logger getLogger() {
        return logger;
    }

}
