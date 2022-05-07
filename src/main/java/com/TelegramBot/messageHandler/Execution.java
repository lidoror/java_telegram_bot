package com.TelegramBot.messageHandler;

import com.TelegramBot.db.DatabaseFilter;
import com.TelegramBot.db.IDatabase;
import com.TelegramBot.db.MariaDB;
import com.TelegramBot.keyboards.InlineKeyboard;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.sql.SQLException;
import java.util.List;


public class Execution {
    private final List<String> keyboardButtonsCommends = List.of("start", "expenses", "refund", "balance", "monthly spent",
            "monthly expenses", "overall expenses", "showcompany", "admincentercontrol");
    private final List<String> approvedCompanies = List.of("דלק", "כללי", "משותף", "קניות", "אוכל");


    InlineKeyboard inLine = new InlineKeyboard();
    messageDispatcher messageDispatcher = new messageDispatcher();
    IDatabase database = new MariaDB();
    DatabaseFilter databaseFilter = new DatabaseFilter();

    public Execution() {}

    public BotApiMethod messageDispatcher(String command, SendMessage message, Update update) {

        try {

            if (keyboardButtonsCommends.contains(command.toLowerCase())) {
                return messageDispatcher.keyboardButtonsHandler(message, command, inLine, approvedCompanies);
            }else if (update.hasCallbackQuery()){
                messageDispatcher.editedMessageReply(update,message,command);
            } else
                return messageDispatcher.inLineCallBackHandler(message, command, databaseFilter, approvedCompanies, database);


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

}

