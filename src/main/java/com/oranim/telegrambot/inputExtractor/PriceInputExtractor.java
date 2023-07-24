package com.oranim.telegrambot.inputExtractor;


import com.oranim.telegrambot.utils.Const;

import java.util.regex.Pattern;

public class PriceInputExtractor extends InputExtractor{

    private final Pattern PATTERN = Pattern.compile(Const.WHOLE_AND_DECIMAL_NUMBER_REGEX);


    @Override
    public Pattern getPattern() {
        return PATTERN;
    }

    @Override
    public String getInput(String input) {
        String pattern = findPattern(input);
        return pattern;
    }


}
