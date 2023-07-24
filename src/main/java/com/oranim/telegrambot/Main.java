package com.oranim.telegrambot;

import com.oranim.telegrambot.bot.Bot;
import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.LogWarningLevel;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {

    public static void main(String[] args) {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
            BotLogging.createLog(LogWarningLevel.INFO,Main.class.getName(),"main","_____Bot is Starting_____");

        } catch (TelegramApiException e) {
            BotLogging.createLog(LogWarningLevel.INFO,Main.class.getName(),"main","_____Bot Failed To Start_____");
            e.printStackTrace();
        }
    }


}

