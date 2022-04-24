package com.TelegramBot.codeHandler;

import com.TelegramBot.balanceMgmt.Balance;
import com.TelegramBot.db.MariaDB;
import com.TelegramBot.keyboards.CustomKeyboard;
import com.TelegramBot.keyboards.InlineKeyboard;
import com.TelegramBot.utils.Company;
import com.TelegramBot.utils.Const;
import com.TelegramBot.utils.Functions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.sql.SQLException;


public class Execution {
    CustomKeyboard keyboard = new CustomKeyboard();
    Balance balance = new Balance();
    Company company = new Company();
    InlineKeyboard inLine = new InlineKeyboard();
    Functions functions = new Functions();
    MariaDB db = new MariaDB();

    public Execution() {}

    public void messageHandler(String command, SendMessage message, Update update) {

        try {
            switch (command.toLowerCase().replace("/", "")) {
                case "start" -> {
                    message.setText(functions.getStartMessage());
                    keyboard.keyboard1(message);
                }
                case "expenses" -> {
                    message.setText(functions.getExpenseMessage());
                }
                case "refund" -> {
                    message.setText(functions.getRefundMessage());
                }
                case "balance" -> message.setText("Balance: \n" + balance.getStringBalance());

                case "monthly spent" -> {
                    message.setText(functions.chooseOptionPrompt());
                    inLine.monthlySum(message);
                    return;
                }
                case "monthly expenses" -> {
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
                    message.setText("check");
                    inLine.monthlyExpensesKeyboard(message);
                }

                default -> message.setText("We are very sorry, this function is not working yet.");
            }



            if (command.contains("monthlyCategory-") || command.contains("monthlySum-")) {
                functions.monthlyCategory(command, message);
                return;
            }
            if (command.contains("GetTransactionInPlace"+Const.INLINE_SEPARATOR)) {
                Integer transactionNumber = Integer.valueOf(command.split(Const.INLINE_SEPARATOR)[1]);
                message.setText(db.transactionMapper(transactionNumber));
                return;
            }

            if (command.contains("monthDbCheck"+ Const.INLINE_SEPARATOR)) {
                String month = command.split(Const.INLINE_SEPARATOR)[1];
                if (db.getMonthByExpense(month).isEmpty()) {
                    message.setText("No Expenses in month " + month + ".");
                } else {
                    message.setText("Month " + month + " Expenses:\n" + db.getMonthByExpense(month));
                }
            }
            if (command.contains("SendChatId.admin"+Const.INLINE_SEPARATOR)) {
                message.setText(command.split(Const.INLINE_SEPARATOR)[1]);
                return;
            }
            if (command.equals("checkDBS.admin")) {
                message.setText(functions.dbStatusCheckInline());
                return;
            }

            if (command.contains(" ") && company.getList().contains(functions.getCompanyFromInput(command))) {
                if (functions.isNumeric(functions.getPriceFromInput(command))) {
                    functions.dbInsertionAndValidation(command,message);
                }
            }

        } catch (NumberFormatException e) {
            message.setText("incorrect input");
            System.err.println("NumberFormatException");
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            message.setText("Incorrect input");
            System.err.println("ArrayIndexOutOfBoundsException");
            e.printStackTrace();
        } catch (SQLException sqlException) {
            System.err.println("No Connection to DB\n");
            sqlException.printStackTrace();
            message.setText("No Connection to DB");
        } catch (Exception e) {
            message.setText("Some Problem occurred");
            System.err.println("exception\n");
            e.printStackTrace();

        }

    }

}

