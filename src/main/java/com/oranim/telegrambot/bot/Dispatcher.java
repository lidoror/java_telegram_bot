package com.oranim.telegrambot.bot;

import com.oranim.telegrambot.inputExtractor.CompanyInputExtractor;
import com.oranim.telegrambot.keyboards.InlineKeyboard;
import com.oranim.telegrambot.services.ExpensesService;
import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.UtilityMethods;
import com.oranim.telegrambot.utils.LogWarningLevel;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


public class Dispatcher {
    private final List<String> approvedCompaniesList = new UtilityMethods().getApprovedCompanies();
    private final List<String> editedMessageList = new UtilityMethods().getEditedMessage();
    private final List<String> messagesWithCallback = new UtilityMethods().getMessagesWithCallbackList();
    private final InlineKeyboard inLine;
    private final MessageInitializer messageInitializer;
    private final ExpensesService expensesService;
    private final UtilityMethods util;

    public Dispatcher() {
        inLine = new InlineKeyboard();
        messageInitializer = new MessageInitializer();
        expensesService = new ExpensesService();
        util = new UtilityMethods();
    }

    @SuppressWarnings("rawtypes")
    public BotApiMethod messageDispatcher(String command, SendMessage message, Update update) {

        try {
            // todo getting doc into bot
            boolean messageContainDoc = update.getMessage() != null && update.getMessage().getDocument() != null;

            if (messageContainDoc ) {

            }


            if (listContainArg(command,messagesWithCallback)){
                messageInitializer.inlineWithCallbackData(message, command);
                return message;
            }
            else if (listContainArg(command,editedMessageList)) {
                return messageInitializer.editedMessageReply(update, command, expensesService);
            }

            boolean messageContainNumber = util.inputContainNumber(command) ;
            if (messageContainNumber) {
                boolean messageContainCompany = approvedCompaniesList.contains(new CompanyInputExtractor().getInput(command));
                if (messageContainCompany) {
                    expensesService.inputInsertionAndValidation(command, message);
                    return message;
                }
            }

            return messageInitializer.keyboardButtonsInit(message, command, inLine, approvedCompaniesList, update);

        } catch (SQLException sqlException) {
            BotLogging.createLog(LogWarningLevel.WARN, Dispatcher.class.getName(),"messageDispatcher",Arrays.toString(sqlException.getStackTrace()));
            message.setText("No Connection to DB");

        } catch (Exception exception) {
            BotLogging.createLog(LogWarningLevel.WARN, Dispatcher.class.getName(),"messageDispatcher",Arrays.toString(exception.getStackTrace()));

        }

        return message;
    }

    private boolean listContainArg(String arg , List<String> lst) {
        return lst.stream().anyMatch(arg::contains);
//        for (var message : lst) {
//            if (arg.contains(message)) {
//                return true;
//            }
//        }
//        return false;
    }



}

