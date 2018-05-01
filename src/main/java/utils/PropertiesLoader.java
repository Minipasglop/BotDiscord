package utils;

import java.io.*;
import java.util.Properties;

public class PropertiesLoader {

    private String botToken;

    private String userEventStatus;

    public String getBotToken() {
        return botToken;
    }

    public boolean isUserMovelementListenerEnabled() {
        return ("true").equals(userEventStatus);
    }

    public PropertiesLoader() {
        try {
            File configFile = new File("jackson.properties");
            FileInputStream fileInput = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();

            botToken = properties.getProperty("botToken");
            userEventStatus = properties.getProperty("userEventStatus");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
