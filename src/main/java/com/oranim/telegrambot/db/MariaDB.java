package com.oranim.telegrambot.db;

import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.FunctionsUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class MariaDB implements IDatabase {

    private final String rootConnection = System.getenv("MARIADB_URL");



    public MariaDB(){}

    /**
     * This method establish connection with the mariadb databaes
     * @return the object of the connection
     */
    private Connection getDbConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(rootConnection);

        } catch (SQLException sqlException) {
            BotLogging.setCriticalLog(classLog("getDBConnection", Arrays.toString(sqlException.getStackTrace())));
        }
        return connection;
    }


    /**
     * this method gets 4 arguments from the user and add 2 generated arguments update them in the database , this is the wat we update data in the DB
     * @param product the product the user send
     * @param price the price the user send
     * @param company the category the user send
     * @param note the note the user send
     */
    @Override
    public void insertDataToDB(String product, String price, String company, String note) {

        try(Connection conn = getDbConnection()) {
            CallableStatement callableStatement = conn.prepareCall("{ call insertDataToShopping(?,?,?,?,?) }");
            callableStatement.setString(1, product);
            callableStatement.setString(2, price);
            callableStatement.setString(3, company);
            callableStatement.setString(4, note);
            callableStatement.setString(5, String.valueOf(LocalDate.now()));
            callableStatement.executeUpdate();
            callableStatement.close();

        } catch (SQLException sqlException) {
            BotLogging.setCriticalLog(classLog("updateDB", Arrays.toString(sqlException.getStackTrace())));

        }
    }

    private List<ShoppingMgmtRecord> returnRecordsAsShoppingMGMT(List<ShoppingMgmtRecord> records, ResultSet resultSet) throws SQLException {

        String product = resultSet.getString(1);
        String price = resultSet.getString(2);
        String company = resultSet.getString(3);
        String note = resultSet.getString(4);
        String purchaseDate = resultSet.getString(5);
        int index_value = resultSet.getInt(6);

        records.add(
                new ShoppingMgmtRecord(
                        product,price,company,note,purchaseDate,index_value
                )
        );
        return records;
    }



    public List<ShoppingMgmtRecord> getItemsFromDbBetweenDates(String startDate, String endDate){
        List<ShoppingMgmtRecord> records = new ArrayList<>();
        try(Connection conn = getDbConnection()){
            CallableStatement callableStatement = conn.prepareCall("{ call getRecordsByDate(? ,?) }");
            callableStatement.setString(1,startDate);
            callableStatement.setString(2,endDate);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()){
                records = returnRecordsAsShoppingMGMT(records, resultSet);
            }
            resultSet.close();
            callableStatement.close();

        }catch (SQLException sqlException){

        }
        return records;
    }



    /**
     * create call that returns all data in database
     * @return list of the loaded data from the DB
     * @throws SQLException
     */
    @Override
    public List<ShoppingMgmtRecord> dbRecordToList() throws SQLException {
        List<ShoppingMgmtRecord> records = new ArrayList<>();
        try(Connection conn = getDbConnection()){
            CallableStatement callableStatement = conn.prepareCall("{ call getAllDbRecords() }");
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                returnRecordsAsShoppingMGMT(records, resultSet);
            }



        }catch (SQLException sqlException){
            BotLogging.setCriticalLog(classLog("dbRecordToList", Arrays.toString(sqlException.getStackTrace())));
            throw new SQLException("Error at getRecordsAsList");
        }
        return records;
    }

    /**
     * generate from the user input data in the order needed for the DB
     * @param command the user from the input
     */
    @Override
    public void setDbParameter(String command) {
        insertDataToDB(FunctionsUtils.generateProductFromInput(command),
                FunctionsUtils.generateProductCostFromInput(command), FunctionsUtils.generateProductCompanyFromInput(command),
                String.valueOf(FunctionsUtils.generateProductNoteFromInput(command)));
    }

    /**
     * check the connection to the DB
     * @return if the connection is valid or not
     * @throws SQLException
     */
    @Override
    public boolean checkConnection() throws SQLException{
        Connection connection =
                DriverManager.getConnection(rootConnection);
        boolean isValid = connection.isValid(2);
        connection.close();
        return isValid;
    }


    /**
     * load the data generated to the list into a map
     * @return map of data generated from db
     * @throws SQLException
     */
    public Map<Integer,ShoppingMgmtRecord> dbListToMap()throws SQLException{
        Map<Integer,ShoppingMgmtRecord> dbRecordMap = new HashMap<>();
        for (var record : dbRecordToList()) {
            dbRecordMap.put(record.columID(),record);
        }
        return dbRecordMap;
    }

    private String classLog(String method ,String stackTrace){
        return "An exception accured in class %s mehtod %s \nStack Tracce:\n %s".formatted(method,MariaDB.class.getName() , stackTrace);
    }



}
