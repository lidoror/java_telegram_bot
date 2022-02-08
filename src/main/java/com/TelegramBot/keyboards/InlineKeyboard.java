package com.TelegramBot.keyboards;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {

    public InlineKeyboard() {}


    public void adminKeyboard(SendMessage message){

        InlineKeyboardMarkup adminInlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> adminKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> adminButton = new ArrayList<>();

        InlineKeyboardButton chatID = new InlineKeyboardButton("ChatID");
        chatID.setCallbackData("SendChatId.admin-" + message.getChatId());
        adminButton.add(chatID);

        InlineKeyboardButton dbStatus = new InlineKeyboardButton("DBStatus");
        dbStatus.setCallbackData("checkDBS.admin");
        adminButton.add(dbStatus);

        InlineKeyboardButton ksp = new InlineKeyboardButton("ksp");
        ksp.setUrl("https://ksp.co.il/web/");
        adminKeyboard.add(adminButton);

        adminInlineKeyboardMarkup.setKeyboard(adminKeyboard);
        message.setReplyMarkup(adminInlineKeyboardMarkup);
    }

    public void monthKeyboard(SendMessage message){
        InlineKeyboardMarkup monthKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsList = new ArrayList<>();
        List<InlineKeyboardButton> monthRows = new ArrayList<>();

        for (int i = 1; i <= 12; i++ ){
           InlineKeyboardButton monthNum = new InlineKeyboardButton(String.valueOf(i));
           monthNum.setCallbackData(String.valueOf(i));
            monthRows.add(monthNum);
           if (i % 3 == 0){
               rowsList.add(monthRows);
               monthRows = new ArrayList<>();
           }
        }
        monthKeyboardMarkup.setKeyboard(rowsList);
        message.setReplyMarkup(monthKeyboardMarkup);
    }

    public void dayKeyboard(SendMessage message){
        InlineKeyboardMarkup daysKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsList = new ArrayList<>();
        List<InlineKeyboardButton> daysRows = new ArrayList<>();

    }
}