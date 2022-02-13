package com.TelegramBot.codeHandler;

import com.TelegramBot.codeHandler.Execution;
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
        if (message.getChatId().equals("561947096") || message.getChatId().equals("1072526175")|| message.getChatId().equals("-686089090"))
            exe.messageHandler(command, message);
        else
            message.setText("A problem accrued");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
