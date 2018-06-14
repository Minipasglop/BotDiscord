package discord.bot.utils;

import discord.bot.BotGlobalManager;
import net.dv8tion.jda.core.entities.Guild;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {

    private String botToken;

    private String userEventStatus;

    private String addRoleCommandStatus;

    private String youtubeApiKey;

    private final String MAIN_CONFIG_FILE_PATH = "jackson.properties";

    public String getBotToken() {
        return botToken;
    }

    public boolean isUserMovementListenerEnabled() {
        return ("true").equals(userEventStatus);
    }

    public String getYoutubeApiKey() {
        return youtubeApiKey;
    }

    public boolean isRoleAddingCommandEnabled(){ return("true").equals(addRoleCommandStatus); }

    public void initializeSavedProperties() {
        List<Guild> guildList = BotGlobalManager.getServers();
        for (int i = 0; i < guildList.size(); i++) {
            try {
                Map<String, String> propertiesValueForServer = new HashMap<>();
                File serverConfigFile = new File("jackson-guild-" + guildList.get(i).getId() + ".properties");
                FileInputStream serverConfigFileInput = new FileInputStream(serverConfigFile);
                Properties serverProperties = new Properties();
                serverProperties.load(serverConfigFileInput);
                serverConfigFileInput.close();
                propertiesValueForServer.put("autoRole", (String) serverProperties.get("autoRole"));
                propertiesValueForServer.put("userEventEnabled", (String) serverProperties.get("userEventEnabled"));
                propertiesValueForServer.put("userEventChannel", (String) serverProperties.get("userEventChannel"));
                ServerPropertiesManager.getInstance().setPropertiesForServer(guildList.get(i).getId(), propertiesValueForServer);
            } catch (IOException e) {
                System.out.println("Fichier introuvable pour le serveur : " + guildList.get(i).getName());
                e.printStackTrace();
            }
        }
    }

    public PropertiesLoader() {
        try {
            File configFile = new File(MAIN_CONFIG_FILE_PATH);
            FileInputStream fileInput = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
            botToken = properties.getProperty("botToken");
            userEventStatus = properties.getProperty("userEventStatus");
            addRoleCommandStatus = properties.getProperty("addRoleCommandStatus");
            youtubeApiKey = properties.getProperty("youtubeApiKey");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
