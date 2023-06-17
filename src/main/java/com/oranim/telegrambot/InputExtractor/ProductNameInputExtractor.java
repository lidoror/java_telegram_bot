package com.oranim.telegrambot.InputExtractor;

import com.oranim.telegrambot.Exception.UnableToGeneratePriceException;

import java.util.regex.Pattern;

public class ProductNameInputExtractor extends InputExtractor{

    private final Pattern PATTERN = Pattern.compile("\\d+");


    @Override
    public Pattern getPattern() {
        return PATTERN;
    }

    @Override
    public String getInput(String input) throws UnableToGeneratePriceException {
        String splitter = findPattern(input);

        String product =  input.split(splitter)[0];

        if (splitter.equals("-1") || product.equals(input)){
            throw new UnableToGeneratePriceException("input -1 regex input was not found");
        }

        return product.trim();
    }


}
