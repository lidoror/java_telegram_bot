package com.TelegramBot.db;

import com.TelegramBot.utils.Functions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;


public class MariaDB implements IDB{

    private final String rootConnection = "jdbc:mariadb://localhost:3334/bot?"+
    "user=root&password=Oranim14265!&serverTimezone=UTC";
    private final String selectAllShopping = "Select * From shopping";

    public MariaDB(){}

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

    public void updateDB(String product, String price, String company, String note) {

        try(Connection conn = getConnection()) {
            String insertIntoShopping = "INSERT INTO shopping (product,price,company,note,purchase_date,index_value) VALUES (?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(insertIntoShopping);
            pstmt.setString(1,product);
            pstmt.setString(2,price);
            pstmt.setString(3,company);
            pstmt.setString(4, note);
            pstmt.setString(5, String.valueOf(LocalDate.now()));
            pstmt.setInt(6, setNewIndexValue());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException sqlException) {
            System.err.println("SQL error insertion error");
            sqlException.printStackTrace();
        }
    }

    private int setNewIndexValue() throws SQLException{
        List<Integer> columID = getRecordsAsList().stream().map(ShoppingMgmtRecord::columID).toList();
        return columID.get(columID.size() - 1)+1;
    }

    private void getDbDataToShoppingMgmt(List<ShoppingMgmtRecord> records, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(selectAllShopping);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            String product = resultSet.getString(1);
            String price = resultSet.getString(2);
            String company = resultSet.getString(3);
            String note = resultSet.getString(4);
            String purchaseDate = resultSet.getString(5);
            int columID = resultSet.getInt(6);

            ShoppingMgmtRecord shoppingMgmtRecord =
                    new ShoppingMgmtRecord(product,price,company,note,purchaseDate,columID);
            records.add(shoppingMgmtRecord);
        }
        resultSet.close();
        preparedStatement.close();
    }


    private List<ShoppingMgmtRecord> getRecordsAsList() throws SQLException {
        List<ShoppingMgmtRecord> records = new ArrayList<>();
        try(Connection conn = getConnection()){
            getDbDataToShoppingMgmt(records, conn);

        }catch (SQLException sqlException){
            throw new SQLException("Error at getRecordsAsList");
        }
        return records;
    }


    private String listOutputFormatter(List<ShoppingMgmtRecord> listToFormat){
        return String.valueOf(listToFormat).replace("[","").replace(", ","").replace("]","");
    }


    @Override
    public String readAll() {
        List<ShoppingMgmtRecord> allData = new ArrayList<>();
        try(Connection conn = getConnection()){
            getDbDataToShoppingMgmt(allData, conn);

        }catch (SQLException sqlException){
            System.err.println("Error in read all");
            sqlException.printStackTrace();
        }
        return listOutputFormatter(allData);
    }


    public boolean checkConnection() throws SQLException{
        Connection Connection =
                DriverManager.getConnection(rootConnection);
        boolean isValid = Connection.isValid(2);
        Connection.close();
        return isValid;
    }



    public String getMonthByExpense(String month) throws SQLException {
        return listOutputFormatter(Optional.of(getRecordsAsList().stream()
                .filter(date -> date.purchaseDate().split("-")[1].contains(month))
                .collect(Collectors.toList())).orElseThrow(()->new SQLException("Exception in getMonthByExpenses")));
    }

    public String getMonthlyExpenses() throws SQLException {
        Functions functions = new Functions();
        return listOutputFormatter(Optional.of(getRecordsAsList().stream()
                .filter(date -> functions.checkForCurrentMonth(date.purchaseDate()))
                .collect(Collectors.toList())).orElseThrow(()->new SQLException("Exception in getMonthlyExpenses")));
    }

    public String getTotalMonthSpending() throws SQLException {
        Functions functions = new Functions();
        return String.valueOf(Optional.of(getRecordsAsList().stream().filter(date -> functions.checkForCurrentMonth(date.purchaseDate()))
                .map(ShoppingMgmtRecord::price).mapToDouble(Double::parseDouble).sum()).orElseThrow(()-> new SQLException("Exception in getTotalSpending")));
    }

    private List<ShoppingMgmtRecord> getMonthlyCategoryAsList(String category)throws SQLException{
        Functions functions = new Functions();
        return Optional.of(getRecordsAsList().stream()
                .filter(filter -> functions.checkForCurrentMonth(filter.purchaseDate()) && filter.company().equals(category))
                .collect(Collectors.toList())).orElseThrow(()-> new SQLException("Exception in getMonthlyCategoryAsList"));
        }

    public String getMonthlyCategoryRecord(String category)throws SQLException{
        return listOutputFormatter(getMonthlyCategoryAsList(category));
    }

    public String getCategoryMonthlySpent(String category) throws SQLException{
        return String.valueOf(getMonthlyCategoryAsList(category).stream().map(ShoppingMgmtRecord::price).mapToDouble(Double::parseDouble).sum());
    }

    public List<String> getCurrentMonthProducts()throws SQLException{
        Functions functions = new Functions();
        return getRecordsAsList().stream().filter(product -> functions.checkForCurrentMonth(product.purchaseDate()))
                .map(ShoppingMgmtRecord::product).collect(Collectors.toList());
    }

    public List<String> getCurrentMonthPrices()throws SQLException{
        Functions functions = new Functions();
        return getRecordsAsList().stream().filter(price -> functions.checkForCurrentMonth(price.purchaseDate()))
                .map(ShoppingMgmtRecord::price).collect(Collectors.toList());
    }
    public List<ShoppingMgmtRecord> getCurrentMonthShoppingList()throws SQLException{
        Functions functions = new Functions();
        return getRecordsAsList().stream().filter(price -> functions.checkForCurrentMonth(price.purchaseDate()))
                .collect(Collectors.toList());
    }

    public String transactionMapper(Integer mapKey)throws SQLException{

        Map<Integer, ShoppingMgmtRecord> mapper = new HashMap<>();
        for (var looper:getRecordsAsList()) {
            mapper.put(looper.columID(), looper);
        }
        return String.valueOf(mapper.get(mapKey));
    }

    public int getDataListSize()throws SQLException{
        return getRecordsAsList().size();

    }

}
