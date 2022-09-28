package com.Oranim.TelegramBot.messageHandler;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


public class Bot extends TelegramLongPollingBot {
    private final String botToken = "***REMOVED***";
    private final String botName = "MYBionicBot";
    private final String betaBotToken = "***REMOVED***";
    private final String betaBotName = "BetaTestingBot";
    private final List<String> approvedChats = List.of(***REMOVED***);

    public Bot()  {
    }


    @Override
    public String getBotUsername() {
        return betaBotName;
    }

    @Override
    public String getBotToken() {
        return betaBotToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        Execution execution = new Execution();
        BotApiMethod messageToReturn = message;
        String command;
        boolean updateHasMessage = update.getMessage() != null;

        if (updateHasMessage) {
            command = update.getMessage().getText();
            message.setChatId(String.valueOf(update.getMessage().getChatId()));
        }
        else {
            command = update.getCallbackQuery().getData();
            message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        }


        if (approvedChats.contains(message.getChatId()))
            messageToReturn = execution.messageDispatcher(command, message,update);

        else
            message.setText("Sorry some problem occurred");


        try {
            execute(messageToReturn);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}