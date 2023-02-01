package com.oranim.telegrambot.db;

import com.oranim.telegrambot.Exception.UnableToGeneratePriceException;
import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.FunctionsUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class MariaDB implements IDatabase {

    private final String dbConnectionString = System.getenv("MARIADB_URL");



    public MariaDB(){}

    /**
     * This method establish connection with the mariadb databases
     * @return the object of the connection
     */
    private Connection getDbConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbConnectionString);

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

    /**
     * takes list of shopping mgmt and adds the records from the db to it
     * @param records is the list we want to add the records from the db
     * @param resultSet is the value we got from the db after the request
     * @return list of shopping mgmt records
     * @throws SQLException if the function wasn't able to get the date from the resultSet
     */
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


    /**
     * this function gets two dates and makes a call to the database to get the records between them
     * @param startDate is the date we wat to start the filter
     * @param endDate is the date we wat to end the filter
     * @return list of records between startDate and endDate
     */
    public List<ShoppingMgmtRecord> getItemsFromDbBetweenDates(String startDate, String endDate){
        List<ShoppingMgmtRecord> records = new ArrayList<>();
        try(Connection conn = getDbConnection()){
            CallableStatement callableStatement = conn.prepareCall("{ call getRecordsByDate(? ,?) }");
            callableStatement.setString(1,startDate);
            callableStatement.setString(2,endDate);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()){
                returnRecordsAsShoppingMGMT(records, resultSet);
            }
            resultSet.close();
            callableStatement.close();

        }catch (SQLException sqlException){
            BotLogging.setInfoLog(classLog("tried to get records in getItemsFromDbBetweenDates but failed", Arrays.toString(sqlException.getStackTrace())));
        }
        return records;
    }



    /**
     * create call that returns all data in database
     * @return list of the loaded data from the DB
     * @throws SQLException if we encountered a problem accessing the database
     */
    @Override
    public List<ShoppingMgmtRecord> getAllRecordsFromDb() throws SQLException {
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
    public void setDbParameter(String command){
        try {
            insertDataToDB(
                    FunctionsUtils.generateProductFromInput(command),
                    FunctionsUtils.generateProductCostFromInput(command),
                    FunctionsUtils.generateProductCompanyFromInput(command),
                    FunctionsUtils.generateProductNoteFromInput(command)
            );
        }catch (UnableToGeneratePriceException exception){
            BotLogging.setInfoLog(classLog("setDbParameter", Arrays.toString(exception.getStackTrace())));
        }

    }

    /**
     * check the connection to the DB
     * @return if the connection is valid or not
     * @throws SQLException if we fail to connect
     */
    @Override
    public boolean checkConnection() throws SQLException{
        Connection connection =
                DriverManager.getConnection(dbConnectionString);
        boolean isValid = connection.isValid(2);
        connection.close();
        return isValid;
    }


    /**
     * load the data generated to the list into a map
     * @return map of data generated from db
     * @throws SQLException if we fail to get the data from the database
     */
    public Map<Integer,ShoppingMgmtRecord> dbListToMap()throws SQLException{
        Map<Integer,ShoppingMgmtRecord> dbRecordMap = new HashMap<>();
        for (var record : getAllRecordsFromDb()) {
            dbRecordMap.put(record.columID(),record);
        }
        return dbRecordMap;
    }

    private String classLog(String method ,String stackTrace){
        return "An exception occurred in class %s method %s \nStack Trace:\n %s".formatted(method,MariaDB.class.getName() , stackTrace);
    }



}
