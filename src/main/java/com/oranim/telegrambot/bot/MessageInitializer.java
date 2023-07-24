package com.oranim.telegrambot.bot;

import com.oranim.telegrambot.keyboards.CustomKeyboard;
import com.oranim.telegrambot.keyboards.InlineKeyboard;
import com.oranim.telegrambot.keyboards.KeyboardBuilders;
import com.oranim.telegrambot.managers.BalanceManager;
import com.oranim.telegrambot.managers.SalaryManager;
import com.oranim.telegrambot.services.ExpensesService;
import com.oranim.telegrambot.utils.Const;
import com.oranim.telegrambot.utils.DateHandler;
import com.oranim.telegrambot.utils.UtilityMethods;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MessageInitializer {

    String chooseOptionPrompt = "Choose an option: ";

    public MessageInitializer() {
    }

    public SendMessage keyboardButtonsInit(SendMessage message, String command, InlineKeyboard inLine,
                                           List<String> companyList, Update update) throws SQLException {

        switch (command.toLowerCase().replace("/", "")) {

            case "start" -> {
                String startMessage = "Hi I am an expense management bot \nChoose your option from the keyboard below.";
                message = KeyboardBuilders.sendKeyboardToUser(startMessage, new CustomKeyboard().startKeyboard(), update);
            }
            case "expenses" -> message.setText("Enter expense in this order: \nProduct Price Company Note");

            case "refund" -> message.setText("Enter refund in this order: \nProduct Minus Sign(-) Price Company Note");

            case "balance" -> message.setText("Balance: \n%s".formatted(new BalanceManager().formatGetBalance()));

            case "monthly spent" -> message = KeyboardBuilders.sendKeyboardToUser(chooseOptionPrompt, inLine.monthlySumKeyboardMarkup(), update);

            case "monthly expenses" -> message = KeyboardBuilders.sendKeyboardToUser(chooseOptionPrompt, inLine.monthlyExpensesInlineButtonMarkup(), update);

            case "overall expenses" -> message = KeyboardBuilders.sendKeyboardToUser(chooseOptionPrompt, inLine.generateYearsKeyboardMarkup(), update);

            case "admincenter" -> message = KeyboardBuilders.sendKeyboardToUser("Admin keyboard", inLine.adminKeyboardMarkup(message), update);

            case "showcompany" -> message.setText(new UtilityMethods().approvedCompanyFormatter(companyList));

            default -> message.setText("We are very sorry, no match found.");
        }
        return message;
    }


    public void inlineWithCallbackData(SendMessage message, String command) throws SQLException{


        if (command.toLowerCase().contains("salary")) {
            new SalaryManager().generateSalaryFromInput(message,command);
        }

        if (command.contains("GetTransactionInPlace" + Const.DOUBLE_SEMICOLON_SEPARATOR)) {
            ExpensesService messagesService = new ExpensesService();
            int transactionNumber = Integer.parseInt(command.split(Const.DOUBLE_SEMICOLON_SEPARATOR)[1]);
            message.setText(messagesService.dbRecordsToMap(transactionNumber));
        }


    }


    public EditMessageText editedMessageReply(Update update, String command, ExpensesService messagesService) throws SQLException {
        EditMessageText editedMessage = null;


        if (command.contains("GET_YEAR"+ Const.DOUBLE_SEMICOLON_SEPARATOR)){
            int year = Integer.parseInt(command.split(Const.DOUBLE_SEMICOLON_SEPARATOR)[1]);
            editedMessage = KeyboardBuilders.createEditMessageInline("Expenses for year " + year,
                    new InlineKeyboard().generateMonthsInKeyboardMarkup(year) , update);
        }


        if (command.contains("monthlyCategory-") || command.contains("monthlySum-")) {
            editedMessage = monthlyCategoryEditedButtonsInit(command, update, messagesService);
        }


        if (command.contains("SendChatId.admin" + Const.DOUBLE_SEMICOLON_SEPARATOR)) {
            String chatID = command.split(Const.DOUBLE_SEMICOLON_SEPARATOR)[1];
            editedMessage = KeyboardBuilders.createEditMessageText(chatID, update);
        }

        if (command.equals("checkDBS.admin")) {
            String dbStatus = messagesService.dbStatusCheckInline();
            editedMessage = KeyboardBuilders.createEditMessageText(dbStatus, update);
        }


        if (command.contains("monthDbCheck" + Const.DOUBLE_SEMICOLON_SEPARATOR)) {
            InlineKeyboard inlineKeyboard = new InlineKeyboard();
            String month = command.split(Const.DOUBLE_SEMICOLON_SEPARATOR)[1];
            String year = command.split(Const.DOUBLE_SEMICOLON_SEPARATOR)[2];

            if (messagesService.getExpensesByDates(month,year).isEmpty()) {
                String messageText = "No Expenses in " + new UtilityMethods().formatNumberMonthsToNames(month) +Const.SINGLE_SPACE_SEPARATOR + year +".";
                editedMessage = KeyboardBuilders.createEditMessageText(messageText, update);
            }

            else {
                editedMessage = KeyboardBuilders.createEditMessageInline(
                        new UtilityMethods()
                        .formatNumberMonthsToNames(month) + " Expenses:\n",
                        inlineKeyboard
                        .listToTransactionInline
                                (
                                messagesService
                                .getExpensesByDates(month ,year)
                                ),
                        update
                );
            }
        }


        return editedMessage;
    }


    public EditMessageText monthlyCategoryEditedButtonsInit
            (String command, Update update, ExpensesService messagesService) throws SQLException {
        DateHandler dateHandler = new DateHandler();
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
                    KeyboardBuilders.createEditMessageInline
                            ("All Expenses:\n",
                                    inlineKeyboard.listToTransactionInline(messagesService.getExpensesByDates(dateHandler.getCurrentMonth(),
                                            dateHandler.getCurrentYear())),
                                    update);
        }

        if (command.equals("monthlySum-All")) {
            editedKeyboard =
                    KeyboardBuilders.createEditMessageText("All Spending:\n" + messagesService.sumMoneySpentInCertainMonth(dateHandler.getCurrentMonth(),dateHandler.getCurrentYear()), update);

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
}

