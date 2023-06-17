package com.oranim.telegrambot.InputExtractor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CompanyInputExtractorTest {

    @Test
    void getCompanyFromProductWithSingleWord() throws Exception {
        String input1 = "מוצר כלשהו 80 כללי פתק כלשהו";
        InputExtractor inputExtractor = new CompanyInputExtractor();

        assertThat(inputExtractor.getInput(input1)).isEqualTo("כללי");

    }

    @Test
    void getCompanyFromProductWithTwoWord() throws Exception {
        String input1 = "מוצר 80 כללי פתק כלשהו";
        InputExtractor inputExtractor = new CompanyInputExtractor();

        assertThat(inputExtractor.getInput(input1)).isEqualTo("כללי");

    }

    @Test
    void getCompanyFromProductWithThreeWord() throws Exception {
        String input1 = "מוצר שלוש מילים 80 כללי פתק כלשהו";
        InputExtractor inputExtractor = new CompanyInputExtractor();

        assertThat(inputExtractor.getInput(input1)).isEqualTo("כללי");

    }

    @Test
    void getCompanyFromProductWithFourWord() throws Exception {
        String input1 = "מוצר עם ארבע מילים 80 כללי פתק כלשהו";
        InputExtractor inputExtractor = new CompanyInputExtractor();

        assertThat(inputExtractor.getInput(input1)).isEqualTo("כללי");

    }
}