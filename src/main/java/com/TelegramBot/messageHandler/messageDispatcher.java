package com.TelegramBot.messageHandler;

import com.TelegramBot.Exception.IllegalSalaryException;
import com.TelegramBot.balanceMgmt.Balance;
import com.TelegramBot.db.DatabaseListAction;
import com.TelegramBot.db.DatabaseMapAction;
import com.TelegramBot.db.IDatabase;
import com.TelegramBot.keyboards.CustomKeyboard;
import com.TelegramBot.keyboards.InlineKeyboard;
import com.TelegramBot.keyboards.KeyboardBuilders;
import com.TelegramBot.utils.Const;
import com.TelegramBot.utils.FunctionsUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class messageDispatcher {
    public static List<String> args = new ArrayList<>();
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


            case "monthly spent" -> {
                message = KeyboardBuilders.sendKeyboardToUser(chooseOptionPrompt, inLine.monthlySumKeyboardMarkup(), update);
            }

            case "monthly expenses" -> {
                message = KeyboardBuilders.sendKeyboardToUser(chooseOptionPrompt, inLine.monthlyExpensesInlineButtonMarkup(), update);
            }

            case "overall expenses" -> {
                message = KeyboardBuilders.sendKeyboardToUser(chooseOptionPrompt, inLine.showMonthsIn2022KeyboardMarkup(), update);
            }

            case "admincenter" -> {
                message = KeyboardBuilders.sendKeyboardToUser("Admin keyboard", inLine.adminKeyboardMarkup(message), update);
            }

            case "showcompany" -> message.setText(FunctionsUtils.approvedCompanyFormatter(companyList));

            default -> message.setText("We are very sorry, no match found.");
        }
        return message;
    }


    public void inLineCallBackHandler(SendMessage message, String command, DatabaseListAction databaseListAction,
                                      List<String> approvedCompanies, IDatabase database) throws SQLException, IllegalSalaryException {

        if (command.contains("salary")) {
            FunctionsUtils.salaryInitializationFromInput(message, command);
        }

        if (command.contains("GetTransactionInPlace" + Const.INLINE_SEPARATOR)) {
            DatabaseMapAction databaseMapAction = new DatabaseMapAction();
            int transactionNumber = Integer.parseInt(command.split(Const.INLINE_SEPARATOR)[1]);
            message.setText(databaseMapAction.dbRecordsToMap(transactionNumber));
        }


        if (command.contains(" ") && approvedCompanies.contains(FunctionsUtils.generateProductCompanyFromInput(command))) {
            if (FunctionsUtils.stringContainNumber(FunctionsUtils.generateProductCostFromInput(command))) {
                FunctionsUtils.inputInsertionAndValidation(command, message, database);
            }
        }

    }


    public EditMessageText editedMessageReply(Update update, SendMessage message, String command, DatabaseListAction databaseListAction, IDatabase database) throws SQLException {
        EditMessageText messageText = null;

        if (command.contains("monthlyCategory-") || command.contains("monthlySum-")) {
            messageText = FunctionsUtils.monthlyCategoryButtonsDispatcher(command, update, databaseListAction);
        }

        if (command.equals("Back-One-Arg")) {

        }

        if (command.contains("SendChatId.admin" + Const.INLINE_SEPARATOR)) {
            String chatID = command.split(Const.INLINE_SEPARATOR)[1];
            messageText = KeyboardBuilders.createEditMessageText(chatID, update);
        }

        if (command.equals("checkDBS.admin")) {
            String dbStatus = FunctionsUtils.dbStatusCheckInline(database);
            messageText = KeyboardBuilders.createEditMessageText(dbStatus, update);
        }


        if (command.contains("monthDbCheck" + Const.INLINE_SEPARATOR)) {
            InlineKeyboard inlineKeyboard = new InlineKeyboard();
            String month = command.split(Const.INLINE_SEPARATOR)[1];
            if (databaseListAction.getMonthByExpense(month).isEmpty()) {
                messageText = KeyboardBuilders.createEditMessageText("No Expenses in " + FunctionsUtils.formatNumberMonthsToNames(month) + ".", update);
            }else {
                messageText = KeyboardBuilders.createEditMessageInline( FunctionsUtils.formatNumberMonthsToNames(month) + " Expenses:\n",
                        inlineKeyboard.listToTransactionInline(databaseListAction.getMonthByExpense(month)), update);
            }
        }


        return messageText;
    }
}

