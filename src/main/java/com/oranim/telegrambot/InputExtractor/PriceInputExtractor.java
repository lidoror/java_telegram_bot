package com.oranim.telegrambot.InputExtractor;


import com.oranim.telegrambot.Exception.InputExtractionException;

import java.util.regex.Pattern;

public class PriceInputExtractor extends InputExtractor{

    private final Pattern PATTERN = Pattern.compile("\\d+");


    @Override
    public Pattern getPattern() {
        return PATTERN;
    }

    @Override
    public String getInput(String input) {
        return findPattern(input);
    }


}
