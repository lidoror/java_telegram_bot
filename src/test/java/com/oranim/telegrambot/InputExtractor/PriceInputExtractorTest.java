package com.oranim.telegrambot.InputExtractor;

import com.oranim.telegrambot.Exception.InputExtractionException;
import com.oranim.telegrambot.Exception.UnableToGeneratePriceException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PriceInputExtractorTest {

    @Test
    void getPriceFromProductWithTwoWord() throws InputExtractionException {
        String input1 = "מוצר כלשהו 80 כללי פתק כלשהו";
        InputExtractor InputExtractor = new PriceInputExtractor();
        assertThat(InputExtractor.getInput(input1)).isEqualTo("80");

    }

    @Test
    void getPriceFromProductWithSingleWord() throws InputExtractionException {
        String input1 = "מוצר 80 כללי פתק כלשהו";
        InputExtractor InputExtractor = new PriceInputExtractor();

        assertThat(InputExtractor.getInput(input1)).isEqualTo("80");

    }

    @Test
    void getPriceFromProductWithThreeWord() throws InputExtractionException {
        String input1 = "מוצר כלשהו אחר 80 כללי פתק כלשהו";
        InputExtractor InputExtractor = new PriceInputExtractor();
        assertThat(InputExtractor.getInput(input1)).isEqualTo("80");

    }

}