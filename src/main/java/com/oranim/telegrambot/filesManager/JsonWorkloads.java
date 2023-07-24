package com.oranim.telegrambot.filesManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.LogWarningLevel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JsonWorkloads {
    private String dataDir = System.getenv("DATA_DIR");

    public JsonWorkloads() {

    }

    /**
     * this method write json file to the file system
     *
     * @param map      contains the jsons values
     * @param fileName the location of the json
     */
    public <K,V> void writeJson(Map<K,V> map, String fileName) {
        Path filePath = Paths.get(dataDir, fileName);

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (!fileExist(filePath)){
               createNewFile(filePath);

                mapper.writeValue(Paths.get(filePath.toUri()).toFile(), map);

                BotLogging.createLog(LogWarningLevel.INFO,JsonWorkloads.class.getName(),"jsonWriter", "data written successfully for the first time");
                return;
            }

            Map<String, String> existingData = new HashMap<>(getJson(fileName));
            map.forEach((key, value) -> existingData.put(key.toString(), value.toString()));
            mapper.writeValue(Paths.get(filePath.toUri()).toFile(), existingData);

            BotLogging.createLog(LogWarningLevel.INFO,JsonWorkloads.class.getName(),"jsonWriter", "data written successfully");


        } catch (IOException e){
            BotLogging.createLog(LogWarningLevel.WARN,JsonWorkloads.class.getName(),"jsonWriter", Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * this method read json file from the file system
     *
     * @param fileName the location of the json
     * @return the json located in the file system as json object
     */

    public <K,V> Map<K,V> getJson(String fileName) {

        Path filePath = Paths.get(dataDir, fileName);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(new File(filePath.toUri()) , HashMap.class);

        }catch (IOException e){

        }
        return new HashMap<>();
    }


    /**
     * this method checks if json file exist in the file path
     *
     * @param path the path of the file
     * @return return True / False if the file exist
     */
    private boolean fileExist(Path path) {
        return Files.exists(path);
    }

    private void createNewFile(Path filePath) throws IOException {
        File file = new File(filePath.toUri());
        file.createNewFile();
    }



}
