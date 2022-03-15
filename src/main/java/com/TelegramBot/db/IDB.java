package com.TelegramBot.db;


import java.sql.Connection;

public interface IDB {

    String readAll();

    void updateDB(String product, String price, String company, String note);




}
