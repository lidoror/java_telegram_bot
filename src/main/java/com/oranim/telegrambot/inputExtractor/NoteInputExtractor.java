package com.oranim.telegrambot.inputExtractor;

import com.oranim.telegrambot.exception.UnableToGeneratePriceException;
import com.oranim.telegrambot.utils.Const;
import java.util.regex.Pattern;

public class NoteInputExtractor extends InputExtractor{
    private final Pattern PATTERN = Pattern.compile(Const.WHOLE_AND_DECIMAL_NUMBER_REGEX);


    @Override
    public Pattern getPattern() {
        return PATTERN;
    }

    @Override
    public String getInput(String input) throws UnableToGeneratePriceException {
        String splitter = findPattern(input);

        if (splitter.equals("-1")){
            throw new UnableToGeneratePriceException("Unable to generate Note");
        }
        String dataToFindNote = input.split(splitter)[1];

        String[] messageSplitter = dataToFindNote.split(Const.SINGLE_SPACE_SEPARATOR);
        StringBuilder noteBuilder = new StringBuilder();

        boolean noNoteInserted = messageSplitter.length == 2;
        if (noNoteInserted) {
            noteBuilder.append("No note added");
            return noteBuilder.toString();
        }

        int i;
        for (i = 2; i <= messageSplitter.length - 2; i++) {
            noteBuilder.append(messageSplitter[i]);
            noteBuilder.append(Const.SINGLE_SPACE_SEPARATOR);
        }
        noteBuilder.append(messageSplitter[i]);

        return noteBuilder.toString();
    }
}
