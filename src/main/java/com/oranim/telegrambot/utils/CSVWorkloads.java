package com.oranim.telegrambot.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CSVWorkloads {


    public CSVWorkloads(){

    }

    private boolean dirExist(String dirPath){
        return Files.exists(Path.of(dirPath));
    }

    private void makeDir(String dirName) throws IOException {
        Files.createDirectory(Path.of(dirName));
    }

    public void handleCSV(){

    }


}
