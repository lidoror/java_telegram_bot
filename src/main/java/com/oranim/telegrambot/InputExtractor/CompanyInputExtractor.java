package com.oranim.telegrambot.InputExtractor;

import com.oranim.telegrambot.Exception.UnableToGeneratePriceException;
import com.oranim.telegrambot.utils.Const;

import java.util.regex.Pattern;

public class CompanyInputExtractor extends InputExtractor{
    private final Pattern PATTERN = Pattern.compile("\\d+");


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
