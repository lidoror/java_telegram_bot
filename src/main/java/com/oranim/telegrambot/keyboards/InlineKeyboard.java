package com.oranim.telegrambot.keyboards;

import com.oranim.telegrambot.db.ShoppingMgmtRecord;
import com.oranim.telegrambot.utils.Const;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;


public class InlineKeyboard {

    public InlineKeyboard() {
    }

    private final int startYear = Integer.parseInt(System.getenv("START_YEAR"));

    /**
     * generate 12 inline keys for the months of the year we pass in
     * @param year we want to generate the key for
     * @return InlineKeyboardMarkup which is the 12 keys
     */
    public InlineKeyboardMarkup generateMonthsInKeyboardMarkup(int year) {
        InlineKeyboardMarkup monthsMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

            List<InlineKeyboardButton> buttons = new ArrayList<>();

            for (int i = 1; i <= 12; i++) {
                String textButton = i + "/" + year;
                String callbackData = "monthDbCheck" + Const.DOUBLE_SEMICOLON_SEPARATOR + i + Const.DOUBLE_SEMICOLON_SEPARATOR + year;

                buttons.add(KeyboardBuilders.createNewKeyboardButton(textButton, callbackData));

                if (i % 4 == 0) {
                    rowList.add(buttons);
                    buttons = new ArrayList<>();
                }
            }
            rowList.add(buttons);
            monthsMarkup.setKeyboard(rowList);



        return monthsMarkup;
    }

    /**
     * this function generates the keys for the years buttons
     * @return InlineKeyboardMarkup of the years buttons
     */
    public InlineKeyboardMarkup generateYearsKeyboardMarkup() {
        InlineKeyboardMarkup yearsMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        generateYearsForInlineMarkup(startYear).forEach(year -> {
            String callbackData = "GET_YEAR" + Const.DOUBLE_SEMICOLON_SEPARATOR + year;
            buttons.add(KeyboardBuilders.createNewKeyboardButton(year.toString(), callbackData));
        });
        rowList.add(buttons);
        yearsMarkup.setKeyboard(rowList);

        return yearsMarkup;
    }

    /**
     * this function generates the years from a specific year till today
     * @param startYear is the year we want to start generating the years
     * @return a list of integers representing the years
     */
    private List<Integer> generateYearsForInlineMarkup(int startYear) {
        int currentYear = Year.now().getValue();
        List<Integer> years = new ArrayList<>();

        for (int i = startYear; i <= currentYear; i++) {
            years.add(i);
        }
        return years;
    }


    /**
     * this method generate keyboard with admin keys only like check db connection
     * @param message is the message from the user
     * @return inline that contain only admin keys
     */
    public InlineKeyboardMarkup adminKeyboardMarkup(SendMessage message) {
        List<InlineKeyboardButton> adminButton = new ArrayList<>();
        adminButton.add(KeyboardBuilders.createNewKeyboardButton("ChatID", "SendChatId.admin" + Const.DOUBLE_SEMICOLON_SEPARATOR + message.getChatId()));
        adminButton.add(KeyboardBuilders.createNewKeyboardButton("DBStatus", "checkDBS.admin"));
        return KeyboardBuilders.createNewKeyboardFromRows(KeyboardBuilders.createNewKeyboardRows(adminButton));
    }

    /**
     * this keyboard is the shopping categories
     * @return inline keyboard with the categories we have
     */
    public InlineKeyboardMarkup monthlyExpensesInlineButtonMarkup() {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("Fuel", "monthlyCategory-Fuel"));
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("House", "monthlyCategory-House Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Shopping", "monthlyCategory-Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Food", "monthlyCategory-Food"));
        thirdRow.add(KeyboardBuilders.createNewKeyboardButton("General", "monthlyCategory-General"));
        thirdRow.add(KeyboardBuilders.createNewKeyboardButton("All", "monthlyCategory-All"));
        return KeyboardBuilders.createNewKeyboardFromRows(KeyboardBuilders.createNewKeyboardRows(firstRow, secondRow, thirdRow));
    }

    /**
     * this keyboard incharge of showing the sum of money spent on each category
     * @return inline keyboard with the sum of each category
     */
    public InlineKeyboardMarkup monthlySumKeyboardMarkup() {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("Fuel", "monthlySum-Fuel"));
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("House", "monthlySum-House Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Shopping", "monthlySum-Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Food", "monthlySum-Food"));
        thirdRow.add(KeyboardBuilders.createNewKeyboardButton("General", "monthlySum-General"));
        thirdRow.add(KeyboardBuilders.createNewKeyboardButton("All", "monthlySum-All"));
        return KeyboardBuilders.createNewKeyboardFromRows(KeyboardBuilders.createNewKeyboardRows(firstRow, secondRow, thirdRow));
    }




    /**
     * @param list of shopping products
     * @return the list as inline keyboard
     */
    public InlineKeyboardMarkup listToTransactionInline(List<ShoppingMgmtRecord> list) {
        InlineKeyboardMarkup monthsMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        List<String> products = list.stream().map(ShoppingMgmtRecord::product).toList();
        List<String> prices = list.stream().map(ShoppingMgmtRecord::price).toList();
        List<Integer> columID = list.stream().map(ShoppingMgmtRecord::columID).toList();


        for (int i = 0; i < list.size(); i++) {

            String buttonTextFormat = products.get(i) + " " + prices.get(i);

            if (i % 3 == 0 && i != 0) {
                rowList.add(buttons);
                buttons = new ArrayList<>();
            }
            buttons.add(KeyboardBuilders.createNewKeyboardButton(buttonTextFormat, "GetTransactionInPlace" + Const.DOUBLE_SEMICOLON_SEPARATOR + columID.get(i)));

        }
        rowList.add(buttons);
        monthsMarkup.setKeyboard(rowList);
        return monthsMarkup;
    }


}