package com.TelegramBot.db;


public interface IDB {

    void readAll();

    void updateDB(String product, String price, String company, String note);


}
