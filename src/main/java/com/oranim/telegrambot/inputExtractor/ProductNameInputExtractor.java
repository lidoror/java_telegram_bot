package com.oranim.telegrambot.inputExtractor;

import com.oranim.telegrambot.exception.UnableToGeneratePriceException;
import com.oranim.telegrambot.utils.Const;

import java.util.regex.Pattern;

public class ProductNameInputExtractor extends InputExtractor{

    private final Pattern PATTERN = Pattern.compile(Const.WHOLE_AND_DECIMAL_NUMBER_REGEX);


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
