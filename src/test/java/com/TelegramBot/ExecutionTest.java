package com.TelegramBot;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExecutionTest {
    //simulating message sent
    String input = "alexa 350 amazon this is a note";
    String input2 = "computer 270 ksp this is second note yap";
    String input3 = "פנקו 70 כללי זה פתק רק בעברית";
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
    void getPriceSecondCheck(){
        String price = exeTest.getPrice(input2);
        assertThat(price).isEqualTo("270");
    }
    @Test
    void getPriceThirdInput(){
        String price = exeTest.getPrice(input3);
        assertThat(price).isEqualTo("70");
    }

    @Test
    void getProductTest(){
        //implementing the method
        String product = exeTest.getProduct(input);
        //testing
        assertThat(product).isEqualTo("alexa");
    }
    @Test
    void getProductSecondTest(){
        String product = exeTest.getProduct(input2);
        assertThat(product).isEqualTo("computer");
    }
    @Test
    void getProductThirdTest(){
        String product = exeTest.getProduct(input3);
        assertThat(product).isEqualTo("פנקו");
    }

    @Test
    void getCompanyTest(){
        //implementing the method
        String company = exeTest.getCompany(input);
        //testing
        assertThat(company).isEqualTo("amazon");
    }

    @Test
    void getCompanySecondTest(){
        String company = exeTest.getCompany(input2);
        assertThat(company).isEqualTo("ksp");
    }
    @Test
    void getCompanyThirdTest(){
        String product = exeTest.getCompany(input3);
        assertThat(product).isEqualTo("כללי");
    }



    @Test
    void getNoteTest(){
        //implementing the method
        StringBuilder note = exeTest.getNote(input);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("this is a note");
    }
    @Test
    void getNoteSecondTest(){
        //implementing the method
        StringBuilder note = exeTest.getNote(input2);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("this is second note yap");
    }
    @Test
    void getNoteThirdTest(){
        //implementing the method
        StringBuilder note = exeTest.getNote(input3);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("זה פתק רק בעברית");
    }

    @Test
    void isNumericTest(){
        //implementing the method
        boolean numCheck = exeTest.isNumeric(exeTest.getPrice(input));
        //testing
        assertThat(numCheck).isTrue();
    }

    @Test
    void isNumericSecondTest(){
        boolean checkIfNum = exeTest.isNumeric(exeTest.getPrice(input2));
        assertThat(checkIfNum).isTrue();
    }
    @Test
    void isNotNumericCheck(){
        boolean shouldNotBeNumeric = exeTest.isNumeric(exeTest.getCompany(input));
        assertThat(shouldNotBeNumeric).isFalse();
    }

    @Test
    void isNumericThirdTest(){
        boolean shouldBeNumeric = exeTest.isNumeric(exeTest.getPrice(input3));
        assertThat(shouldBeNumeric).isTrue();
    }



}
