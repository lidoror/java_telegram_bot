package com.oranim.telegrambot.db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MariaDBTest {
    MariaDB db = new MariaDB();

    @Test
    void checkConnection() throws SQLException {
        assertThat(db.checkConnection()).isTrue();
    }



}