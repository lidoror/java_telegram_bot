package com.oranim.telegrambot.inputExtractor;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProductNameInputExtractorTest {

    @Test
    void getProductWithSingleWord() throws Exception {
        String input1 = "מוצר כלשהו 80 כללי פתק כלשהו";
        InputExtractor productNameInputExtractor = new ProductNameInputExtractor();

        assertThat(productNameInputExtractor.getInput(input1)).isEqualTo("מוצר כלשהו");

    }

    @Test
    void getProductWithTwoWord() throws Exception {
        String input1 = "מוצר 80 כללי פתק כלשהו";
        InputExtractor productNameInputExtractor = new ProductNameInputExtractor();

        assertThat(productNameInputExtractor.getInput(input1)).isEqualTo("מוצר");

    }

    @Test
    void getProductWithThreeWord() throws Exception {
        String input1 = "מוצר שלוש מילים 80 כללי פתק כלשהו";
        InputExtractor productNameInputExtractor = new ProductNameInputExtractor();

        assertThat(productNameInputExtractor.getInput(input1)).isEqualTo("מוצר שלוש מילים");

    }

    @Test
    void getProductWithFourWord() throws Exception {
        String input1 = "מוצר עם ארבע מילים 80 כללי פתק כלשהו";
        InputExtractor productNameInputExtractor = new ProductNameInputExtractor();

        assertThat(productNameInputExtractor.getInput(input1)).isEqualTo("מוצר עם ארבע מילים");

    }


}