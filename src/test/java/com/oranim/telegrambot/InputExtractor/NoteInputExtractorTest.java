package com.oranim.telegrambot.InputExtractor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NoteInputExtractorTest {


    @Test
    void getNoteWithTwoWordProduct() throws Exception {
        String input1 = "מוצר כלשהו 80 כללי פתק כלשהו";
        InputExtractor inputExtractor = new NoteInputExtractor();

        assertThat(inputExtractor.getInput(input1)).isEqualTo("פתק כלשהו");

    }

    @Test
    void getNoteWithSingleWordProduct() throws Exception {
        String input1 = "מוצר 80 כללי פתק כלשהו";
        InputExtractor inputExtractor = new NoteInputExtractor();

        assertThat(inputExtractor.getInput(input1)).isEqualTo("פתק כלשהו");

    }

    @Test
    void getNoteWithMultiWord() throws Exception {
        String input1 = "מוצר שלוש מילים 80 כללי יש לנו פתק כלשהו";
        InputExtractor inputExtractor = new NoteInputExtractor();

        assertThat(inputExtractor.getInput(input1)).isEqualTo("יש לנו פתק כלשהו");

    }

    @Test
    void getNoteWithSingleWord() throws Exception {
        String input1 = "מוצר עם ארבע מילים 80 כללי פתק";
        InputExtractor inputExtractor = new NoteInputExtractor();

        assertThat(inputExtractor.getInput(input1)).isEqualTo("פתק");

    }

}