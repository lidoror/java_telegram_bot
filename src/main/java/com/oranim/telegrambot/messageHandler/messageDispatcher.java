package com.oranim.telegrambot.messageHandler;

import com.oranim.telegrambot.Exception.IllegalSalaryException;
import com.oranim.telegrambot.db.DatabaseAction;
import com.oranim.telegrambot.db.IDatabase;
import com.oranim.telegrambot.keyboards.InlineKeyboard;
import com.oranim.telegrambot.keyboards.KeyboardBuilders;
import com.oranim.telegrambot.utils.Const;
import com.oranim.telegrambot.utils.FunctionsUtils;
import com.oranim.telegrambot.balanceMgmt.Balance;
import com.oranim.telegrambot.keyboards.CustomKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.util.List;

public class messageDispatcher {

    String chooseOptionPrompt = "Choose an option: ";

    public messageDispatcher() {
    }

    public SendMessage keyboardButtonsHandler(SendMessage message, String command, InlineKeyboard inLine,
                                              List<String> companyList, Update update) throws SQLException {

        switch (command.toLowerCase().replace("/", "")) {

            case "start" -> {
                String startMessage = "Hi I am an expense management bot \nChoose your option from the keyboard below.";
                message = KeyboardBuilders.sendKeyboardToUser(startMessage, new CustomKeyboard().startKeyboard(), update);
            }
            case "expenses" -> message.setText("Enter expense in this order: \nProduct Price Company Note");

            case "refund" -> message.setText("Enter refund in this order: \nProduct Minus Sign(-) Price Company Note");

            case "balance" -> message.setText("Balance: \n" + new Balance().getStringBalance());

            case "monthly spent" -> message = KeyboardBuilders.sendKeyboardToUser(chooseOptionPrompt, inLine.monthlySumKeyboardMarkup(), update);

            case "monthly expenses" -> message = KeyboardBuilders.sendKeyboardToUser(chooseOptionPrompt, inLine.monthlyExpensesInlineButtonMarkup(), update);

            case "overall expenses" -> message = KeyboardBuilders.sendKeyboardToUser(chooseOptionPrompt, inLine.generateYearsKeyboardMarkup(), update);

            case "admincenter" -> message = KeyboardBuilders.sendKeyboardToUser("Admin keyboard", inLine.adminKeyboardMarkup(message), update);

            case "showcompany" -> message.setText(FunctionsUtils.approvedCompanyFormatter(companyList));

            default -> message.setText("We are very sorry, no match found.");
        }
        return message;
    }


    public void inLineCallBackHandler(SendMessage message, String command,
                                      List<String> approvedCompanies, IDatabase database) throws SQLException, IllegalSalaryException {


            if (command.toLowerCase().contains("salary")) {
            FunctionsUtils.salaryInitializationFromInput(message,command);
        }

        if (command.contains("GetTransactionInPlace" + Const.SEPARATOR)) {
            DatabaseAction databaseAction = new DatabaseAction();
            int transactionNumber = Integer.parseInt(command.split(Const.SEPARATOR)[1]);
            message.setText(databaseAction.dbRecordsToMap(transactionNumber));
        }


        if (command.contains(" ") && approvedCompanies.contains(FunctionsUtils.generateProductCompanyFromInput(command))) {
            if (FunctionsUtils.stringContainNumber(FunctionsUtils.generateProductCostFromInput(command))) {
                FunctionsUtils.inputInsertionAndValidation(command, message, database);
            }
        }

    }


    public EditMessageText editedMessageReply(Update update, SendMessage message, String command, DatabaseAction databaseAction, IDatabase database) throws SQLException {
        EditMessageText editedMessage = null;

        //TODO complete this
        if (command.contains("GET_YEAR"+ Const.SEPARATOR)){
            int year = Integer.parseInt(command.split(Const.SEPARATOR)[1]);
            editedMessage = KeyboardBuilders.createEditMessageInline("Expenses for year " + year,
                    new InlineKeyboard().generateMonthsInKeyboardMarkup(year) , update);
        }


        if (command.contains("monthlyCategory-") || command.contains("monthlySum-")) {
            editedMessage = FunctionsUtils.monthlyCategoryButtonsDispatcher(command, update, databaseAction);
        }


        if (command.contains("SendChatId.admin" + Const.SEPARATOR)) {
            String chatID = command.split(Const.SEPARATOR)[1];
            editedMessage = KeyboardBuilders.createEditMessageText(chatID, update);
        }

        if (command.equals("checkDBS.admin")) {
            String dbStatus = FunctionsUtils.dbStatusCheckInline(database);
            editedMessage = KeyboardBuilders.createEditMessageText(dbStatus, update);
        }


        if (command.contains("monthDbCheck" + Const.SEPARATOR)) {
            InlineKeyboard inlineKeyboard = new InlineKeyboard();
            String month = command.split(Const.SEPARATOR)[1];
            String year = command.split(Const.SEPARATOR)[2];

            if (databaseAction.getExpensesByDates(month,year).isEmpty()) {
                String messageText = "No Expenses in " + FunctionsUtils.formatNumberMonthsToNames(month) +" " + year +".";
                editedMessage = KeyboardBuilders.createEditMessageText(messageText, update);
            }else {
                editedMessage = KeyboardBuilders.createEditMessageInline( FunctionsUtils.formatNumberMonthsToNames(month) + " Expenses:\n",
                        inlineKeyboard.listToTransactionInline(databaseAction.getExpensesByDates(month ,year)), update);
            }
        }


        return editedMessage;
    }
}

