package com.oranim.telegrambot.keyboards;

import com.oranim.telegrambot.db.DatabaseListAction;
import com.oranim.telegrambot.db.ShoppingMgmtRecord;
import com.oranim.telegrambot.utils.Const;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InlineKeyboard {

    public InlineKeyboard() {
    }
    //TODO fix this
    //showing the months in this year and make inline keyboard for each month
    public InlineKeyboardMarkup generateMonthsInKeyboardMarkup(int year) {
        InlineKeyboardMarkup monthsMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

            List<InlineKeyboardButton> buttons = new ArrayList<>();

            for (int i = 1; i <= 12; i++) {
                String textButton = i + "/" + year;
                String callbackData = "monthDbCheck" + Const.SEPARATOR + i + Const.SEPARATOR + year;

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

    public InlineKeyboardMarkup generateYearsKeyboardMarkup() {
        InlineKeyboardMarkup yearsMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        generateYearsForInlineMarkup(2022).forEach(year -> {
            String callbackData = "GET_YEAR" + Const.SEPARATOR + year;
            buttons.add(KeyboardBuilders.createNewKeyboardButton(year.toString(), callbackData));
        });
        rowList.add(buttons);
        yearsMarkup.setKeyboard(rowList);

        return yearsMarkup;
    }


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
     *
     * @param message is the message from the user
     * @return inline that contain only admin keys
     */
    public InlineKeyboardMarkup adminKeyboardMarkup(SendMessage message) {
        List<InlineKeyboardButton> adminButton = new ArrayList<>();
        adminButton.add(KeyboardBuilders.createNewKeyboardButton("ChatID", "SendChatId.admin" + Const.SEPARATOR + message.getChatId()));
        adminButton.add(KeyboardBuilders.createNewKeyboardButton("DBStatus", "checkDBS.admin"));
        return KeyboardBuilders.createNewKeyboardFromRows(KeyboardBuilders.createNewKeyboardRows(adminButton));
    }

    /**
     * this keyboard is the shopping categories
     *
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
     *
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


    //made  for future use suppose to show transaction from older months
    public void showOlderMonthCategory(SendMessage message) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("Fuel", "OlderMonth-Fuel"));
        firstRow.add(KeyboardBuilders.createNewKeyboardButton("House", "OlderMonth-House Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Shopping", "OlderMonth-Shopping"));
        secondRow.add(KeyboardBuilders.createNewKeyboardButton("Food", "OlderMonth-Food"));
        thirdRow.add(KeyboardBuilders.createNewKeyboardButton("General", "OlderMonth-General"));
        thirdRow.add(KeyboardBuilders.createNewKeyboardButton("All", "OlderMonth-All"));
        message.setReplyMarkup(KeyboardBuilders.createNewKeyboardFromRows(KeyboardBuilders.createNewKeyboardRows(firstRow, secondRow, thirdRow)));
    }

    //template for turning transaction into inline keyboard fields
    private void showTransactionAsInline(SendMessage message, DatabaseListAction databaseListAction) throws SQLException {
        InlineKeyboardMarkup monthsMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();


        for (int i = 0; i < databaseListAction.getCurrentMonthProducts().size(); i++) {

            String buttonTextFormat = databaseListAction.getCurrentMonthProducts().get(i) + " " + databaseListAction.getCurrentMonthPrices().get(i);

            if (i % 3 == 0 && i != 0) {
                rowList.add(buttons);
                buttons = new ArrayList<>();
            }
            buttons.add(KeyboardBuilders.createNewKeyboardButton(buttonTextFormat, "GetTransactionInPlace-"));

        }
        rowList.add(buttons);
        monthsMarkup.setKeyboard(rowList);
        message.setReplyMarkup(monthsMarkup);
    }

    /**
     * @param list of shopping products
     * @return the list as inline keyboard
     * @throws SQLException
     */
    public InlineKeyboardMarkup listToTransactionInline(List<ShoppingMgmtRecord> list) throws SQLException {
        InlineKeyboardMarkup monthsMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        List<String> products = list.stream().map(ShoppingMgmtRecord::product).collect(Collectors.toList());
        List<String> prices = list.stream().map(ShoppingMgmtRecord::price).collect(Collectors.toList());
        List<Integer> productID = list.stream().map(ShoppingMgmtRecord::columID).collect(Collectors.toList());


        for (int i = 0; i < list.size(); i++) {

            String buttonTextFormat = products.get(i) + " " + prices.get(i);

            if (i % 3 == 0 && i != 0) {
                rowList.add(buttons);
                buttons = new ArrayList<>();
            }
            buttons.add(KeyboardBuilders.createNewKeyboardButton(buttonTextFormat, "GetTransactionInPlace" + Const.SEPARATOR + productID.get(i)));

        }
        rowList.add(buttons);
        monthsMarkup.setKeyboard(rowList);
        return monthsMarkup;
    }


}