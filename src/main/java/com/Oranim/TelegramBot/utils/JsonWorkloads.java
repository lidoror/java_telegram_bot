package com.Oranim.TelegramBot.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class JsonWorkloads {

    public JsonWorkloads(){

    }

    /**
     * this method write json file to the file system
     * @param key the key for the json
     * @param val the val of the json
     * @param fileLocation the location of the json
     */
    public void jsonWriter(String key, String val, String fileLocation){
        JSONObject jsonObject = new JSONObject();

        if (jsonExist(Path.of(fileLocation))){
            jsonObject.putAll(jsonReader(fileLocation));
        }

        try(FileWriter fileWriter = new FileWriter(fileLocation)) {

            jsonObject.put(key,val);
            fileWriter.write(jsonObject.toJSONString());
            BotLogging.setInfoLog(classLog("jsonWriter","data written to json"));

        }catch (IOException ioException){
            BotLogging.setInfoLog(classLog("jsonReader", Arrays.toString(ioException.getStackTrace())));
        }

    }

    /**
     * this method read json file from the file system
     * @param fileLocation the location of the json
     * @return the json located in the file system as json object
     */

    public JSONObject jsonReader(String fileLocation )  {
        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(fileLocation));
            BotLogging.setInfoLog(classLog("jsonWriter","readed data from json"));
            return jsonObject;


        }catch (IOException | ParseException Exception){
            BotLogging.setInfoLog(classLog("jsonReader" , Arrays.toString(Exception.getStackTrace())));
        }
        return null;

        }


    /**
     * this method checks if json file exist in the file path
     * @param path the path of the file
     * @return return True / False if the file exist
     */
    private boolean jsonExist(Path path){
        boolean exists = Files.exists(path);
        return exists;
    }


    private String classLog(String method,String description){
        return "Exception accure in class: %s , method: %s Description: %s".formatted(JsonWorkloads.class.getName(), method, description);
    }
}
