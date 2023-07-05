package com.oranim.telegrambot.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BotLogging {
    private static final Logger logger = Logger.getLogger(BotLogging.class.getName());

    public static void setInfoLog(String log){
        logger.log(Level.INFO,"\n" + "\u001B[32m" + log + "\u001B[0m" + "\n");

    }

    public static void setCriticalLog(String log){
        logger.log(Level.WARNING,"\n" + "\u001B[31m" + log + "\u001B[0m" + "\n");
    }
}
