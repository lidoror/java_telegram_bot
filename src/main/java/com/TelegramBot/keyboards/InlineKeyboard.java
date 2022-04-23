package com.TelegramBot.keyboards;

import com.TelegramBot.db.MariaDB;
import com.TelegramBot.db.ShoppingMgmtRecord;
import com.TelegramBot.utils.Const;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InlineKeyboard {

    public InlineKeyboard() {}


    private InlineKeyboardButton setNewButton(String text,String callback){
        return InlineKeyboardButton.builder().text(text).callbackData(callback).build();
    }
    private InlineKeyboardMarkup setNewKeyboard(List<List<InlineKeyboardButton>> rows){
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    @SafeVarargs
    private List<List<InlineKeyboardButton>> setNewRows(List<InlineKeyboardButton>... lists){
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        Collections.addAll(keyboardRows, lists);
        return keyboardRows;
    }
    //returns admin keyboard with the ability to see db status and current session chat id
    public void adminKeyboard(SendMessage message){
        List<InlineKeyboardButton> adminButton = new ArrayList<>();
        adminButton.add(setNewButton("ChatID","SendChatId.admin"+ Const.INLINE_SEPARATOR + message.getChatId()));
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
    //return inline keyboard with the total spending of each category
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


    //showing the months in this year and make inline keyboard for each month
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
    //made  for future use suppose to show transaction from older months
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
    //template for turning transaction into inline keyboard fields
    private void showTransactionAsInline(SendMessage message)throws SQLException {
        MariaDB db = new MariaDB();
        InlineKeyboardMarkup monthsMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();


        for (int i = 0; i < db.getCurrentMonthProducts().size(); i++){

            String buttonTextFormat = db.getCurrentMonthProducts().get(i)+" "+db.getCurrentMonthPrices().get(i);

            if (i % 3 == 0 && i != 0){
                rowList.add(buttons);
                buttons = new ArrayList<>();
            }
            buttons.add(setNewButton(buttonTextFormat,"GetTransactionInPlace-"));

        }
        rowList.add(buttons);
        monthsMarkup.setKeyboard(rowList);
        message.setReplyMarkup(monthsMarkup);
    }

    public void monthlyExpensesKeyboard(SendMessage message)throws SQLException {
        MariaDB db = new MariaDB();
        InlineKeyboardMarkup monthsMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        List<String> products = db.getCurrentMonthShoppingList().stream().map(ShoppingMgmtRecord::product).toList();
        //not in use now need to check with the client if this field stay or moving to delete
        List<Integer> IDs = db.getCurrentMonthShoppingList().stream().map(ShoppingMgmtRecord::columID).toList();

        for (int i = 0; i < db.getCurrentMonthProducts().size(); i++){

            String buttonTextFormat = products.get(i);

            if (i % 3 == 0 && i != 0){
                rowList.add(buttons);
                buttons = new ArrayList<>();
            }
            buttons.add(setNewButton(buttonTextFormat,"GetTransactionInPlace"+Const.INLINE_SEPARATOR+IDs.get(i)));

        }
        rowList.add(buttons);
        monthsMarkup.setKeyboard(rowList);
        message.setReplyMarkup(monthsMarkup);
    }

   

 





}