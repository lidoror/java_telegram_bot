package com.TelegramBot.keyboards;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class EditedInlines {

    public EditMessageText createEditMessageText(String text, InlineKeyboardMarkup inlineKeyboardMarkup, Update update) {
        return EditMessageText.builder().text(text).
                replyMarkup(inlineKeyboardMarkup).
                chatId(update.getCallbackQuery().getMessage().getChatId().toString()).
                messageId(update.getCallbackQuery().getMessage().getMessageId())
                .build();

    }



}
