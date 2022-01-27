package com.TelegramBot.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OperationsTest {

    //simulating message sent
    String input = "alexa 350 amazon this is a note";
    String input2 = "computer 270 ksp this is second note yap";
    String input3 = "פנקו 70 כללי זה פתק רק בעברית";
    //
    Operations operations = new Operations();

    @Test
    void getPriceCheck(){
        //implementing the method
        String price = operations.getPrice(input);
        //testing
        assertThat(price).isEqualTo("350");
    }

    @Test
    void getPriceSecondCheck(){
        String price = operations.getPrice(input2);
        assertThat(price).isEqualTo("270");
    }
    @Test
    void getPriceThirdInput(){
        String price = operations.getPrice(input3);
        assertThat(price).isEqualTo("70");
    }

    @Test
    void getProductTest(){
        //implementing the method
        String product = operations.getProduct(input);
        //testing
        assertThat(product).isEqualTo("alexa");
    }
    @Test
    void getProductSecondTest(){
        String product = operations.getProduct(input2);
        assertThat(product).isEqualTo("computer");
    }
    @Test
    void getProductThirdTest(){
        String product = operations.getProduct(input3);
        assertThat(product).isEqualTo("פנקו");
    }

    @Test
    void getCompanyTest(){
        //implementing the method
        String company = operations.getCompany(input);
        //testing
        assertThat(company).isEqualTo("amazon");
    }

    @Test
    void getCompanySecondTest(){
        String company = operations.getCompany(input2);
        assertThat(company).isEqualTo("ksp");
    }
    @Test
    void getCompanyThirdTest(){
        String product = operations.getCompany(input3);
        assertThat(product).isEqualTo("כללי");
    }



    @Test
    void getNoteTest(){
        //implementing the method
        StringBuilder note = operations.getNote(input);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("this is a note");
    }
    @Test
    void getNoteSecondTest(){
        //implementing the method
        StringBuilder note = operations.getNote(input2);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("this is second note yap");
    }
    @Test
    void getNoteThirdTest(){
        //implementing the method
        StringBuilder note = operations.getNote(input3);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("זה פתק רק בעברית");
    }

    @Test
    void isNumericTest(){
        //implementing the method
        boolean numCheck = operations.isNumeric(operations.getPrice(input));
        //testing
        assertThat(numCheck).isTrue();
    }

    @Test
    void isNumericSecondTest(){
        boolean checkIfNum = operations.isNumeric(operations.getPrice(input2));
        assertThat(checkIfNum).isTrue();
    }
    @Test
    void isNotNumericCheck(){
        boolean shouldNotBeNumeric = operations.isNumeric(operations.getCompany(input));
        assertThat(shouldNotBeNumeric).isFalse();
    }

    @Test
    void isNumericThirdTest(){
        boolean shouldBeNumeric = operations.isNumeric(operations.getPrice(input3));
        assertThat(shouldBeNumeric).isTrue();
    }


}