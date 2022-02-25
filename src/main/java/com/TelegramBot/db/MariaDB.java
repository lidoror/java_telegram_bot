package com.TelegramBot.db;

import com.TelegramBot.utils.Operations;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class MariaDB implements IDB{

    private final String rootConnection = "jdbc:mariadb://localhost:3334/bot?"+
    "user=root&password=Oranim14265!&serverTimezone=UTC";

    public MariaDB(){}

    public boolean checkConnection() throws SQLException{
        Connection Connection = 
                DriverManager.getConnection(rootConnection);
        boolean isValid = Connection.isValid(2);
        Connection.close();
        return isValid;        
    }



    private Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(rootConnection);

        } catch (SQLException e) {
            System.err.println("Exception MariraDB Class Connection method");
            e.printStackTrace();
        }
        return connection;
    }


    @Override
    public String readAll() {
        String sqlQuery = "Select * From shopping";
        List<ShoppingMgnt> allData = new ArrayList<>();
        try(Connection conn = getConnection()){
            PreparedStatement pstms = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstms.executeQuery();

            while (resultSet.next()){
                ShoppingMgnt shoppingMgnt = new ShoppingMgnt(resultSet.getString(1) , resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4),resultSet.getString(5));
                allData.add(shoppingMgnt);
            }
            resultSet.close();
            pstms.close();

        }catch (SQLException sqlException){
            System.err.println("Error in read all");
            sqlException.printStackTrace();
        }

        return String.valueOf(allData).replace("[","").replace(", ","").replace("]","");
    }

    public String getProductPrice()  {
        String sqlQuery = "Select * From shopping";
        List<ShoppingMgnt> productPrice = new ArrayList<>();
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                ShoppingMgnt shopping = new ShoppingMgnt(resultSet.getString(1), resultSet.getString(2));
                productPrice.add(shopping);
            }
            resultSet.close();
            pstmt.close();

        }catch (SQLException sqlException){
            System.err.println("SQLException getProductPrice");
        }
        return String.valueOf(productPrice).replace("[","").replace(",","\b").replace("]","");
    }




    public void updateDB(String product, String price, String company, String note) {

        String sqlQuery = "INSERT INTO shopping (product,price,company,note,purchase_date) VALUES (?,?,?,?,?)";
        try(Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setString(1,product);
            pstmt.setString(2,price);
            pstmt.setString(3,company);
            pstmt.setString(4, note);
            pstmt.setString(5, String.valueOf(LocalDate.now()));
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException sqlException) {
            System.err.println("SQL error insertion error");
            sqlException.printStackTrace();
        }
    }

    public String sumAllMoneySpent(){
        String sqlQuery = "Select * From shopping";
        List<Double> priceSum = new ArrayList<>();
        double sum = 0;
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                ShoppingMgnt prices = new ShoppingMgnt(resultSet.getString(2));
                priceSum.add(Double.parseDouble(prices.getPrice()));

            }
            for (Double looper : priceSum) {
                sum += looper;
            }

            resultSet.close();
            pstmt.close();

        }catch (SQLException sqlException){
            System.err.println("SQLException sumAllMoneySpend");
        }
        return String.valueOf(sum);
    }

    public String getMonthlyExpenses(){
        Operations operations = new Operations();
        String sqlQuery = "Select * From shopping";
        List<ShoppingMgnt> monthlyData = new ArrayList<>();
        try(Connection conn = getConnection()){
            PreparedStatement pstms = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstms.executeQuery();

            while (resultSet.next()){
                if (operations.checkForCurrentMonth(resultSet.getString(5))) {
                    ShoppingMgnt shoppingMgnt = new ShoppingMgnt(resultSet.getString(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
                    monthlyData.add(shoppingMgnt);
                }
            }
            resultSet.close();
            pstms.close();

        }catch (SQLException sqlException){
            System.err.println("Error in read all");
            sqlException.printStackTrace();
        }

        return String.valueOf(monthlyData).replace("[","").replace(", ","").replace("]","");
    }

    public String getMonthlySpent(){
        Operations operations = new Operations();
        String sqlQuery = "Select * From shopping";
        List<Double> monthlySum = new ArrayList<>();
        double sum = 0;
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                if (operations.checkForCurrentMonth(resultSet.getString(5))) {
                    ShoppingMgnt prices = new ShoppingMgnt(resultSet.getString(2));
                    monthlySum.add(Double.parseDouble(prices.getPrice()));
                }

            }
            for (Double looper : monthlySum) {
                sum += looper;
            }

            resultSet.close();
            pstmt.close();

        }catch (SQLException sqlException){
            System.err.println("SQLException sumAllMoneySpend");
        }
        return String.valueOf(sum);
    }




    public String getMonthlyCategoryRecord(String category){
        Operations operations = new Operations();
        String sqlQuery = "Select * From shopping";
        List<ShoppingMgnt> monthlyGeneralData = new ArrayList<>();
        try(Connection conn = getConnection()){
            PreparedStatement pstms = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstms.executeQuery();

            while (resultSet.next()){
                if (operations.checkForCurrentMonth(resultSet.getString(5))&& resultSet.getString(3).equals(category)) {
                    ShoppingMgnt shoppingMgnt = new ShoppingMgnt(resultSet.getString(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
                    monthlyGeneralData.add(shoppingMgnt);
                }
            }
            resultSet.close();
            pstms.close();

        }catch (SQLException sqlException){
            System.err.println("Error in read all");
            sqlException.printStackTrace();
        }

        return String.valueOf(monthlyGeneralData).replace("[","").replace(", ","").replace("]","");
    }

    public String getCategoryMonthlySpent(String category){
        Operations operations = new Operations();
        String sqlQuery = "Select * From shopping";
        List<Double> monthlySum = new ArrayList<>();
        double sum = 0;
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                if (operations.checkForCurrentMonth(resultSet.getString(5))&& resultSet.getString(3).equals(category)) {
                    ShoppingMgnt prices = new ShoppingMgnt(resultSet.getString(2));
                    monthlySum.add(Double.parseDouble(prices.getPrice()));
                }

            }
            for (Double looper : monthlySum) {
                sum += looper;
            }

            resultSet.close();
            pstmt.close();

        }catch (SQLException sqlException){
            System.err.println("SQLException sumAllMoneySpend");
        }
        return String.valueOf(sum);
    }


}
