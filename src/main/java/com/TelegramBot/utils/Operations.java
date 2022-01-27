package com.TelegramBot.utils;

import org.apache.commons.lang3.math.NumberUtils;

import javax.validation.constraints.NotNull;

public class Operations {

    public Operations(){}

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
