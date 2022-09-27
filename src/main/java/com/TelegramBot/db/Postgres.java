package com.TelegramBot.db;

import com.TelegramBot.utils.FunctionsUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Postgres implements IDatabase {
    private final String postgresURL = "jdbc:postgresql://localhost:2468/postgres";
    private final String dbUser = "postgres";
    private final String dbPass = "Oranim14265!";


    private Connection getDbConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(postgresURL,dbUser,dbPass);

        } catch (SQLException e) {
            System.err.println("Exception postgres Class Connection method");
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void updateDB(String product, String price, String company, String note) {

        try(Connection conn = getDbConnection()) {
            String insertIntoShopping = "INSERT INTO shopping (product,price,company,note,purchase_date,index_value) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertIntoShopping);
            preparedStatement.setString(1,product);
            preparedStatement.setString(2,price);
            preparedStatement.setString(3,company);
            preparedStatement.setString(4, note);
            preparedStatement.setString(5, String.valueOf(LocalDate.now()));
            preparedStatement.setInt(6, setNewIndexValue());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException sqlException) {
            System.err.println("UpdateDb Exception");
            sqlException.printStackTrace();
        }
    }

    private int setNewIndexValue() throws SQLException{
        List<Integer> columID = dbRecordToList().stream().map(ShoppingMgmtRecord::columID).toList();
        return columID.get(columID.size() - 1)+1;
    }

    private void getDbDataToShoppingMgmt(List<ShoppingMgmtRecord> records, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM \"postDB\".bot.shopping");
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


    @Override
    public List<ShoppingMgmtRecord> dbRecordToList() throws SQLException {
        List<ShoppingMgmtRecord> records = new ArrayList<>();
        try(Connection conn = getDbConnection()){
            getDbDataToShoppingMgmt(records, conn);

        }catch (SQLException sqlException){
            throw new SQLException("Error at getRecordsAsList");
        }
        return records;
    }

    @Override
    public void setDbParameter(String command) {
        updateDB(FunctionsUtils.generateProductFromInput(command),
                FunctionsUtils.generateProductCostFromInput(command), FunctionsUtils.generateProductCompanyFromInput(command),
                String.valueOf(FunctionsUtils.generateProductNoteFromInput(command)));
    }


    @Override
    public boolean checkConnection() throws SQLException{
        Connection connection =
                DriverManager.getConnection(postgresURL,dbUser,dbPass);
        boolean isValid = connection.isValid(2);
        connection.close();
        return isValid;
    }




    //new implementation instead of getting only the record
    public Map<Integer,ShoppingMgmtRecord> dbListToMap()throws SQLException{
        Map<Integer,ShoppingMgmtRecord> dbRecordMap = new HashMap<>();
        for (var record:dbRecordToList()) {
            dbRecordMap.put(record.columID(),record);
        }
        return dbRecordMap;
    }


}
