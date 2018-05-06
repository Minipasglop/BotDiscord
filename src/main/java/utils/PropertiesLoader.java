package utils;

import java.io.*;
import java.util.Properties;

public class PropertiesLoader {

    private String botToken;

    private String userEventStatus;

    private String addRoleCommandStatus;

    private final String FILE_PATH = "jackson.properties";

    public String getBotToken() {
        return botToken;
    }

    public boolean isUserMovementListenerEnabled() {
        return ("true").equals(userEventStatus);
    }

    public boolean isRoleAddingCommandEnabled(){ return("true").equals(addRoleCommandStatus); }

    public PropertiesLoader() {
        try {
            File configFile = new File(FILE_PATH);
            FileInputStream fileInput = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();

            botToken = properties.getProperty("botToken");
            userEventStatus = properties.getProperty("userEventStatus");
            addRoleCommandStatus = properties.getProperty("addRoleCommandStatus");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
