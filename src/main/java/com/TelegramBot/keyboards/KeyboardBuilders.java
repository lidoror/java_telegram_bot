package com.TelegramBot.keyboards;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyboardBuilders {

    public static EditMessageText createEditMessageInline(String text, InlineKeyboardMarkup inlineKeyboardMarkup, Update update) {
        return EditMessageText.builder().text(text).
                replyMarkup(inlineKeyboardMarkup).
                chatId(update.getCallbackQuery().getMessage().getChatId().toString()).
                messageId(update.getCallbackQuery().getMessage().getMessageId())
                .build();

    }
    public static EditMessageText createEditMessageText(String text,Update update){
        return EditMessageText.builder().text(text)
                .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .build();
    }


    public static InlineKeyboardButton createNewKeyboardButton(String text, String callback){
        return InlineKeyboardButton.builder().text(text).callbackData(callback).build();
    }
    public static InlineKeyboardMarkup createNewKeyboardFromRows(List<List<InlineKeyboardButton>> rows){
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    @SafeVarargs
    public static List<List<InlineKeyboardButton>> createNewKeyboardRows(List<InlineKeyboardButton>... lists){
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        Collections.addAll(keyboardRows, lists);
        return keyboardRows;
    }

    public static void sendKeyboardToUser(SendMessage message,InlineKeyboardMarkup inlineKeyboardMarkup){
        message.setReplyMarkup(inlineKeyboardMarkup);
    }


}
