package com.TelegramBot.Exception;

import java.sql.SQLException;

public class NoConnectionToDbException extends SQLException {

    public NoConnectionToDbException(String message){
        super(message);
    }
}
