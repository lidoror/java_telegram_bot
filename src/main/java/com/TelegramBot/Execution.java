package com.TelegramBot;

import org.apache.commons.lang3.math.NumberUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import javax.validation.constraints.NotNull;
import java.util.Locale;


public class Execution {
    String[] messageSplitter;
    CustomKeyboard keyboard = new CustomKeyboard();
    Balance balance = new Balance();
    Company company = new Company();

    Execution (){}

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


            default -> message.setText("We are very sorry, this function is not working yet.");


        }

        try {

            if(command.contains(" ") && command.contains("-") && company.getList().contains(getCompany(command))){
                if (isNumeric(getPrice(command))) {
                    balance.addToBalance(getPrice(command));
                    message.setText("Refunded from balance");
                }

            }else if (command.contains(" ") && company.getList().contains(getCompany(command))) {
                if (isNumeric(getPrice(command))) {
                    balance.addToBalance(getPrice(command));
                    message.setText("Added to balance.");
                    System.out.println(
                            "product: " + getProduct(command)+
                                    "\nprice: " + getPrice(command)+
                                    "\ncompany: " + getCompany(command)+
                                    "\nnote: " + getNote(command)
                    );
                }
            }

        }catch (NumberFormatException e){
            message.setText("incorrect input");
            System.out.println("Execution Exception\n" + e.getMessage());
        }



    }


    @NotNull
    private String getPrice(String price) {
        messageSplitter = price.split(" ");
        return messageSplitter[1];
    }

    @NotNull
    private String getProduct(String product) {
        messageSplitter = product.split(" ");
        return messageSplitter[0];
    }
    @NotNull
    private String getCompany(String company){
        messageSplitter = company.split(" ");
        return messageSplitter[2];
    }

    private boolean isNumeric(String str){
        return NumberUtils.isNumber(str);
    }

    private StringBuilder getNote(String note){
        messageSplitter = note.split(" ");
        StringBuilder comment = new StringBuilder(" ");
        for (int i = 3; i < messageSplitter.length; i++) {
            comment.append(messageSplitter[i]);
            comment.append(" ");
        }
        return comment;
    }


}

