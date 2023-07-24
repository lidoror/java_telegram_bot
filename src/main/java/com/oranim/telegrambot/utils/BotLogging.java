package com.oranim.telegrambot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BotLogging {
    private static final Logger logger = LoggerFactory.getLogger(BotLogging.class);

    private static void setInfoLog(String log){
        logger.info("\n \u001B[32m %s \u001B[0m \n".formatted(log));
    }

    private static void setWarnLog (String log) {
        logger.info("\n \u001B[33m %s \u001B[0m \n".formatted(log));
    }

    private static void setCriticalLog(String log){
        logger.warn("\n \u001B[31m %s \u001B[0m \n".formatted(log));
    }

    public static void createLog(LogWarningLevel logStatus , String className, String methodName , String description) {
        String log = "%s log-> \nClass name: [%s] \nMethod name: [%s] \nDescription: [%s]".formatted(logStatus,className, methodName, description);

        switch (logStatus) {
            case WARN -> setWarnLog(log);
            case CRITICAL -> setCriticalLog(log);
            case INFO -> setInfoLog(log);
        }

    }
}
