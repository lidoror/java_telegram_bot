package com.TelegramBot.codeHandler;

import com.TelegramBot.balanceMgmt.Balance;
import com.TelegramBot.db.MariaDB;
import com.TelegramBot.keyboards.CustomKeyboard;
import com.TelegramBot.keyboards.InlineKeyboard;
import com.TelegramBot.utils.Company;
import com.TelegramBot.utils.Functions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.sql.SQLException;


public class Execution {
    CustomKeyboard keyboard = new CustomKeyboard();
    Balance balance = new Balance();
    Company company = new Company();
    InlineKeyboard inLine = new InlineKeyboard();
    Functions functions = new Functions();
    MariaDB db = new MariaDB();

     public Execution(){}
    
    public void messageHandler(String command, SendMessage message) {

        try {
         switch (command.toLowerCase().replace("/","")) {
            case "start"->{
                message.setText(functions.getStartMessage());
                keyboard.keyboard1(message);
            }
            case "expenses" ->{
                message.setText(functions.getExpenseMessage());
            }
            case "refund" ->{
                message.setText(functions.getRefundMessage());
            }
            case "balance"->
                    message.setText("Balance: \n"+balance.getStringBalance());

            case "monthly spent"-> {
                message.setText(functions.chooseOptionPrompt());
                inLine.monthlySum(message);
                return;
            }
            case "monthly expenses"->{
                message.setText(functions.chooseOptionPrompt());
                inLine.monthlyCategory(message);
                return;
            }
            case "overall expenses" -> {
                message.setText(functions.chooseOptionPrompt());
                inLine.showMonths(message);
                return;
            }
            case "admincentercontrol" -> {
               message.setText(functions.chooseOptionPrompt());
               inLine.adminKeyboard(message);
               return;
           }

            case "showcompany" -> message.setText(functions.getCompanyFormatter(company.getList()));

            case "check" -> {
                message.setText("Check Mode");

            }

            default -> message.setText("We are very sorry, this function is not working yet.");
        }


            if (command.contains("monthlyCategory-") || command.contains("monthlySum-") ){
                functions.monthlyCategory(command, message);
                return;
            }

            if (command.contains("monthDbCheck-")){
                String month = command.split("-")[1];
                if (db.getMonthByExpense(month).isEmpty()){
                    message.setText("No Expenses in month "+month+".");
                }else {
                    message.setText("Month "+month+" Expenses:\n"+db.getMonthByExpense(month));
                }
            }
            if(command.contains("SendChatId.admin-")){
                message.setText(command.split("-")[1]);
                return;
            }
            if (command.equals("checkDBS.admin")){
                message.setText(functions.dbKeyboardCheck());
                return;
            }

            if (command.contains(" ") && company.getList().contains(functions.getCompanyFromInput(command))) {
                if (functions.isNumeric(functions.getPriceFromInput(command))) {
                    balance.addToBalance(functions.getPriceFromInput(command));
                    functions.setDbParameter(command);
                    message.setText("Data added to bot.");
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
            message.setText("Some Problem occurred");
            System.err.println("exception\n");
            e.printStackTrace();

        }

    }

}

