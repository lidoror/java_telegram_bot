package com.Oranim.TelegramBot.messageHandler;

import com.Oranim.TelegramBot.db.DatabaseListAction;
import com.Oranim.TelegramBot.db.IDatabase;
import com.Oranim.TelegramBot.db.MariaDB;
import com.Oranim.TelegramBot.keyboards.InlineKeyboard;
import com.Oranim.TelegramBot.utils.FunctionsUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.sql.SQLException;
import java.util.List;


public class Execution {
    private final List<String> keyboardButtonsCommendsList = FunctionsUtils.getKeyboardButtonsCommands();
    private final List<String> approvedCompaniesList = FunctionsUtils.getApprovedCompanies();
    private final List<String> editedMessageList = FunctionsUtils.getEditedMessage();


    InlineKeyboard inLine = new InlineKeyboard();
    messageDispatcher messageDispatcher = new messageDispatcher();
    IDatabase database = new MariaDB();
    DatabaseListAction databaseListAction = new DatabaseListAction();

    public Execution() {
    }

    @SuppressWarnings("rawtypes")
    public BotApiMethod messageDispatcher(String command, SendMessage message, Update update) {
        try {
            //todo need to finish implementation on branch load excel excel changed to csv
            if (update.getMessage().getDocument() != null){
                System.out.println("doc");
                GetFile fileRequest = new GetFile();
            }

            if (keyboardButtonsCommendsList.contains(command.toLowerCase())) {
                return messageDispatcher.keyboardButtonsHandler(message, command, inLine, approvedCompaniesList, update);

            } else if (listContainArg(command)) {
                return messageDispatcher.editedMessageReply(update, message, command, databaseListAction, database);
            }
            messageDispatcher.inLineCallBackHandler(message, command, databaseListAction, approvedCompaniesList, database);


        } catch (SQLException sqlException) {
            System.err.println("No Connection to DB\n");
            sqlException.printStackTrace();
            message.setText("No Connection to DB");

        } catch (Exception e) {
            message.setText("Some Problem occurred");
            System.err.println("exception\n");
            e.printStackTrace();

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

}

