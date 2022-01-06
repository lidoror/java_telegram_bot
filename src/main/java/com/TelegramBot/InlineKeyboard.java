package com.TelegramBot;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class InlineKeyboard {

    InlineKeyboard () {}


    public void adminKeyboard(SendMessage message){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> button = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow2 = new ArrayList<>();

        InlineKeyboardButton youtube = new InlineKeyboardButton("ChatID");

        youtube.setCallbackData(message.getChatId());

        button.add(youtube);

        InlineKeyboardButton github = new InlineKeyboardButton("github");

        github.setUrl("https://github.com");

        button.add(github);

        InlineKeyboardButton ksp = new InlineKeyboardButton("ksp");
        ksp.setUrl("https://ksp.co.il/web/");
        buttonRow2.add(ksp);

        keyboard.add(button);
        keyboard.add(buttonRow2);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
    }
}