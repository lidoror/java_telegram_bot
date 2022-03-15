package com.TelegramBot.keyboards;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InlineKeyboard {

    public InlineKeyboard() {}

    private InlineKeyboardButton setNewButton(String text,String callback){
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callback);
        return button;
    }
    private InlineKeyboardMarkup setNewKeyboard(List<List<InlineKeyboardButton>> rows){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    @SafeVarargs
    private List<List<InlineKeyboardButton>> setNewRows(List<InlineKeyboardButton>... lists){
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        Collections.addAll(keyboardRows, lists);
        return keyboardRows;
    }

    public void adminKeyboard(SendMessage message){
        List<InlineKeyboardButton> adminButton = new ArrayList<>();
        adminButton.add(setNewButton("ChatID","SendChatId.admin-" + message.getChatId()));
        adminButton.add(setNewButton("DBStatus","checkDBS.admin"));
        message.setReplyMarkup(setNewKeyboard(setNewRows(adminButton)));
    }

    public void monthlyCategory(SendMessage message){
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        firstRow.add(setNewButton("Fuel","monthlyCategory-Fuel"));
        firstRow.add(setNewButton("House","monthlyCategory-House Shopping"));
        secondRow.add(setNewButton("Shopping","monthlyCategory-Shopping"));
        secondRow.add(setNewButton("Food","monthlyCategory-Food"));
        thirdRow.add(setNewButton("General","monthlyCategory-General"));
        message.setReplyMarkup(setNewKeyboard(setNewRows(firstRow,secondRow,thirdRow)));
    }

    public void monthlySum(SendMessage message){
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        firstRow.add(setNewButton("Fuel","monthlySum-Fuel"));
        firstRow.add(setNewButton("House","monthlySum-House Shopping"));
        secondRow.add(setNewButton("Shopping","monthlySum-Shopping"));
        secondRow.add(setNewButton("Food","monthlySum-Food"));
        thirdRow.add(setNewButton("General","monthlySum-General"));
        thirdRow.add(setNewButton("All","monthlySum-All"));
        message.setReplyMarkup(setNewKeyboard(setNewRows(firstRow,secondRow,thirdRow)));
    }



    public void showMonths(SendMessage message){
        InlineKeyboardMarkup monthsMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        int year = 2022;

        for (int i = 1; i <= 12; i++){
            InlineKeyboardButton monthButton = new InlineKeyboardButton(i + "/" + year);
            monthButton.setCallbackData("monthDbCheck-"+i);
            buttons.add(monthButton);
            if (i % 4 == 0){
                rowList.add(buttons);
                buttons = new ArrayList<>();
            }
        }
        rowList.add(buttons);
        monthsMarkup.setKeyboard(rowList);
        message.setReplyMarkup(monthsMarkup);
    }

    public void showOlderMonthCategory(SendMessage message){
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        firstRow.add(setNewButton("Fuel","OlderMonth-Fuel"));
        firstRow.add(setNewButton("House","OlderMonth-House Shopping"));
        secondRow.add(setNewButton("Shopping","OlderMonth-Shopping"));
        secondRow.add(setNewButton("Food","OlderMonth-Food"));
        thirdRow.add(setNewButton("General","OlderMonth-General"));
        thirdRow.add(setNewButton("All","OlderMonth-All"));
        message.setReplyMarkup(setNewKeyboard(setNewRows(firstRow,secondRow,thirdRow)));
    }


}