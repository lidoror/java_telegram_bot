package com.TelegramBot.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FunctionsTest {

    //simulating message sent
    String input = "alexa 350 amazon this is a note";
    String input2 = "computer 270 ksp this is second note yap";
    String input3 = "פנקו 70 כללי זה פתק רק בעברית";
    //
    Functions functions = new Functions();

    @Test
    void getPriceCheck(){
        //implementing the method
        String price = functions.getPriceFromInput(input);
        //testing
        assertThat(price).isEqualTo("350");
    }

    @Test
    void getPriceSecondCheck(){
        String price = functions.getPriceFromInput(input2);
        assertThat(price).isEqualTo("270");
    }
    @Test
    void getPriceThirdInput(){
        String price = functions.getPriceFromInput(input3);
        assertThat(price).isEqualTo("70");
    }

    @Test
    void getProductTest(){
        //implementing the method
        String product = functions.getProductFromInput(input);
        //testing
        assertThat(product).isEqualTo("alexa");
    }
    @Test
    void getProductSecondTest(){
        String product = functions.getProductFromInput(input2);
        assertThat(product).isEqualTo("computer");
    }
    @Test
    void getProductThirdTest(){
        String product = functions.getProductFromInput(input3);
        assertThat(product).isEqualTo("פנקו");
    }

    @Test
    void getCompanyTest(){
        //implementing the method
        String company = functions.getCompanyFromInput(input);
        //testing
        assertThat(company).isEqualTo("amazon");
    }

    @Test
    void getCompanySecondTest(){
        String company = functions.getCompanyFromInput(input2);
        assertThat(company).isEqualTo("ksp");
    }
    @Test
    void getCompanyThirdTest(){
        String product = functions.getCompanyFromInput(input3);
        assertThat(product).isEqualTo("כללי");
    }



    @Test
    void getNoteTest(){
        //implementing the method
        StringBuilder note = functions.getNoteFromInput(input);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("this is a note");
    }
    @Test
    void getNoteSecondTest(){
        //implementing the method
        StringBuilder note = functions.getNoteFromInput(input2);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("this is second note yap");
    }
    @Test
    void getNoteThirdTest(){
        //implementing the method
        StringBuilder note = functions.getNoteFromInput(input3);
        //testing
        assertThat(String.valueOf(note)).isEqualTo("זה פתק רק בעברית");
    }

    @Test
    void isNumericTest(){
        //implementing the method
        boolean numCheck = functions.isNumeric(functions.getPriceFromInput(input));
        //testing
        assertThat(numCheck).isTrue();
    }

    @Test
    void isNumericSecondTest(){
        boolean checkIfNum = functions.isNumeric(functions.getPriceFromInput(input2));
        assertThat(checkIfNum).isTrue();
    }
    @Test
    void isNotNumericCheck(){
        boolean shouldNotBeNumeric = functions.isNumeric(functions.getCompanyFromInput(input));
        assertThat(shouldNotBeNumeric).isFalse();
    }

    @Test
    void isNumericThirdTest(){
        boolean shouldBeNumeric = functions.isNumeric(functions.getPriceFromInput(input3));
        assertThat(shouldBeNumeric).isTrue();
    }


}