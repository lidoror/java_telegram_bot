package com.oranim.telegrambot.utils;

import com.oranim.telegrambot.Exception.IllegalSalaryException;
import com.oranim.telegrambot.Exception.NoConnectionToDbException;
import com.oranim.telegrambot.balanceMgmt.Balance;
import com.oranim.telegrambot.db.DatabaseAction;
import com.oranim.telegrambot.db.IDatabase;
import com.oranim.telegrambot.db.ShoppingMgmtRecord;
import com.oranim.telegrambot.keyboards.InlineKeyboard;
import com.oranim.telegrambot.keyboards.KeyboardBuilders;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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





    public static EditMessageText monthlyCategoryButtonsDispatcher(String command, Update update, DatabaseAction databaseAction) throws SQLException {
        InlineKeyboard inlineKeyboard = new InlineKeyboard();
        String category = command.split("-")[1];
        String identifier = command.split("-")[0];
        String textFormat = category + ":\n";


        EditMessageText editedKeyboard = null;


        if (identifier.contains("monthlyCategory")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageInline(textFormat,
                            inlineKeyboard.listToTransactionInline(databaseAction
                                    .getMonthlyCategoryRecord(categoryMapper(category))), update);
        }
        if (command.equals("monthlyCategory-All")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageInline("All Expenses:\n", inlineKeyboard.listToTransactionInline(databaseAction.getExpensesByDates(getCurrentMonth() , getCurrentYear())), update);
        }

        if (command.equals("monthlySum-All")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageText("All Spending:\n" + databaseAction.getTotalMoneySpentCurrentMonth(getCurrentMonth(),getCurrentYear()), update);

        } else if (identifier.equals("monthlySum")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageText(textFormat + databaseAction.getCategoryMonthlySpent(categoryMapper(category)), update);
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



    public static List<String> getKeyboardButtonsCommands() {
        return List.of("/start", "expenses", "refund", "balance", "monthly spent",
                "monthly expenses", "overall expenses", "/showcompany", "admincenter");
    }

    public static String formatNumberMonthsToNames(String month) {
        Map<String,String> monthMapper = new HashMap<>();
        monthMapper.put("1","January");
        monthMapper.put("2","February");
        monthMapper.put("3","March");
        monthMapper.put("4","April");
        monthMapper.put("5","May");
        monthMapper.put("6","June");
        monthMapper.put("7","July");
        monthMapper.put("8","August");
        monthMapper.put("9","September");
        monthMapper.put("10","October");
        monthMapper.put("11","November");
        monthMapper.put("12","December");

        return monthMapper.get(month);
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
                "monthDbCheck",
                "GET_YEAR");
    }

    public static void salaryInitializationFromInput(SendMessage message, String command)  {
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





