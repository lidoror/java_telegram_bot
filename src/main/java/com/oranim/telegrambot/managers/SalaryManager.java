package com.oranim.telegrambot.managers;

import com.oranim.telegrambot.filesManager.JsonWorkloads;
import com.oranim.telegrambot.utils.Const;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

public class SalaryManager {


    private void addSalary(String key, String value) {
        Map<String,String> salaryData = new HashMap<>();
        salaryData.put(key,value);

        JsonWorkloads jsonWorkloads = new JsonWorkloads();
        jsonWorkloads.writeJson(salaryData, "salary.json");
    }

    public String getSalary(String fileName,String key) {
        JsonWorkloads jsonWorkloads = new JsonWorkloads();
        return (String) jsonWorkloads.getJson(fileName).get(key);
    }

    public void generateSalaryFromInput(SendMessage message, String command)  {
        String salary = command.split(Const.SINGLE_SPACE_SEPARATOR)[2];
        String salaryNumber = command.split(Const.SINGLE_SPACE_SEPARATOR)[1];
        String salaryName = "salary_%s".formatted(salaryNumber).toUpperCase();
        addSalary(salaryName,salary);
        message.setText("salary %s initialized".formatted(salaryNumber));

//            message.setText("Salary initialization failed");
    }

    public Map<String ,String > getAllSalaries(){
        JsonWorkloads jsonWorkloads = new JsonWorkloads();
        return jsonWorkloads.getJson("salary.json");
    }

    public double getSalarySum(){
        return getAllSalaries().values().stream().mapToDouble(Double::parseDouble).sum();
    }

}
