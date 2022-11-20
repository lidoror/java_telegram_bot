package com.Oranim.TelegramBot.messageHandler;

import com.Oranim.TelegramBot.utils.BotLogging;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
    private final String botName = System.getenv("BOT_NAME");
    private final String botToken = System.getenv("BOT_TOKEN");
    private final List<String> approvedChats = List.of(System.getenv("APPROVED_CHATS").split(","));


    public Bot() {}


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
        Execution execution = new Execution();
        BotApiMethod messageToReturn = message;
        String command;
        boolean updateHasMessage = update.getMessage() != null;

        if (updateHasMessage) {
            command = update.getMessage().getText();
            message.setChatId(String.valueOf(update.getMessage().getChatId()));
        } else {
            command = update.getCallbackQuery().getData();
            message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        }


        if (approvedChats.contains(message.getChatId())) {
            BotLogging.setInfoLog(classLog(message.getChatId(),"approved",Bot.class.getName()));
            messageToReturn = execution.messageDispatcher(command, message, update);


        } else {
            BotLogging.setInfoLog(classLog(message.getChatId(),"dissapproved",Bot.class.getName()));
            message.setText("Sorry some problem occurred");
        }

        try {
            execute(messageToReturn);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

    private String classLog(String logParameters , String approval , String className){
        return "Chat number %s was %s at class %s".formatted(logParameters,approval,className);
    }
}