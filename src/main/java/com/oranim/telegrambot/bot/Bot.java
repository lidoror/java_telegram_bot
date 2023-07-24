package com.oranim.telegrambot.bot;

import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.LogWarningLevel;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
    private final String botName = System.getenv("BOT_NAME");
    private final String botToken = System.getenv("BOT_TOKEN");
    private final List<String> approvedChats = Arrays.stream(System.getenv("APPROVED_CHATS")
                    .split(",")).map(String::trim).toList();

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
        String chatId = update.getMessage() != null ? update.getMessage().getChatId().toString() : update.getCallbackQuery().getMessage().getChatId().toString();
        String updateText = update.getMessage() != null ? update.getMessage().getText() : update.getCallbackQuery().getData();

        SendMessage message = new SendMessage();

        @SuppressWarnings("rawtypes")
        BotApiMethod messageToReturn = message;
        message.setChatId(chatId);

        if (approvedChats.contains(chatId)) {
            BotLogging.createLog(LogWarningLevel.INFO,Bot.class.getName(),"updateDB", "chatId [%s] approved".formatted(chatId));
            messageToReturn = new Dispatcher().messageDispatcher(updateText, message, update);

        } else {
            BotLogging.createLog(LogWarningLevel.WARN,Bot.class.getName(),"updateDB", "chatId [%s] disapproved".formatted(chatId));
            message.setText("Sorry some problem occurred");
        }

        try {
            execute(messageToReturn);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

}