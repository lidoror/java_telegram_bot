package com.Oranim.TelegramBot.utils;

import com.Oranim.TelegramBot.Exception.IllegalSalaryException;
import com.Oranim.TelegramBot.Exception.NoConnectionToDbException;
import com.Oranim.TelegramBot.balanceMgmt.Balance;
import com.Oranim.TelegramBot.db.DatabaseListAction;
import com.Oranim.TelegramBot.db.IDatabase;
import com.Oranim.TelegramBot.db.ShoppingMgmtRecord;
import com.Oranim.TelegramBot.keyboards.InlineKeyboard;
import com.Oranim.TelegramBot.keyboards.KeyboardBuilders;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class FunctionsUtils {


    @NotNull
    public static String generateProductCostFromInput(String price) {
        return price.split(" ")[1];
    }

    @NotNull
    public static String generateProductFromInput(String product) {
        return product.split(" ")[0];
    }

    @NotNull
    public static String generateProductCompanyFromInput(String company) {
        return company.split(" ")[2];
    }

    public static boolean stringContainNumber(String str) {
        return NumberUtils.isNumber(str);
    }


    @NotNull
    public static StringBuilder generateProductNoteFromInput(String note) {
        String[] messageSplitter = note.split(" ");
        StringBuilder noteBuilder = new StringBuilder();

        boolean noNoteInserted = messageSplitter.length == 3;
        if (noNoteInserted) {
            noteBuilder.append("No note added");
            return noteBuilder;
        }

        int i;
        for (i = 3; i <= messageSplitter.length - 2; i++) {
            noteBuilder.append(messageSplitter[i]);
            noteBuilder.append(" ");
        }
        noteBuilder.append(messageSplitter[i]);

        return noteBuilder;
    }

    public static String dbStatusCheckInline(IDatabase database) throws SQLException {
        if (database.checkConnection()) {
            return "Connection to Database achieved";
        } else
            throw new NoConnectionToDbException("Cannot connect to Database");
    }


    public static boolean checkIfGivenMonthEqualToCurrentMonth(String givenDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate monthToCheck = LocalDate.parse(givenDate);
        return currentDate.getMonth().equals(monthToCheck.getMonth());
    }


    public static void monthlyCategoryButtonsDispatcher1(String command, SendMessage message, DatabaseListAction databaseListAction) throws SQLException {
        String category = command.split("-")[1];
        String identifier = command.split("-")[0];
        String textFormat = category + ":\n";

        if (command.equals("monthlyCategory-All")) {
            message.setText("All Expenses: \n" + databaseListAction.getMonthlyExpenses());
        } else if (identifier.contains("monthlyCategory")) {
            message.setText(textFormat + databaseListAction.getMonthlyCategoryRecord(categoryChanger(category)));
        }
        if (command.equals("monthlySum-All")) {
            message.setText("All Spending:\n" + databaseListAction.getTotalMonthSpending());
        } else if (identifier.equals("monthlySum")) {
            message.setText(textFormat + databaseListAction.getCategoryMonthlySpent(categoryChanger(category)));
        }
    }

    public static EditMessageText monthlyCategoryButtonsDispatcher(String command, Update update, DatabaseListAction databaseListAction) throws SQLException {
        InlineKeyboard inlineKeyboard = new InlineKeyboard();
        String category = command.split("-")[1];
        String identifier = command.split("-")[0];
        String textFormat = category + ":\n";
        EditMessageText editedKeyboard = null;


        if (identifier.contains("monthlyCategory")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageInline(textFormat,
                            inlineKeyboard.listToTransactionInline(databaseListAction
                                    .getMonthlyCategoryRecord(categoryChanger(category))), update);
        }
        if (command.equals("monthlyCategory-All")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageInline("All Expenses:\n", inlineKeyboard.listToTransactionInline(databaseListAction.getMonthlyExpensesAsList()), update);
        }

        if (command.equals("monthlySum-All")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageText("All Spending:\n" + databaseListAction.getTotalMonthSpending(), update);
        } else if (identifier.equals("monthlySum")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageText(textFormat + databaseListAction.getCategoryMonthlySpent(categoryChanger(category)), update);
        }
        return editedKeyboard;
    }

    private static String categoryChanger(String arg) {
        String param = "";
        switch (arg.toLowerCase()) {
            case "general" -> param = "כללי";
            case "fuel" -> param = "דלק";
            case "house shopping" -> param = "משותף";
            case "shopping" -> param = "קניות";
            case "food" -> param = "אוכל";
            case "all spending" -> param = "all";
        }
        return param;
    }

    public static String approvedCompanyFormatter(List<String> companyList) {
        return String.valueOf(companyList).replace("[", "").replace("]", "");
    }

    public static void inputInsertionAndValidation(String command, SendMessage message, IDatabase database) throws SQLException {
        Balance balance = new Balance();
        int sizeBeforeDataInsertion = database.dbRecordToList().size();
        database.setDbParameter(command);
        int sizeAfterDataInsertion = database.dbRecordToList().size();
        boolean dbRecordUpdated = sizeAfterDataInsertion == sizeBeforeDataInsertion + 1;

        if (dbRecordUpdated) {
            balance.addToBalance(generateProductCostFromInput(command));
            message.setText("Data added to bot.");
        } else
            message.setText("A problem occurred in data insertion");
    }

    public static String listOutputFormatter(List<ShoppingMgmtRecord> listToFormat) {
        return String.valueOf(listToFormat).replace("[", "").replace(", ", "").replace("]", "");
    }


    public static List<String> getKeyboardButtonsCommands() {
        return List.of("/start", "expenses", "refund", "balance", "monthly spent",
                "monthly expenses", "overall expenses", "/showcompany", "admincenter");
    }

    public static String formatNumberMonthsToNames(String month) {
        String monthToReturn = month;
        switch (monthToReturn) {
            case "1" -> monthToReturn = "January ";
            case "2" -> monthToReturn = "February";
            case "3" -> monthToReturn = "March";
            case "4" -> monthToReturn = "April ";
            case "5" -> monthToReturn = "May";
            case "6" -> monthToReturn = "June";
            case "7" -> monthToReturn = "July";
            case "8" -> monthToReturn = "August";
            case "9" -> monthToReturn = "September";
            case "10" -> monthToReturn = "October";
            case "11" -> monthToReturn = "November";
            case "12" -> monthToReturn = "December";

        }
        return monthToReturn;
    }


    public static List<String> getApprovedCompanies() {
        return List.of("דלק", "כללי", "משותף", "קניות", "אוכל");
    }

    public static List<String> getEditedMessage() {
        return List.of("Back",
                "monthlySum-Fuel",
                "monthlyCategory-All",
                "monthlySum-House Shopping",
                "monthlySum-Shopping",
                "monthlySum-Food",
                "monthlySum-General",
                "monthlySum-All",
                "monthlyCategory-Fuel",
                "monthlyCategory-House Shopping",
                "monthlyCategory-Shopping",
                "monthlyCategory-Food",
                "monthlyCategory-General",
                "checkDBS.admin",
                "SendChatId.admin",
                "monthDbCheck");
    }

    public static void salaryInitializationFromInput(SendMessage message, String command) throws IllegalSalaryException {
        JsonWorkloads jsonWorkloads = new JsonWorkloads();
        if (command.contains("one")) {
            String firstSalary = command.split(" ")[2];
            jsonWorkloads.jsonWriter("First_Salary" , firstSalary , "./vars.json");
            message.setText("First salary initialized");

        } else if (command.contains("two")) {
            String secondSalary = command.split(" ")[2];
            jsonWorkloads.jsonWriter("Second_Salary" , secondSalary , "./vars.json");
            message.setText("Second salary initialized");

        } else
            message.setText("Salary initialization failed");
    }
}





