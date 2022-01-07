package com.TelegramBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private final String botToken = "***REMOVED***";
    private final String botName = "MYBionicBot";

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        Execution exe = new Execution();
        String command;

        if (update.getMessage() != null) {
            command = update.getMessage().getText();
            message.setChatId(String.valueOf(update.getMessage().getChatId()));
        }
        else {
            command = update.getCallbackQuery().getData();
            message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        }

        exe.messageHandler(command, message);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
