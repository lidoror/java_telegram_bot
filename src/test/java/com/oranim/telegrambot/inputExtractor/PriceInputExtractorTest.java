package com.oranim.telegrambot.inputExtractor;

import com.oranim.telegrambot.exception.RegexPatternNotFound;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PriceInputExtractorTest {

    @Test
    void canGetPriceFromProductWithTwoWord() throws RegexPatternNotFound {
        String input1 = "מוצר כלשהו 80 כללי פתק כלשהו";
        InputExtractor InputExtractor = new PriceInputExtractor();
        assertThat(InputExtractor.getInput(input1)).isEqualTo("80");

    }

    @Test
    void canGetPriceFromProductWithSingleWord() throws RegexPatternNotFound {
        String input1 = "מוצר 80 כללי פתק כלשהו";
        InputExtractor InputExtractor = new PriceInputExtractor();

        assertThat(InputExtractor.getInput(input1)).isEqualTo("80");

    }

    @Test
    void canGetPriceFromProductWithThreeWord() throws RegexPatternNotFound {
        String input1 = "מוצר כלשהו אחר 80 כללי פתק כלשהו";
        InputExtractor InputExtractor = new PriceInputExtractor();
        assertThat(InputExtractor.getInput(input1)).isEqualTo("80");

    }


    @Test
    void canGetPriceFromProductWithNegativePrice() throws RegexPatternNotFound {
        String input1 = "מוצר כלשהו אחר -80 כללי פתק כלשהו";
        InputExtractor InputExtractor = new PriceInputExtractor();
        assertThat(InputExtractor.getInput(input1)).isEqualTo("-80");

    }

    @Test
    void canGetPriceFromProductWithBigNegativePrice() throws RegexPatternNotFound {
        String input1 = "מוצר כלשהו אחר -500 כללי פתק כלשהו";
        InputExtractor InputExtractor = new PriceInputExtractor();
        assertThat(InputExtractor.getInput(input1)).isEqualTo("-500");

    }

    @Test
    void canGetPriceFromProductWithDecimalPrice() throws RegexPatternNotFound {
        String input1 = "מוצר כלשהו אחר 0.5 כללי פתק כלשהו";
        InputExtractor InputExtractor = new PriceInputExtractor();
        assertThat(InputExtractor.getInput(input1)).isEqualTo("0.5");

    }

    @Test
    void canGetPriceFromProductWithNegativeDecimalPrice() throws RegexPatternNotFound {
        String input1 = "מוצר כלשהו אחר -0.5 כללי פתק כלשהו";
        InputExtractor InputExtractor = new PriceInputExtractor();
        assertThat(InputExtractor.getInput(input1)).isEqualTo("-0.5");

    }

    @Test
    void canGetPriceFromProductWithBigNegativeDecimalPrice() throws RegexPatternNotFound {
        String input1 = "מוצר כלשהו אחר -100.5 כללי פתק כלשהו";
        InputExtractor InputExtractor = new PriceInputExtractor();
        assertThat(InputExtractor.getInput(input1)).isEqualTo("-100.5");

    }

}