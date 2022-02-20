package com.TelegramBot.utils;

import java.util.List;

public class Company {

    public Company() {}

    private final List<String> companyList = List.of(
            "דלק",
            "כללי",
            "משותף",
            "קניות",
            "אוכל"

    );

    public List<String> getList(){
        return companyList;
    }





}
