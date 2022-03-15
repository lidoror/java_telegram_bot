package com.TelegramBot.db;

import com.TelegramBot.utils.Functions;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MariaDB implements IDB{

    private final String rootConnection = "jdbc:mariadb://localhost:3333/bot?"+
    "user=root&password=***REMOVED***&serverTimezone=UTC";
    private final String selectAllShopping = "Select * From shopping";


    public MariaDB(){}

    public void updateDB(String product, String price, String company, String note) {

        try(Connection conn = getConnection()) {
            String insertIntoShopping = "INSERT INTO shopping (product,price,company,note,purchase_date) VALUES (?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(insertIntoShopping);
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

    @Override
    public String readAll() {
        List<ShoppingMgmt> allData = new ArrayList<>();
        try(Connection conn = getConnection()){
            getDbDataToShoppingMgmt(allData, conn);

        }catch (SQLException sqlException){
            System.err.println("Error in read all");
            sqlException.printStackTrace();
        }
        return listOutputFormatter(allData);
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

    public boolean checkConnection() throws SQLException{
        Connection Connection = 
                DriverManager.getConnection(rootConnection);
        boolean isValid = Connection.isValid(2);
        Connection.close();
        return isValid;        
    }

    private void getDbDataToShoppingMgmt(List<ShoppingMgmt> records, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(selectAllShopping);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            ShoppingMgmt shoppingMgmt = new ShoppingMgmt(resultSet.getString(1) , resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4),resultSet.getString(5));
            records.add(shoppingMgmt);
        }
        resultSet.close();
        preparedStatement.close();
    }


    private List<ShoppingMgmt> getRecordsAsList() throws SQLException {
        List<ShoppingMgmt> records = new ArrayList<>();
        try(Connection conn = getConnection()){
            getDbDataToShoppingMgmt(records, conn);

        }catch (SQLException sqlException){
            throw new SQLException("Error at getRecordsAsList");
        }
        return records;
    }

    private String listOutputFormatter(List<ShoppingMgmt> listToFormat){
        return String.valueOf(listToFormat).replace("[","").replace(", ","").replace("]","");
    }

    public String getMonthByExpense(String month){
        List<ShoppingMgmt> dataByMonth = new ArrayList<>();
        try {
            dataByMonth = getRecordsAsList().stream().filter(date -> date.getPurchaseDate().split("-")[1].contains(month)).collect(Collectors.toList());

        }catch (SQLException sqlException){
            System.err.println("Error in getMonthExpense");
        }
        return listOutputFormatter(dataByMonth);
    }



    public String getMonthlyExpenses() {
        Functions functions = new Functions();
        List<ShoppingMgmt> monthlyData = new ArrayList<>();
        try {
            monthlyData = getRecordsAsList().stream()
                    .filter(date -> functions.checkForCurrentMonth(date.getPurchaseDate())).collect(Collectors.toList());

        }catch (SQLException sqlException){
            System.err.println("Error in getMonthlyExpensesNew");
        }
        return listOutputFormatter(monthlyData);
    }


    public String getTotalMonthSpending(){
        Functions functions = new Functions();
        double listSum = 0.0;
        try {
            List<String> prices = getRecordsAsList().stream().filter(date -> functions.checkForCurrentMonth(date.getPurchaseDate()))
                    .map(ShoppingMgmt::getPrice).collect(Collectors.toList());

            listSum = prices.stream().mapToDouble(Double::parseDouble).sum();

        }catch (SQLException sqlException){
            System.err.println("Error in getTotalMonthSpending");
        }
        return String.valueOf(listSum);
    }

    private List<ShoppingMgmt> getMonthlyCategoryAsList(String category){
        List<ShoppingMgmt> monthlyCatagoryData = new ArrayList<>();
        Functions functions = new Functions();
        try {
            monthlyCatagoryData = getRecordsAsList().stream()
                    .filter(filter -> functions.checkForCurrentMonth(filter.getPurchaseDate()) && filter.getCompany().equals(category))
                    .collect(Collectors.toList());
        }catch (SQLException sqlException){
            System.err.println("Error in getMonthlyCategoryRecord");
        }
        return monthlyCatagoryData;
    }

    public String getMonthlyCategoryRecord(String category){
        return listOutputFormatter(getMonthlyCategoryAsList(category));
    }

    public String getCategoryMonthlySpent(String category){
        List<String> monthlyCategorySum = getMonthlyCategoryAsList(category).stream().map(ShoppingMgmt::getPrice).collect(Collectors.toList());
        double sum = monthlyCategorySum.stream().mapToDouble(Double::parseDouble).sum();
        return String.valueOf(sum);
    }



}
