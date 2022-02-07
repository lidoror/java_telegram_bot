package com.TelegramBot.db;

import java.sql.*;
import java.time.LocalDate;


public class MariaDB implements IDB{

    private String username = "***REMOVED***";
    private String password = "***REMOVED***";
    private String DBUrl = "jdbc:mariadb://localhost:3333/BOT?";
    private String rootConnection = "jdbc:mariadb://localhost:3333/bot?"+
    "user=root&password=***REMOVED***&serverTimezone=UTC";

    public MariaDB(){}

    public boolean checkConnection() throws SQLException{
        Connection Connection = 
                DriverManager.getConnection(rootConnection);
        boolean isValid = Connection.isValid(2);
        Connection.close();
        return isValid;        
    }

    public boolean checkLidorConnection() throws SQLException{
        Connection connection =
                DriverManager.getConnection(DBUrl, username, password);
        boolean isValid = connection.isValid(2);
        connection.close();
        return isValid;
    }

    private Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(rootConnection);

        } catch (SQLException e) {
            System.err.println("Exception MariraDB Connection method");
            e.printStackTrace();
        }
        return connection;
    }




    @Override
    public void readAll() {

    }


    public void updateDB(String product, int price, String company, String note, String purchaseDate) {
        Shopping shopping = new Shopping();
        String sqlQuery = "INSERT INTO shopping(product,price,company,note,purchase_date ) VALUES(?,?,?,?,?)";
        try(Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setString(1,product);
            pstmt.setInt(2,price);
            pstmt.setString(3,company);
            pstmt.setString(4, note);
            pstmt.setString(5,purchaseDate);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
}
