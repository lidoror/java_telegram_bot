package com.oranim.telegrambot.messageHandler;

import com.oranim.telegrambot.Exception.UnableToGeneratePriceException;
import com.oranim.telegrambot.balanceMgmt.Balance;
import com.oranim.telegrambot.balanceMgmt.SalaryActions;
import com.oranim.telegrambot.db.IDatabase;
import com.oranim.telegrambot.db.MessagesService;
import com.oranim.telegrambot.keyboards.CustomKeyboard;
import com.oranim.telegrambot.keyboards.InlineKeyboard;
import com.oranim.telegrambot.keyboards.KeyboardBuilders;
import com.oranim.telegrambot.utils.Const;
import com.oranim.telegrambot.utils.FunctionsUtils;
import com.oranim.telegrambot.utils.ListAndMappersUtils;
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


    public void inlineWithCallbackData(SendMessage message, String command) throws SQLException, UnableToGeneratePriceException {


        if (command.toLowerCase().contains("salary")) {
            new SalaryActions().salaryInitializationFromInput(message,command);
        }

        if (command.contains("GetTransactionInPlace" + Const.DOUBLE_SEMICOLON_SEPARATOR)) {
            MessagesService messagesService = new MessagesService();
            int transactionNumber = Integer.parseInt(command.split(Const.DOUBLE_SEMICOLON_SEPARATOR)[1]);
            message.setText(messagesService.dbRecordsToMap(transactionNumber));
        }


    }


    public EditMessageText editedMessageReply(Update update, SendMessage message, String command, MessagesService messagesService, IDatabase database) throws SQLException {
        EditMessageText editedMessage = null;


        if (command.contains("GET_YEAR"+ Const.DOUBLE_SEMICOLON_SEPARATOR)){
            int year = Integer.parseInt(command.split(Const.DOUBLE_SEMICOLON_SEPARATOR)[1]);
            editedMessage = KeyboardBuilders.createEditMessageInline("Expenses for year " + year,
                    new InlineKeyboard().generateMonthsInKeyboardMarkup(year) , update);
        }


        if (command.contains("monthlyCategory-") || command.contains("monthlySum-")) {
            editedMessage = FunctionsUtils.monthlyCategoryButtonsDispatcher(command, update, messagesService);
        }


        if (command.contains("SendChatId.admin" + Const.DOUBLE_SEMICOLON_SEPARATOR)) {
            String chatID = command.split(Const.DOUBLE_SEMICOLON_SEPARATOR)[1];
            editedMessage = KeyboardBuilders.createEditMessageText(chatID, update);
        }

        if (command.equals("checkDBS.admin")) {
            String dbStatus = new MessagesService().dbStatusCheckInline(database);
            editedMessage = KeyboardBuilders.createEditMessageText(dbStatus, update);
        }


        if (command.contains("monthDbCheck" + Const.DOUBLE_SEMICOLON_SEPARATOR)) {
            InlineKeyboard inlineKeyboard = new InlineKeyboard();
            String month = command.split(Const.DOUBLE_SEMICOLON_SEPARATOR)[1];
            String year = command.split(Const.DOUBLE_SEMICOLON_SEPARATOR)[2];

            if (messagesService.getExpensesByDates(month,year).isEmpty()) {
                String messageText = "No Expenses in " + new ListAndMappersUtils().formatNumberMonthsToNames(month) +Const.SINGLE_SPACE_SEPARATOR + year +".";
                editedMessage = KeyboardBuilders.createEditMessageText(messageText, update);
            }else {
                editedMessage = KeyboardBuilders.createEditMessageInline( new ListAndMappersUtils().formatNumberMonthsToNames(month) + " Expenses:\n",
                        inlineKeyboard.listToTransactionInline(messagesService.getExpensesByDates(month ,year)), update);
            }
        }


        return editedMessage;
    }
}

