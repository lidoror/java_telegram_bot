package com.TelegramBot;

import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {
        
        MariaDB db = new MariaDB();
        try{
        db.checkLidorConnection();
        System.out.println("connected");
        }catch (SQLException e){
            System.out.println( e.getMessage());
        }
    }
    
}
