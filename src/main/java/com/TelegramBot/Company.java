package com.TelegramBot;

import java.util.List;

public class Company {

    Company() {}

    private final List<String> company = List.of(
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
        return company;
    }
}
