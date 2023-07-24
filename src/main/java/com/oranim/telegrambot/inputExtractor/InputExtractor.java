package com.oranim.telegrambot.inputExtractor;


import com.oranim.telegrambot.exception.RegexPatternNotFound;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class InputExtractor {

    public abstract Pattern getPattern();


    public abstract String getInput(String input) throws RegexPatternNotFound;

    protected String findPattern(String input)  {
        Matcher matcher = getPattern().matcher(input);

        if (matcher.find()){
            return matcher.group();
        }

        return "-1";

    }


}
