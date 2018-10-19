package discord.bot.utils.save;

import discord.bot.BotGlobalManager;
import net.dv8tion.jda.core.entities.Guild;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {

    private static Logger logger = Logger.getLogger(PropertiesLoader.class);
    private String botToken;
    private String addRoleCommandStatus;
    private String youtubeApiKey;
    private String botOwnerUserId;
    private final String MAIN_CONFIG_FILE_PATH = "jackson.properties";

    public String getBotToken() {
        return botToken;
    }

    public String getYoutubeApiKey() {
        return youtubeApiKey;
    }

    public boolean isRoleAddingCommandEnabled(){ return("true").equals(addRoleCommandStatus); }

    public String getBotOwnerUserId() {
        return botOwnerUserId;
    }

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
                propertiesValueForServer.put("volume", (String) serverProperties.get("volume"));
                propertiesValueForServer.put("loop", (String) serverProperties.get("loop"));
                ServerPropertiesManager.getInstance().setPropertiesForServer(guildList.get(i).getId(), propertiesValueForServer);
            } catch (IOException e) {
                logger.log(Level.INFO,"Fichier introuvable pour le serveur : " + guildList.get(i).getName());
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
            addRoleCommandStatus = properties.getProperty("addRoleCommandStatus");
            youtubeApiKey = properties.getProperty("youtubeApiKey");
            botOwnerUserId = properties.getProperty("botOwnerUserId");
        } catch (IOException e) {
            logger.log(Level.ERROR, "Something went wrong", e);
        }
    }
}
