package com.oranim.telegrambot.messageHandler;

import com.oranim.telegrambot.InputExtractor.CompanyInputExtractor;
import com.oranim.telegrambot.InputExtractor.PriceInputExtractor;
import com.oranim.telegrambot.db.MessagesService;
import com.oranim.telegrambot.db.IDatabase;
import com.oranim.telegrambot.db.MessagesDAO;
import com.oranim.telegrambot.keyboards.InlineKeyboard;
import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.FunctionsUtils;
import com.oranim.telegrambot.utils.ListAndMappersUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


public class Execution {
    private final List<String> approvedCompaniesList = new ListAndMappersUtils().getApprovedCompanies();
    private final List<String> editedMessageList = new ListAndMappersUtils().getEditedMessage();
    private final List<String> messagesWithCallback = new ListAndMappersUtils().getMessagesWithCallbackList();

    InlineKeyboard inLine = new InlineKeyboard();
    messageDispatcher messageDispatcher = new messageDispatcher();
    IDatabase database = new MessagesDAO();
    MessagesService messagesService = new MessagesService();

    public Execution() {
    }

    @SuppressWarnings("rawtypes")
    public BotApiMethod messageDispatcher(String command, SendMessage message, Update update) {
        try {
            // todo getting doc into bot
            boolean messageContainDoc = update.getMessage() != null && update.getMessage().getDocument() != null;
            if (messageContainDoc ){

            }

            boolean messageToDB = FunctionsUtils.stringContainNumber(command) && approvedCompaniesList.contains(new CompanyInputExtractor().getInput(command));
            if (messageToDB) {
                if (FunctionsUtils.stringContainNumber(new PriceInputExtractor().getInput(command))) {
                    FunctionsUtils.inputInsertionAndValidation(command, message, database);
                }
            }

            if (listContainArg(command,messagesWithCallback)){
                messageDispatcher.inlineWithCallbackData(message, command);
            }
            else if (listContainArg(command,editedMessageList)) {
                return messageDispatcher.editedMessageReply(update, message, command, messagesService, database);
            }
            else  {
                return messageDispatcher.keyboardButtonsHandler(message, command, inLine, approvedCompaniesList, update);
            }



        } catch (SQLException sqlException) {
            BotLogging.setInfoLog(classLog("SQLexception catch", Arrays.toString(sqlException.getStackTrace()) ));
            message.setText("No Connection to DB");

        } catch (Exception exception) {
            BotLogging.setInfoLog(classLog("Exception catch", Arrays.toString(exception.getStackTrace()) ));

        }

        return message;
    }

    private boolean listContainArg(String arg , List<String> lst) {
        for (var message : lst) {
            if (arg.contains(message)) {
                return true;
            }
        }
        return false;
    }

    private String classLog(String method, String description) {
        return "Exception occur in class: %s , method: %s Description: %s".formatted(Execution.class.getName(), method, description);
    }

}

