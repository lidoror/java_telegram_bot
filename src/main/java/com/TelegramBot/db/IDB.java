package com.TelegramBot.db;


public interface IDB {

    String readAll();

    void updateDB(String product, String price, String company, String note);


}
