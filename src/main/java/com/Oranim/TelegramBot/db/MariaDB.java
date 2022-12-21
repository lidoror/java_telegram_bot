package com.Oranim.TelegramBot.db;

import com.Oranim.TelegramBot.utils.BotLogging;
import com.Oranim.TelegramBot.utils.FunctionsUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.*;

public class MariaDB implements IDatabase {

    private final String rootConnection = System.getenv("MARIADB_URL");
    private final String selectAllShopping = "Select * From shopping";


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
            String stackTrace = Arrays.toString(sqlException.getStackTrace());
            BotLogging.setCriticalLog(classLog("getDBConnection", stackTrace));
        }
        return connection;
    }


    /**
     * this method gets 4 arguments from the user and add 2 generated arguments update them in the database , this is the wat we update data in the DB
     * @param product the product the user send
     * @param price the price the user send
     * @param company the company the user send
     * @param note the note the user send
     */
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
            String stackTrace = Arrays.toString(sqlException.getStackTrace());
            BotLogging.setCriticalLog(classLog("updateDB", stackTrace));

        }
    }

    /**
     * this methon search thow the db and generate new index value in DB
     * @return new insex value for the inserted data
     * @throws SQLException
     */
    private int setNewIndexValue() throws SQLException{
        List<Integer> columID = dbRecordToList().stream().map(ShoppingMgmtRecord::columID).toList();
        return columID.get(columID.size() - 1)+1;
    }

    /**
     * this method pull all the data in the db and load to shooping menegemnt class
     * @param records list the data generated into
     * @param conn the connection we use to acces the data
     * @throws SQLException
     */
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

    /**
     * transfer the loaded data to a list
     * @return list of the loaded data from the DB
     * @throws SQLException
     */
    @Override
    public List<ShoppingMgmtRecord> dbRecordToList() throws SQLException {
        List<ShoppingMgmtRecord> records = new ArrayList<>();
        try(Connection conn = getDbConnection()){
            getDbDataToShoppingMgmt(records, conn);

        }catch (SQLException sqlException){
            String stackTrace = Arrays.toString(sqlException.getStackTrace());
            BotLogging.setCriticalLog(classLog("dbRecordToList", stackTrace));
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
        updateDB(FunctionsUtils.generateProductFromInput(command),
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
        for (var record:dbRecordToList()) {
            dbRecordMap.put(record.columID(),record);
        }
        return dbRecordMap;
    }

    private String classLog(String method ,String stackTrace){
        return "An exception accured in class %s mehtod %s \nStack Tracce:\n %s".formatted(method,MariaDB.class.getName() , stackTrace);
    }



}
