package com.oranim.telegrambot.FileHandelers;

import com.oranim.telegrambot.utils.BotLogging;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class JsonWorkloads {
    private String dataDir = System.getenv("DATA_DIR");
    public JsonWorkloads(){

    }

    /**
     * this method write json file to the file system
     * @param key the key for the json
     * @param val the val of the json
     * @param fileName the location of the json
     */
    public void jsonWriter(String key, String val, String fileName){
        String filePath = String.format("%s/%s",dataDir,fileName);

        JSONObject jsonObject = new JSONObject();

        if (jsonExist(Path.of(filePath))){
            jsonObject.putAll(jsonReader(fileName));
        }

        try(FileWriter fileWriter = new FileWriter(filePath)) {

            jsonObject.put(key,val);
            fileWriter.write(jsonObject.toJSONString());
            BotLogging.setInfoLog(classLog("jsonWriter","data written to json"));

        }catch (IOException ioException){
            System.out.println(Arrays.toString(ioException.getStackTrace()));
            BotLogging.setInfoLog(classLog("jsonReader", Arrays.toString(ioException.getStackTrace())));
        }

    }

    /**
     * this method read json file from the file system
     * @param fileName the location of the json
     * @return the json located in the file system as json object
     */

    public JSONObject jsonReader(String fileName )  {
        String filePath = String.format("%s/%s",dataDir,fileName);
        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(filePath));
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
