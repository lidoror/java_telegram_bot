package com.TelegramBot.codeHandler;

import com.TelegramBot.balanceMgmt.Balance;
import com.TelegramBot.db.MariaDB;
import com.TelegramBot.keyboards.CustomKeyboard;
import com.TelegramBot.keyboards.InlineKeyboard;
import com.TelegramBot.utils.Company;
import com.TelegramBot.utils.Operations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.sql.SQLException;
import java.util.Locale;


public class Execution {
    CustomKeyboard keyboard = new CustomKeyboard();
    Balance balance = new Balance();
    Company company = new Company();
    InlineKeyboard inLine = new InlineKeyboard();
    Operations operations = new Operations();
    MariaDB db = new MariaDB();

     public Execution(){}
    
    public void messageHandler(String command, SendMessage message) {

        switch (command.toLowerCase(Locale.ROOT)) {
            case "start"->{
                message.setText(operations.getStartMessage());
                keyboard.keyboard1(message);
            }

            case "expenses" ->{
                message.setText(operations.getExpenseMessage());
            }
            case "refund" ->{
                message.setText(operations.getRefundMessage());
            }
            case "balance"->
                    message.setText("Balance: \n"+balance.getStringBalance());

            case "monthly spent"-> {
                message.setText(operations.chooseOptionPrompt());
                inLine.monthlySum(message);
                return;
            }
            case "monthly expenses"->{
                message.setText(operations.chooseOptionPrompt());
                inLine.monthlyCategory(message);
                return;
            }
            case "overall expenses" -> {
                message.setText(db.readAll());
                return;
            }
            case "admin.center.control" -> {
               message.setText(operations.chooseOptionPrompt());
               inLine.adminKeyboard(message);
               return;
           }
           case "sl" -> message.setText(String.valueOf(company.getList()));

           default -> message.setText("We are very sorry, this function is not working yet.");
        }

        try {
            if (command.contains("monthlyCategory.")){
                operations.monthlyCategory(command, message);
                return;
            }

            if (command.contains("monthlySum.")){
                operations.monthlySum(command,message);
                return;
            }


            if(command.contains("SendChatId.admin-")){
                message.setText(command.split("-")[1]);
                return;
            }
            if (command.equals("checkDBS.admin")){
                message.setText(operations.dbKeyboardCheck());
                return;
            }

            if (command.contains(" ") && company.getList().contains(operations.getCompany(command))) {
                if (operations.isNumeric(operations.getPrice(command))) {
                    balance.addToBalance(operations.getPrice(command));
                    operations.setDbParameter(command);
                    message.setText("Data Added.");
                }
            }

        }catch (NumberFormatException e){
            message.setText("incorrect input");
            System.err.println("NumberFormatException");
        }catch(ArrayIndexOutOfBoundsException e){
            message.setText("Incorrect input");
            System.err.println("ArrayIndexOutOfBoundsException");
        }catch (SQLException sqlException){
            System.err.println("No Connection to DB");
            message.setText("No Connection to DB");
        } catch(Exception e){
            System.err.println("exception");

        }

    }




}

