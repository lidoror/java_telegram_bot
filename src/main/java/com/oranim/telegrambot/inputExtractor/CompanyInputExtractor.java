package com.oranim.telegrambot.inputExtractor;

import com.oranim.telegrambot.exception.UnableToGeneratePriceException;
import com.oranim.telegrambot.utils.Const;

import java.util.regex.Pattern;

public class CompanyInputExtractor extends InputExtractor{
    private final Pattern PATTERN = Pattern.compile(Const.WHOLE_AND_DECIMAL_NUMBER_REGEX);


    @Override
    public Pattern getPattern() {
        return PATTERN;
    }

    @Override
    public String getInput(String input) throws UnableToGeneratePriceException {

        String splitter = findPattern(input);

        if (splitter.equals("-1")){
            throw new UnableToGeneratePriceException("error thrown from CompanyInputExtractor");
        }

        String stringWithCompany = input.split(splitter)[1].trim();
        String company = stringWithCompany.split(Const.SINGLE_SPACE_SEPARATOR)[0];

        return company;
    }
}
