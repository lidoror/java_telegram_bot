package com.oranim.telegrambot.utils;

import com.oranim.telegrambot.InputExtractor.PriceInputExtractor;
import com.oranim.telegrambot.balanceMgmt.Balance;
import com.oranim.telegrambot.db.MessagesService;
import com.oranim.telegrambot.db.IDatabase;
import com.oranim.telegrambot.keyboards.InlineKeyboard;
import com.oranim.telegrambot.keyboards.KeyboardBuilders;
import org.apache.commons.lang3.math.NumberUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FunctionsUtils {



    public static boolean stringContainNumber(String str) {
        return NumberUtils.isDigits(str);
    }


    public static EditMessageText monthlyCategoryButtonsDispatcher
            (String command, Update update, MessagesService messagesService) throws SQLException {
        InlineKeyboard inlineKeyboard = new InlineKeyboard();
        String category = command.split("-")[1];
        String identifier = command.split("-")[0];
        String textFormat = category + ":\n";


        EditMessageText editedKeyboard = null;


        if (identifier.contains("monthlyCategory")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageInline(textFormat,
                            inlineKeyboard.listToTransactionInline(messagesService
                                    .getMonthlyCategoryRecord(categoryMapper(category))), update);
        }
        if (command.equals("monthlyCategory-All")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageInline("All Expenses:\n", inlineKeyboard.listToTransactionInline(messagesService.getExpensesByDates(getCurrentMonth() , getCurrentYear())), update);
        }

        if (command.equals("monthlySum-All")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageText("All Spending:\n" + messagesService.getTotalMoneySpentCurrentMonth(getCurrentMonth(),getCurrentYear()), update);

        } else if (identifier.equals("monthlySum")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageText(textFormat + messagesService.getCategoryMonthlySpent(categoryMapper(category)), update);
        }
        return editedKeyboard;
    }

    private static String categoryMapper(String arg) {
        Map<String,String> categoryMapper = Map.of(
        "general","כללי",
        "fuel","דלק",
        "house shopping","משותף",
        "shopping","קניות",
        "food","אוכל",
        "all spending","all");

        return categoryMapper.get(arg);
    }

    public static String approvedCompanyFormatter(List<String> companyList) {
        return String.valueOf(companyList).replace("[", "").replace("]", "");
    }

    public static void inputInsertionAndValidation(String command, SendMessage message, IDatabase database) throws SQLException {
        Balance balance = new Balance();
        int sizeBeforeDataInsertion = database.getAllRecordsFromDb().size();
        database.setDbParameter(command);
        int sizeAfterDataInsertion = database.getAllRecordsFromDb().size();
        boolean dbRecordUpdated = sizeAfterDataInsertion == sizeBeforeDataInsertion + 1;

        if (dbRecordUpdated) {
            balance.addToBalance(new PriceInputExtractor().getInput(command));
            message.setText("Data added to bot.");
        } else
            message.setText("A problem occurred in data insertion");
    }





    public static List<String> getApprovedChats(){
        String approvedChats = System.getenv("APPROVED_CHATS");
        List<String> chatsToReturn =
                Arrays.stream(approvedChats.split(",")).map(String::trim).collect(Collectors.toList());
        return chatsToReturn;
    }

    public static String getCurrentMonth() {
        String addZeroToMonthIfValueLessThan10 = "0"+LocalDate.now().getMonthValue();
        String currentMonth = LocalDate.now().getMonthValue() < 10 ?  addZeroToMonthIfValueLessThan10 : String.valueOf(LocalDate.now().getMonthValue());

        return currentMonth;
    }

    public static String getCurrentYear(){
        String currentYear = String.valueOf(LocalDate.now().getYear());
        return currentYear;
    }
}





