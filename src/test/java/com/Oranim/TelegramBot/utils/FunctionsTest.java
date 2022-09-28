package com.Oranim.TelegramBot.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FunctionsTest {

    //simulating message sent
    String input = "alexa 350 amazon this is a note";
    String input2 = "computer 270 ksp this is second note yap";
    String input3 = "פנקו 70 כללי זה פתק רק בעברית";
    //


    @Test
    void getPriceCheck(){
        //implementing the method
        String price = FunctionsUtils.generateProductCostFromInput(input);
        //testing
        assertThat(price).isEqualTo("350");
    }

    @Test
    void getPriceSecondCheck(){
        String price = FunctionsUtils.generateProductCostFromInput(input2);
        assertThat(price).isEqualTo("270");
    }
    @Test
    void getPriceThirdInput(){
        String price = FunctionsUtils.generateProductCostFromInput(input3);
        assertThat(price).isEqualTo("70");
    }

    @Test
    void getProductTest(){
        //implementing the method
        String product = FunctionsUtils.generateProductFromInput(input);
        //testing
        assertThat(product).isEqualTo("alexa");
    }
    @Test
    void getProductSecondTest(){
        String product = FunctionsUtils.generateProductFromInput(input2);
        assertThat(product).isEqualTo("computer");
    }
    @Test
    void getProductThirdTest(){
        String product = FunctionsUtils.generateProductFromInput(input3);
        assertThat(product).isEqualTo("פנקו");
    }

    @Test
    void getCompanyTest(){
        //implementing the method
        String company = FunctionsUtils.generateProductCompanyFromInput(input);
        //testing
        assertThat(company).isEqualTo("amazon");
    }

    @Test
    void getCompanySecondTest(){
        String company = FunctionsUtils.generateProductCompanyFromInput(input2);
        assertThat(company).isEqualTo("ksp");
    }
    @Test
    void getCompanyThirdTest(){
        String product = FunctionsUtils.generateProductCompanyFromInput(input3);
        assertThat(product).isEqualTo("כללי");
    }



    @Test
    void getNoteTest(){
        //implementing the method
        StringBuilder note = FunctionsUtils.generateProductNoteFromInput(input);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("this is a note");
    }
    @Test
    void getNoteSecondTest(){
        //implementing the method
        StringBuilder note = FunctionsUtils.generateProductNoteFromInput(input2);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("this is second note yap");
    }
    @Test
    void getNoteThirdTest(){
        //implementing the method
        StringBuilder note = FunctionsUtils.generateProductNoteFromInput(input3);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("זה פתק רק בעברית");
    }

    @Test
    void isNumericTest(){
        //implementing the method
        boolean numCheck = FunctionsUtils.stringContainNumber(FunctionsUtils.generateProductCostFromInput(input));
        //testing
        assertThat(numCheck).isTrue();
    }

    @Test
    void isNumericSecondTest(){
        boolean checkIfNum = FunctionsUtils.stringContainNumber(FunctionsUtils.generateProductCostFromInput(input2));
        assertThat(checkIfNum).isTrue();
    }
    @Test
    void isNotNumericCheck(){
        boolean shouldNotBeNumeric = FunctionsUtils.stringContainNumber(FunctionsUtils.generateProductCompanyFromInput(input));
        assertThat(shouldNotBeNumeric).isFalse();
    }

    @Test
    void isNumericThirdTest(){
        boolean shouldBeNumeric = FunctionsUtils.stringContainNumber(FunctionsUtils.generateProductCostFromInput(input3));
        assertThat(shouldBeNumeric).isTrue();
    }


}