package com.oranim.telegrambot.balanceMgmt;

import com.oranim.telegrambot.utils.Const;
import com.oranim.telegrambot.FileHandelers.JsonWorkloads;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class Salary {




    public void salaryInitializationFromInput(SendMessage message, String command)  {
        JsonWorkloads jsonWorkloads = new JsonWorkloads();

        if (command.contains("one")) {
            String firstSalary = command.split(Const.SINGLE_SPACE_SEPARATOR)[2];
            jsonWorkloads.jsonWriter("First_Salary" , firstSalary , "./data/salary.json");
            message.setText("First salary initialized");

        } else if (command.contains("two")) {
            String secondSalary = command.split(Const.SINGLE_SPACE_SEPARATOR)[2];
            jsonWorkloads.jsonWriter("Second_Salary" , secondSalary , "./data/salary.json");
            message.setText("Second salary initialized");

        } else
            message.setText("Salary initialization failed");
    }

    public double getSalaryFromJson(String salary) {
        JsonWorkloads jsonWorkloads = new JsonWorkloads();
        String salaryToReturn = jsonWorkloads.jsonReader("./data/salary.json").get(salary).toString();
        return Double.parseDouble(salaryToReturn);

    }

    public double getSalarySum(){
        double firstSalary = getSalaryFromJson("First_Salary");
        double secondSalary = getSalaryFromJson("Second_Salary");
        double salary = firstSalary + secondSalary;
        return salary;
    }
}
