package com.TelegramBot.db;

import com.TelegramBot.utils.Functions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class MariaDB implements IDB{

    private final String rootConnection = "jdbc:mariadb://localhost:3306/bot?"+
    "user=root&password=Oranim14265!&serverTimezone=UTC";
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
            pstmt.setLong(1,getLastIndex()+1);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException sqlException) {
            System.err.println("SQL error insertion error");
            sqlException.printStackTrace();
        }
    }

    private Long getLastIndex()throws SQLException{
        Long lastIndex = null;
        try(Connection connection = getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(selectAllShopping);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                lastIndex = resultSet.getLong(1);
            }
        }
        return lastIndex;
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
                    resultSet.getString(3), resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6));
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

    public String getMonthByExpense(String month) throws SQLException {
        return listOutputFormatter(Optional.of(getRecordsAsList().stream()
                .filter(date -> date.getPurchaseDate().split("-")[1].contains(month))
                .collect(Collectors.toList())).orElseThrow(()->new SQLException("Exception in getMonthByExpenses")));
    }

    public String getMonthlyExpenses() throws SQLException {
        Functions functions = new Functions();
        return listOutputFormatter(Optional.of(getRecordsAsList().stream()
                .filter(date -> functions.checkForCurrentMonth(date.getPurchaseDate()))
                .collect(Collectors.toList())).orElseThrow(()->new SQLException("Exception in getMonthlyExpenses")));
    }

    public String getTotalMonthSpending() throws SQLException {
        Functions functions = new Functions();
        return String.valueOf(Optional.of(getRecordsAsList().stream().filter(date -> functions.checkForCurrentMonth(date.getPurchaseDate()))
                .map(ShoppingMgmt::getPrice).mapToDouble(Double::parseDouble).sum()).orElseThrow(()-> new SQLException("Exception in getTotalSpending")));
    }

    private List<ShoppingMgmt> getMonthlyCategoryAsList(String category)throws SQLException{
        Functions functions = new Functions();
        return Optional.of(getRecordsAsList().stream()
                .filter(filter -> functions.checkForCurrentMonth(filter.getPurchaseDate()) && filter.getCompany().equals(category))
                .collect(Collectors.toList())).orElseThrow(()-> new SQLException("Exception in getMonthlyCategoryAsList"));
        }

    public String getMonthlyCategoryRecord(String category)throws SQLException{
        return listOutputFormatter(getMonthlyCategoryAsList(category));
    }

    public String getCategoryMonthlySpent(String category) throws SQLException{
        return String.valueOf(getMonthlyCategoryAsList(category).stream().map(ShoppingMgmt::getPrice).mapToDouble(Double::parseDouble).sum());
    }

    public List<String> getProduct()throws SQLException{
        Functions functions = new Functions();
        return getRecordsAsList().stream().filter(product -> functions.checkForCurrentMonth(product.getPurchaseDate()))
                .map(ShoppingMgmt::getProduct).collect(Collectors.toList());
    }

    public List<String> getPrice()throws SQLException{
        Functions functions = new Functions();
        return getRecordsAsList().stream().filter(price -> functions.checkForCurrentMonth(price.getPurchaseDate()))
                .map(ShoppingMgmt::getPrice).collect(Collectors.toList());
    }

}
