package com.oranim.telegrambot.db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MessagesDAOTest {
    MessagesDAO db = new MessagesDAO();

    @Test
    void checkConnection() throws SQLException {
        assertThat(db.checkConnection()).isTrue();
    }



}