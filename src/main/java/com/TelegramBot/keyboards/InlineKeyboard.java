package com.TelegramBot.keyboards;

import com.TelegramBot.db.DatabaseFilter;
import com.TelegramBot.utils.Const;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {

    public InlineKeyboard() {}



    //returns admin keyboard with the ability to see db status and current session chat id
    public void adminKeyboard(SendMessage message){
        List<InlineKeyboardButton> adminButton = new ArrayList<>();
        adminButton.add(KeyboardBuilders.createNewKeyboardButton("ChatID","SendChatId.admin"+ Const.INLINE_SEPARATOR + message.getChatId()));
        adminButton.add(KeyboardBuilders.createNewKeyboardButton("DBStatus","checkDBS.admin"));
        message.setReplyMarkup(KeyboardBuilders.createNewKeyboardFromRows(KeyboardBuilders.createNewKeyboardRows(adminButton)));
    }

    public void monthlyExpensesInlineButton(SendMessage message){
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("Fuel","monthlyCategory-Fuel"));
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("House","monthlyCategory-House Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Shopping","monthlyCategory-Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Food","monthlyCategory-Food"));
        thirdRow.add(KeyboardBuilders.createNewKeyboardButton("General","monthlyCategory-General"));
        message.setReplyMarkup(KeyboardBuilders.createNewKeyboardFromRows(KeyboardBuilders.createNewKeyboardRows(firstRow,secondRow,thirdRow)));
    }
    //return inline keyboard with the total spending of each category
    public void monthlySum(SendMessage message){
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("Fuel","monthlySum-Fuel"));
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("House","monthlySum-House Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Shopping","monthlySum-Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Food","monthlySum-Food"));
        thirdRow.add(KeyboardBuilders.createNewKeyboardButton("General","monthlySum-General"));
        thirdRow.add(KeyboardBuilders.createNewKeyboardButton("All","monthlySum-All"));
        message.setReplyMarkup(KeyboardBuilders.createNewKeyboardFromRows(KeyboardBuilders.createNewKeyboardRows(firstRow,secondRow,thirdRow)));
    }


    //showing the months in this year and make inline keyboard for each month
    public void showMonthsIn2022(SendMessage message){
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
    //made  for future use suppose to show transaction from older months
    public void showOlderMonthCategory(SendMessage message){
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("Fuel","OlderMonth-Fuel"));
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("House","OlderMonth-House Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Shopping","OlderMonth-Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Food","OlderMonth-Food"));
        thirdRow.add(KeyboardBuilders.createNewKeyboardButton("General","OlderMonth-General"));
        thirdRow.add(KeyboardBuilders.createNewKeyboardButton("All","OlderMonth-All"));
        message.setReplyMarkup(KeyboardBuilders.createNewKeyboardFromRows(KeyboardBuilders.createNewKeyboardRows(firstRow,secondRow,thirdRow)));
    }
    //template for turning transaction into inline keyboard fields
    private void showTransactionAsInline(SendMessage message , DatabaseFilter databaseFilter)throws SQLException {
        InlineKeyboardMarkup monthsMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();


        for (int i = 0; i < databaseFilter.getCurrentMonthProducts().size(); i++){

            String buttonTextFormat = databaseFilter.getCurrentMonthProducts().get(i)+" "+databaseFilter.getCurrentMonthPrices().get(i);

            if (i % 3 == 0 && i != 0){
                rowList.add(buttons);
                buttons = new ArrayList<>();
            }
            buttons.add(KeyboardBuilders.createNewKeyboardButton(buttonTextFormat,"GetTransactionInPlace-"));

        }
        rowList.add(buttons);
        monthsMarkup.setKeyboard(rowList);
        message.setReplyMarkup(monthsMarkup);
    }



   

 





}