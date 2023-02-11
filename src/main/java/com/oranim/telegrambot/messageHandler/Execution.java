package com.oranim.telegrambot.messageHandler;

import com.oranim.telegrambot.db.DatabaseAction;
import com.oranim.telegrambot.db.IDatabase;
import com.oranim.telegrambot.db.MariaDB;
import com.oranim.telegrambot.keyboards.InlineKeyboard;
import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.FunctionsUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


public class Execution {
    private final List<String> keyboardButtonsCommendsList = FunctionsUtils.getKeyboardButtonsCommands();
    private final List<String> approvedCompaniesList = FunctionsUtils.getApprovedCompanies();
    private final List<String> editedMessageList = FunctionsUtils.getEditedMessage();


    InlineKeyboard inLine = new InlineKeyboard();
    messageDispatcher messageDispatcher = new messageDispatcher();
    IDatabase database = new MariaDB();
    DatabaseAction databaseAction = new DatabaseAction();

    public Execution() {
    }

    @SuppressWarnings("rawtypes")
    public BotApiMethod messageDispatcher(String command, SendMessage message, Update update) {
        try {
            //todo getting doc into bot
            boolean messageContainDoc = update.getMessage() != null && update.getMessage().getDocument() != null;
            if (messageContainDoc ){

            }

            if (keyboardButtonsCommendsList.contains(command.toLowerCase())) {
                return messageDispatcher.keyboardButtonsHandler(message, command, inLine, approvedCompaniesList, update);

            } else if (listContainArg(command)) {
                return messageDispatcher.editedMessageReply(update, message, command, databaseAction, database);
            }

            messageDispatcher.inLineCallBackHandler(message, command, approvedCompaniesList, database);


        } catch (SQLException sqlException) {
            BotLogging.setInfoLog(classLog("SQLexception catch", Arrays.toString(sqlException.getStackTrace()) ));
            message.setText("No Connection to DB");

        } catch (Exception exception) {
            BotLogging.setInfoLog(classLog("Exception catch", Arrays.toString(exception.getStackTrace()) ));

        }
        return message;
    }

    private boolean listContainArg(String arg) {
        for (var message : editedMessageList) {
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

