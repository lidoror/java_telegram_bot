package com.oranim.telegrambot.services;

import com.oranim.telegrambot.bot.Dispatcher;
import com.oranim.telegrambot.dao.ExpensesDao;
import com.oranim.telegrambot.dao.IExpensesDao;
import com.oranim.telegrambot.exception.NoConnectionToDbException;
import com.oranim.telegrambot.exception.UnableToGeneratePriceException;
import com.oranim.telegrambot.inputExtractor.CompanyInputExtractor;
import com.oranim.telegrambot.inputExtractor.NoteInputExtractor;
import com.oranim.telegrambot.inputExtractor.PriceInputExtractor;
import com.oranim.telegrambot.inputExtractor.ProductNameInputExtractor;
import com.oranim.telegrambot.managers.BalanceManager;
import com.oranim.telegrambot.models.ExpensesModel;
import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.DateHandler;
import com.oranim.telegrambot.utils.LogWarningLevel;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExpensesService {


    private final IExpensesDao expensesDao;
    private final DateHandler dateHandler;

    public ExpensesService(){
        expensesDao = new ExpensesDao();
        dateHandler = new DateHandler();
    }

    /**
     * this function return records between two dates
     * @return records from database between two dates
     * @throws SQLException after trying to get data from database
     */
    public List<ExpensesModel> getExpensesByDates(String month , String year) throws SQLException {
        String startDate = "%s-%s-01".formatted(year,month);
        String endDate = "%s-%s-31".formatted(year,month);
        return Optional.of(new ArrayList<>(expensesDao.getItemsFromDbBetweenDates(startDate, endDate))).orElseThrow(() -> new SQLException("Exception in getMonthByExpenses"));
    }


    /**
     * this function calculate the sum of money spent between two dates
     * @param month the month we calc
     * @param year the year we calc
     * @return thew sum of money spent between two dates as a string
     * @throws SQLException after trying to get data from database
     */
    public Double sumMoneySpentInCertainMonth(String month , String year) throws SQLException {
        return Optional.of(
                getExpensesByDates(month,year)
                        .stream()
                        .map(ExpensesModel::price)
                        .mapToDouble(Double::parseDouble)
                        .sum())
                .orElseThrow(()-> new SQLException("Exception in getTotalSpending"));
    }

    public String sumMoneySpentCurrentMonth(){
        try {
            return sumMoneySpentInCertainMonth(
                    dateHandler.getCurrentMonth(), dateHandler.getCurrentMonth()).toString();
        }catch (SQLException e){
            BotLogging.createLog(LogWarningLevel.WARN, Dispatcher.class.getName(),"sumMoneySpentCurrentMonth",Arrays.toString(e.getStackTrace()));
        }
        return "-1";
    }

    /**
     * filter the data by the category we sent to it
     * @param category to filter by
     * @return list of ExpensesModel records filtered by the category its getting
     * @throws SQLException after trying to get data from database
     */
    private List<ExpensesModel> getMonthlyCategoryAsList(String category)throws SQLException{
        return Optional.of(getExpensesByDates(dateHandler.getCurrentMonth(),dateHandler.getCurrentYear()).stream()
                .filter(filter -> filter.category().equals(category))
                .collect(Collectors.toList())).orElseThrow(()-> new SQLException("Exception in getMonthlyCategoryAsList"));
    }

    /**
     * use the category and return it as list
     * @param category to filter by
     * @return records of ExpensesModel filtered by category
     * @throws SQLException after trying to get data from database
     */
    public List<ExpensesModel> getMonthlyCategoryRecord(String category)throws SQLException{
        return getMonthlyCategoryAsList(category);
    }

    /**
     * use the category to filter the data and sum all the price value after the filter
     * @param category to filter by
     * @return the sum of all the price value
     * @throws SQLException after trying to get data from database
     */
    public String getCategoryMonthlySpent(String category) throws SQLException{
        return String.valueOf(getMonthlyCategoryAsList(category).stream().map(ExpensesModel::price).mapToDouble(Double::parseDouble).sum());
    }

    /**
     * takes integer as the key and returns string implementation of the record
     * @param mapKey is the key to get from the map will be the index value in the db
     * @return string implementation of the record
     * @throws SQLException after trying to get data from database
     */
    public String dbRecordsToMap(int mapKey) throws SQLException {
        return String.valueOf(expensesDao.dbListToMap().get(mapKey));
    }

    /**
     * checks connection to the db
     * @return string that contain message of db connection status
     * @throws NoConnectionToDbException after checking connection to db and failed
     */
    public String dbStatusCheckInline() throws SQLException {
        if (expensesDao.checkConnection()) {
            return "Connection to Database achieved";
        } else
            throw new NoConnectionToDbException("Cannot connect to Database");
    }

    /**
     * generate from the user input data in the order needed for the DB
     * @param command the user from the input
     */
    public void setDbParameter(String command){
        try {
            expensesDao.insertDataToDB(
                    new ProductNameInputExtractor().getInput(command),
                    new PriceInputExtractor().getInput(command),
                    new CompanyInputExtractor().getInput(command),
                    new NoteInputExtractor().getInput(command)
            );
        }catch (UnableToGeneratePriceException e){
            BotLogging.createLog(LogWarningLevel.CRITICAL,ExpensesDao.class.getName(),"setDbParameter", Arrays.toString(e.getStackTrace()));
        }

    }

    /**
     *
     * @param command the command sent from the user
     * @param message the object message sent to the user with a message
     * @throws SQLException after trying to access the database
     */
    public void inputInsertionAndValidation(String command, SendMessage message) throws SQLException {
        BalanceManager balanceManager = new BalanceManager();

        int sizeBeforeDataInsertion = expensesDao.getAllRecordsFromDb().size();
        setDbParameter(command);
        int sizeAfterDataInsertion = expensesDao.getAllRecordsFromDb().size();
        boolean dbRecordUpdated = sizeAfterDataInsertion == sizeBeforeDataInsertion + 1;

        if (dbRecordUpdated) {
            balanceManager.addToBalance(new PriceInputExtractor().getInput(command));
            message.setText("Data added to bot.");
        } else
            message.setText("A problem occurred in data insertion");
    }




}
