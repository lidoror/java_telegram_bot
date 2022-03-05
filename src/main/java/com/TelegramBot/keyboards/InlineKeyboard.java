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

    public void monthlyCategory(SendMessage message){
        InlineKeyboardMarkup monthlyCategory = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsSecondRow = new ArrayList<>();
        List<InlineKeyboardButton> buttonThirdRow = new ArrayList<>();

        InlineKeyboardButton fuel = new InlineKeyboardButton("Fuel");
        fuel.setCallbackData("monthlyCategory.fuel");
        buttons.add(fuel);

        InlineKeyboardButton homeShopping = new InlineKeyboardButton("House");
        homeShopping.setCallbackData("monthlyCategory.homeShopping");
        buttons.add(homeShopping);

        InlineKeyboardButton internetShopping = new InlineKeyboardButton("Shopping");
        internetShopping.setCallbackData("monthlyCategory.internetShopping");
        buttonsSecondRow.add(internetShopping);


        InlineKeyboardButton food = new InlineKeyboardButton("Food");
        food.setCallbackData("monthlyCategory.Food");
        buttonsSecondRow.add(food);

        InlineKeyboardButton general = new InlineKeyboardButton("General");
        general.setCallbackData("monthlyCategory.General");
        buttonThirdRow.add(general);

        InlineKeyboardButton allMonthlyExpenses = new InlineKeyboardButton("All");
        allMonthlyExpenses.setCallbackData("monthlyCategory.All");
        buttonThirdRow.add(allMonthlyExpenses);

        rowList.add(buttons);
        rowList.add(buttonsSecondRow);
        rowList.add(buttonThirdRow);
        monthlyCategory.setKeyboard(rowList);
        message.setReplyMarkup(monthlyCategory);

    }

    public void monthlySum(SendMessage message){
        InlineKeyboardMarkup monthlySum = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsSecondRow = new ArrayList<>();
        List<InlineKeyboardButton> buttonThirdRow = new ArrayList<>();

        InlineKeyboardButton fuel = new InlineKeyboardButton("Fuel");
        fuel.setCallbackData("monthlySum.fuel");
        buttons.add(fuel);

        InlineKeyboardButton homeShopping = new InlineKeyboardButton("House");
        homeShopping.setCallbackData("monthlySum.homeShopping");
        buttons.add(homeShopping);

        InlineKeyboardButton internetShopping = new InlineKeyboardButton("Shopping");
        internetShopping.setCallbackData("monthlySum.internetShopping");
        buttonsSecondRow.add(internetShopping);


        InlineKeyboardButton food = new InlineKeyboardButton("Food");
        food.setCallbackData("monthlySum.Food");
        buttonsSecondRow.add(food);

        InlineKeyboardButton general = new InlineKeyboardButton("General");
        general.setCallbackData("monthlySum.General");
        buttonThirdRow.add(general);

        InlineKeyboardButton allMonthlyExpenses = new InlineKeyboardButton("All");
        allMonthlyExpenses.setCallbackData("monthlySum.All");
        buttonThirdRow.add(allMonthlyExpenses);

        rowList.add(buttons);
        rowList.add(buttonsSecondRow);
        rowList.add(buttonThirdRow);
        monthlySum.setKeyboard(rowList);
        message.setReplyMarkup(monthlySum);

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
}