package com.oranim.telegrambot.utils;

import java.time.LocalDate;

public class DateHandler {

    public String getCurrentMonth() {
        String addZeroToMonthIfValueLessThan10 = "0%d".formatted(LocalDate.now().getMonthValue());
        String currentMonth = LocalDate.now().getMonthValue() < 10 ?  addZeroToMonthIfValueLessThan10 : String.valueOf(LocalDate.now().getMonthValue());
        return currentMonth;
    }

    public String getCurrentYear(){
        String currentYear = String.valueOf(LocalDate.now().getYear());
        return currentYear;
    }
}
