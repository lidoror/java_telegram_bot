package com.oranim.telegrambot;

import com.oranim.telegrambot.db.MessagesDAO;
import com.oranim.telegrambot.messageHandler.Bot;
import com.oranim.telegrambot.utils.BotLogging;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {

    public static void main(String[] args) {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
            BotLogging.setInfoLog("_____Bot is Starting_____");
        } catch (TelegramApiException e) {
            BotLogging.setInfoLog("_____Bot Failed To Start_____");
            e.printStackTrace();
        }catch (Exception e){
            BotLogging.setInfoLog("_____Bot Failed To Start_____");
            System.out.println(e.getMessage());

        }
    }


}

