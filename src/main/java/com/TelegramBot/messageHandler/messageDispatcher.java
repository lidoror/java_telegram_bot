package com.TelegramBot.messageHandler;

import com.TelegramBot.Exception.IllegalSalaryException;
import com.TelegramBot.balanceMgmt.Balance;
import com.TelegramBot.db.DatabaseFilter;
import com.TelegramBot.db.IDatabase;
import com.TelegramBot.keyboards.CustomKeyboard;
import com.TelegramBot.keyboards.InlineKeyboard;
import com.TelegramBot.utils.Const;
import com.TelegramBot.utils.FunctionsUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.util.List;

public class messageDispatcher {
    String chooseOptionPrompt = "Choose an option: ";

    public messageDispatcher() {
    }

    public SendMessage keyboardButtonsHandler(SendMessage message, String command, InlineKeyboard inLine,
                                              List<String> companyList) throws SQLException {

        switch (command.toLowerCase().replace("/", "")) {
            case "start" -> {
                message.setText("Hi I am an expense management bot \nChoose your option from the keyboard below.");
                CustomKeyboard customKeyboard = new CustomKeyboard();
                customKeyboard.startKeyboard(message);
            }
            case "expenses" -> 
                    message.setText("Enter expense in this order: \nProduct Price Company Note");

            case "refund" -> 
                    message.setText("Enter refund in this order: \nProduct Minus Sign(-) Price Company Note");

            case "balance" -> {
                Balance balance = new Balance();
                message.setText("Balance: \n" + balance.getStringBalance());
            }

            case "monthly spent" -> {
                message.setText(chooseOptionPrompt);
                inLine.monthlySum(message);
            }
            case "monthly expenses" -> {
                message.setText(chooseOptionPrompt);
                inLine.monthlyExpensesInlineButton(message);
            }
            case "overall expenses" -> {
                message.setText(chooseOptionPrompt);
                inLine.showMonthsIn2022(message);
            }
            case "admincenter" -> {
                message.setText(chooseOptionPrompt);
                inLine.adminKeyboard(message);
            }

            case "showcompany" -> message.setText(FunctionsUtils.approvedCompanyFormatter(companyList));

            default -> message.setText("We are very sorry, no match found.");
        }
        return message;
    }


    public BotApiMethod inLineCallBackHandler(SendMessage message, String command, DatabaseFilter databaseFilter,
                                              List<String> approvedCompanies , IDatabase database) throws SQLException, IllegalSalaryException {

        if (command.contains("salary")){
            FunctionsUtils.salaryInitializationFromInput(message, command);
            return message;
        }

        if (command.contains("monthlyCategory-") || command.contains("monthlySum-")) {
            FunctionsUtils.monthlyCategoryButtonsDispatcher(command, message, databaseFilter);
            return message;
        }

        if (command.contains("GetTransactionInPlace" + Const.INLINE_SEPARATOR)) {
            Integer transactionNumber = Integer.valueOf(command.split(Const.INLINE_SEPARATOR)[1]);
            message.setText(database.DbRecordsToMap(transactionNumber));
            return message;
        }

        if (command.contains("monthDbCheck" + Const.INLINE_SEPARATOR)) {
            String month = command.split(Const.INLINE_SEPARATOR)[1];
            if (databaseFilter.getMonthByExpense(month).isEmpty()) {
                message.setText("No Expenses in month " + month + ".");
            } else {
                message.setText("Month " + month + " Expenses:\n" + databaseFilter.getMonthByExpense(month));
            }
        }

        if (command.contains("SendChatId.admin" + Const.INLINE_SEPARATOR)) {
            message.setText(command.split(Const.INLINE_SEPARATOR)[1]);
            return message;
        }

        if (command.equals("checkDBS.admin")) {
            message.setText(FunctionsUtils.dbStatusCheckInline(database));
            return message;
        }

        if (command.contains(" ") && approvedCompanies.contains(FunctionsUtils.generateProductCompanyFromInput(command))) {
            if (FunctionsUtils.stringContainNumber(FunctionsUtils.generateProductCostFromInput(command))) {
                FunctionsUtils.inputInsertionAndValidation(command, message, database);
            }
        }

        return message;
    }


    public BotApiMethod editedMessageReply(Update update, SendMessage message, String command) {
        return null;
    }
}

