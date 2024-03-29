package com.oranim.telegrambot.dao;

import com.oranim.telegrambot.models.ExpensesModel;
import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.LogWarningLevel;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpensesDao implements IExpensesDao {

    private final String dbUser = System.getenv("DB_User");
    private final String dbPassword = System.getenv("DB_Pass");
    private final String dbConnection = System.getenv("DB_CONNECTION");
    private final String dbAddress = System.getenv("DB_ADDRESS");
    private final String database = System.getenv("DATABASE");
    private final String dbPort = System.getenv("DB_PORT");




    public ExpensesDao(){}

    /**
     * this function takes all the env vars and concatenate them into one connection string
     * @return connection string
     */
    private String getDbConnectionAddress(){
        return String.format(
                "jdbc:%s://%s:%s/%s?user=%s&password=%s&serverTimezone=UTC"
                ,dbConnection,dbAddress,dbPort,database,dbUser,dbPassword
        );
    }

    /**
     * This method establish connection with the mariadb databases
     * @return the object of the connection
     */
    private Connection getDbConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(getDbConnectionAddress());

        } catch (SQLException sqlException) {
            BotLogging.createLog(LogWarningLevel.CRITICAL, ExpensesDao.class.getName(),"getDBConnection", Arrays.toString(sqlException.getStackTrace()));
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
            BotLogging.createLog(LogWarningLevel.CRITICAL,ExpensesDao.class.getName(),"updateDB", Arrays.toString(sqlException.getStackTrace()));
        }
    }

    /**
     * takes list of shopping mgmt and adds the records from the db to it
     *
     * @param records   is the list we want to add the records from the db
     * @param resultSet is the value we got from the db after the request
     * @throws SQLException if the function wasn't able to get the date from the resultSet
     */
    private void returnRecordsAsShoppingMGMT(List<ExpensesModel> records, ResultSet resultSet) throws SQLException {

        String product = resultSet.getString(1);
        String price = resultSet.getString(2);
        String company = resultSet.getString(3);
        String note = resultSet.getString(4);
        String purchaseDate = resultSet.getString(5);
        int index_value = resultSet.getInt(6);

        records.add(
                new ExpensesModel(
                        product,price,company,note,purchaseDate,index_value
                )
        );
    }


    /**
     * this function gets two dates and makes a call to the database to get the records between them
     * @param startDate is the date we wat to start the filter
     * @param endDate is the date we wat to end the filter
     * @return list of records between startDate and endDate
     */
    public List<ExpensesModel> getItemsFromDbBetweenDates(String startDate, String endDate){
        List<ExpensesModel> records = new ArrayList<>();
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
            BotLogging.createLog(LogWarningLevel.CRITICAL,ExpensesDao.class.getName(),"getItemsFromDbBetweenDates", Arrays.toString(sqlException.getStackTrace()));
        }
        return records;
    }



    /**
     * create call that returns all data in database
     * @return list of the loaded data from the DB
     */
    @Override
    public List<ExpensesModel> getAllRecordsFromDb()  {
        List<ExpensesModel> records = new ArrayList<>();
        try(Connection conn = getDbConnection()){
            CallableStatement callableStatement = conn.prepareCall("{ call getAllDbRecords() }");
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                returnRecordsAsShoppingMGMT(records, resultSet);
            }

        }catch (SQLException sqlException){
            BotLogging.createLog(LogWarningLevel.CRITICAL,ExpensesDao.class.getName(),"dbRecordToList", Arrays.toString(sqlException.getStackTrace()));
        }
        return records;
    }



    /**
     * check the connection to the DB
     * @return if the connection is valid or not
     * @throws SQLException if we fail to connect
     */
    @Override
    public boolean checkConnection() throws SQLException{
        Connection connection =
                DriverManager.getConnection(getDbConnectionAddress());
        boolean isValid = connection.isValid(2);
        connection.close();
        return isValid;
    }


    /**
     * load the data generated to the list into a map
     * @return map of data generated from db
     * @throws SQLException if we fail to get the data from the database
     */
    public Map<Integer, ExpensesModel> dbListToMap()throws SQLException{
        Map<Integer, ExpensesModel> dbRecordMap = new HashMap<>();
        for (var record : getAllRecordsFromDb()) {
            dbRecordMap.put(record.columID(),record);
        }
        return dbRecordMap;
    }


}
