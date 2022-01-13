package com.TelegramBot;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MariaDBTest {
    MariaDB db = new MariaDB();

    @Test
    void checkConnection() throws SQLException {
        assertThat(db.checkConnection()).isTrue();
    }

    @Test
    void checkLidorConnection() throws SQLException{
        assertThat(db.checkLidorConnection()).isTrue();
    }
}