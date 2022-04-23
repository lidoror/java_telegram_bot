package com.TelegramBot.utils;

import com.TelegramBot.balanceMgmt.Balance;
import com.TelegramBot.db.MariaDB;
import com.jtelegram.api.inline.keyboard.InlineKeyboardMarkup;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Functions {
    MariaDB db = new MariaDB();


    public Functions() {
    }

    @NotNull
    public String getPriceFromInput(String price) {
        return price.split(" ")[1];
    }

    @NotNull
    public String getProductFromInput(String product) {
        return product.split(" ")[0];
    }

    @NotNull
    public String getCompanyFromInput(String company) {
        return company.split(" ")[2];
    }

    public boolean isNumeric(String str) {
        return NumberUtils.isNumber(str);
    }


    @NotNull
    public StringBuilder getNoteFromInput(String note) {
        String[] messageSplitter = note.split(" ");
        StringBuilder noteBuilder = new StringBuilder();
        int i;
        if (messageSplitter.length == 3) {
            noteBuilder.append("No note added");
            return noteBuilder;
        }
        for (i = 3; i <= messageSplitter.length - 2; i++) {
            noteBuilder.append(messageSplitter[i]);
            noteBuilder.append(" ");
        }
        noteBuilder.append(messageSplitter[i]);

        return noteBuilder;
    }

    public String dbStatusCheckInline() throws SQLException {
        MariaDB db = new MariaDB();
        if (db.checkConnection()) {
            return "Active";
        } else
            throw new SQLException("Inactive");
    }

    public void setDbParameter(String command) {
        db.updateDB(getProductFromInput(command),
                getPriceFromInput(command), getCompanyFromInput(command),
                String.valueOf(getNoteFromInput(command)));
    }

    public boolean checkForCurrentMonth(String givenDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate monthToCheck = LocalDate.parse(givenDate);
        return currentDate.getMonth().equals(monthToCheck.getMonth());
    }

    public String getRefundMessage() {
        return "Enter refund in this order: \nProduct Minus Sign(-) Price Company Note";
    }

    public String getExpenseMessage() {
        return "Enter expense in this order: \nProduct Price Company Note";
    }

    public String getStartMessage() {
        return "Hi I am an expense management bot \nChoose your option from the keyboard below.";
    }

    public String chooseOptionPrompt() {
        return "Choose an option: ";
    }

    public void monthlyCategory(String command, SendMessage message) throws SQLException {
        String category = command.split("-")[1];
        String identifier = command.split("-")[0];
        String textFormat = category + ":\n";
        if (command.equals("monthlyCategory-All")) {
            message.setText("All Expenses: \n" + db.getMonthlyExpenses());
        } else if (identifier.contains("monthlyCategory")) {
            message.setText(textFormat + db.getMonthlyCategoryRecord(categoryChanger(category)));

        }
        if (command.equals("monthlySum-All")) {
            message.setText("All Spending:\n" + db.getTotalMonthSpending());
        } else if (identifier.equals("monthlySum")) {
            message.setText(textFormat + db.getCategoryMonthlySpent(categoryChanger(category)));
        }
    }

    private String categoryChanger(String paramToChange) {
        String param = "";
        switch (paramToChange.toLowerCase()) {
            case "general" -> param = "כללי";
            case "fuel" -> param = "דלק";
            case "house shopping" -> param = "משותף";
            case "shopping" -> param = "קניות";
            case "food" -> param = "אוכל";
            case "all spending" -> param = "all";
        }
        return param;
    }

    public String getCompanyFormatter(List<String> companyList) {
        return String.valueOf(companyList).replace("[", "").replace("]", "");
    }

    public void dbInsertionAndValidation(String command, SendMessage message) throws SQLException {
        Balance balance = new Balance();
        int sizeBeforeDataInsertion = db.getDataListSize();
        setDbParameter(command);
        int sizeAfterDataInsertion = db.getDataListSize();
        if (sizeAfterDataInsertion == sizeBeforeDataInsertion + 1) {
            balance.addToBalance(getPriceFromInput(command));
            message.setText("Data added to bot.");
        } else
            message.setText("A problem occurred in data insertion");
    }

    public void inlineEditorDispatcher(String text, InlineKeyboardMarkup inlineKeyboardMarkup , Update update){
        Integer messageID = update.getCallbackQuery().getMessage().getMessageId();
        SendMessage message = new SendMessage();
        if(messageID.equals("2413544400615266673")){
            message.setText("work");
        }else if (messageID.equals(3453453)){

        }

    }




}
