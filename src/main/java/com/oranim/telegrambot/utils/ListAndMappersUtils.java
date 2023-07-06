package com.oranim.telegrambot.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAndMappersUtils {

    public String formatNumberMonthsToNames(String month) {
        Map<String,String> monthMapper = new HashMap<>();
        monthMapper.put("1","January");
        monthMapper.put("2","February");
        monthMapper.put("3","March");
        monthMapper.put("4","April");
        monthMapper.put("5","May");
        monthMapper.put("6","June");
        monthMapper.put("7","July");
        monthMapper.put("8","August");
        monthMapper.put("9","September");
        monthMapper.put("10","October");
        monthMapper.put("11","November");
        monthMapper.put("12","December");

        return monthMapper.get(month);
    }


    public List<String> getKeyboardButtonsCommands() {
        return List.of("/start", "expenses", "refund", "balance", "monthly spent",
                "monthly expenses", "overall expenses", "/showcompany", "admincenter");
    }




    public List<String> getApprovedCompanies() {
        return List.of("דלק", "כללי", "משותף", "קניות", "אוכל");
    }

    public List<String> getEditedMessage() {
        return List.of("Back",
                "monthlySum-Fuel",
                "monthlyCategory-All",
                "monthlySum-House Shopping",
                "monthlySum-Shopping",
                "monthlySum-Food",
                "monthlySum-General",
                "monthlySum-All",
                "monthlyCategory-Fuel",
                "monthlyCategory-House Shopping",
                "monthlyCategory-Shopping",
                "monthlyCategory-Food",
                "monthlyCategory-General",
                "checkDBS.admin",
                "SendChatId.admin",
                "monthDbCheck",
                "GET_YEAR");
    }

    public List<String> getMessagesWithCallbackList(){
        return List.of(
                "GetTransactionInPlace",
                "salary"

        );
    }

    public List<String> getKeyboardButtonText(){
        return List.of(
                "start",
                "expenses",
                "refund",
                "balance",
                "monthly spent",
                "monthly expenses",
                "overall expenses",
                "admincenter",
                "showcompany"

        );
    }


}
