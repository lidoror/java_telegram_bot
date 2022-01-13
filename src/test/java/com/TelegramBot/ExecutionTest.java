package com.TelegramBot;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExecutionTest {
    //simulating message sent
    String input = "alexa 350 amazon this is a note";
    //
    Execution exeTest = new Execution();

    @Test
    void getPriceCheck(){
        //implementing the method
        String price = exeTest.getPrice(input);
        //testing
        assertThat(price).isEqualTo("350");
    }

    @Test
    void getProductTest(){
        //implementing the method
        String product = exeTest.getProduct(input);
        //testing
        assertThat(product).isEqualTo("alexa");
    }

    @Test
    void getCompanyTest(){
        //implementing the method
        String company = exeTest.getCompany(input);
        //testing
        assertThat(company).isEqualTo("amazon");
    }

    @Test
    void getNoteTest(){
        //implementing the method
        StringBuilder note = exeTest.getNote(input);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("this is a note");
    }

    @Test
    void isNumericTest(){
        //implementing the method
        boolean numCheck = exeTest.isNumeric(exeTest.getPrice(input));
        //testing
        assertThat(numCheck).isTrue();
    }



}
