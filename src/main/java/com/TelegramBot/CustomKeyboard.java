package com.TelegramBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class CustomKeyboard {

    public void keyboard1(SendMessage message){
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

    List<KeyboardRow> keyboard = new ArrayList<>();

    KeyboardRow row = new KeyboardRow();
        row.add("Balance");
        row.add("Expenses");
        row.add("Refund");
        keyboard.add(row);

    row = new KeyboardRow();
        row.add("Overall Expenses");
        row.add("All Expenses");
        row.add("All Refunds");
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);;


}
}
