package com.TelegramBot.utils;


import java.util.List;


public class Company {

    public Company() {}

    private final List<String> companyList = List.of(
            "דלק",
            "חשבונות",
            "כללי",
            "משותף",
            "ksp",
            "אמאזון",
            "קניות",
            "אוכל",
            "כביש6",
            "אפל",
            "משחקים",
            "עלי"
    );

    public List<String> getList(){
        return companyList;
    }





}
