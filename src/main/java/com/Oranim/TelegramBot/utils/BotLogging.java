package com.Oranim.TelegramBot.utils;

import com.Oranim.TelegramBot.messageHandler.Bot;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BotLogging {
    private static final Logger logger = Logger.getLogger(BotLogging.class.getName());

    public static void setInfoLog(String log){
        logger.log(Level.INFO,log);
    }

    public static void setCriticalLog(String log){
        logger.log(Level.WARNING,log);
    }
}
