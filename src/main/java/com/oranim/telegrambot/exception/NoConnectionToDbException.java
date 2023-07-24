package com.oranim.telegrambot.exception;

import java.sql.SQLException;

public class NoConnectionToDbException extends SQLException {

    public NoConnectionToDbException(String message){
        super(message);
    }
}
