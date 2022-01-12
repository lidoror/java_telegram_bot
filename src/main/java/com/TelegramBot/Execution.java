package com.TelegramBot;

import org.apache.commons.lang3.math.NumberUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import javax.validation.constraints.NotNull;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;


public class Execution {
    CustomKeyboard keyboard = new CustomKeyboard();
    Balance balance = new Balance();
    Company company = new Company();
    InlineKeyboard inLine = new InlineKeyboard();

     Execution(){}
    
    public void messageHandler(String command, SendMessage message) {

        switch (command.toLowerCase(Locale.ROOT)) {
            case "start"->{
                message.setText("Hi this is an expenses manager bot" +
                        "\nplease choose an option:");
                keyboard.keyboard1(message);
            }

            case "expenses" ->{
                message.setText("Please enter an expense" +
                        "\nenter the expense in this order \nproduct->price->company");
            }
            case "refund" ->{
                message.setText("please enter the refund\n" +
                        "enter the refund in this order \n" +
                        "order->-price->company\n" +
                        "dont forget the - sign before the price");
            }
            case "b"->
                    message.setText(balance.getBalance());

            case "s" ->
                    message.setText(balance.getSalary());


            case "balance"->
                    message.setText(balance.getSaving());

            case "overall expenses", "all expenses" , "all refunds"-> {
                message.setText("not supported yet");
                return;
            }
            case "key" ->{
                message.setText("choose your option:");
                inLine.inLineKeyboard1(message);
            }

            case "admin.center.control" -> {
               message.setText("choose an option: ");
               inLine.adminKeyboard(message);
           }


            default -> message.setText("We are very sorry, this function is not working yet.");
        }

        try {

            if(command.contains("SendChatId-")){
                message.setText(command.split("-")[1]);
                return;
            }

            if(command.contains(" ") && command.contains("-") && company.getList().contains(getCompany(command))){
                if (isNumeric(getPrice(command))) {
                    balance.addToBalance(getPrice(command));
                    message.setText("Refunded from balance");
                }

            }else if (command.contains(" ") && company.getList().contains(getCompany(command))) {
                if (isNumeric(getPrice(command))) {
                    balance.addToBalance(getPrice(command));
                    message.setText("Added to balance.");
                }
            }

        }catch (NumberFormatException e){
            message.setText("incorrect input");
            System.err.println("NumberFormatException");
        }catch(ArrayIndexOutOfBoundsException e){
            message.setText("Incorrect input");
            System.err.println("ArrayIndexOutOfBoundsException");
        }catch(Exception e){;
            System.err.println("exception");

        }

    }


    @NotNull
    public String getPrice(String price) {
        return price.split(" ")[1];
    }

    @NotNull
    public String getProduct(String product) {
        return product.split(" ")[0];
    }
    @NotNull
    public String getCompany(String company){
        return company.split(" ")[2];
    }
    @NotNull
    public boolean isNumeric(String str){
        return NumberUtils.isNumber(str);
    }
    
    @NotNull
    public StringBuilder getNote(String note){
        String[] messageSplitter = note.split(" ");
        StringBuilder comment = new StringBuilder();
        int i;
        for ( i = 3; i <= messageSplitter.length-2; i++) {
            comment.append(messageSplitter[i]);
            comment.append(" ");
        }
        comment.append(messageSplitter[i]);
        return comment;
    }

    private String inputCheck(String str){
        return
                "product: " + getProduct(str) +
                "\nprice: " + getPrice(str) +
                "\ncompany: " + getCompany(str) +
                "\nNote: "  + getNote(str);
    }



}

