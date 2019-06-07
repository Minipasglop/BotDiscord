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
    private String catApiKey;
    private String dogApiKey;
    private String chuckNorrisApiKey;
    private String botOwnerUserId;
    private String botLogsChannelId;
    private String supportServerId;
    private final String MAIN_CONFIG_FILE_PATH = "jackson.properties";

    public String getBotToken() {
        return botToken;
    }

    public String getYoutubeApiKey() {
        return youtubeApiKey;
    }

    public String getCatApiKey() {
        return catApiKey;
    }

    public String getDogApiKey() {
        return dogApiKey;
    }

    public String getChuckNorrisApiKey() {
        return chuckNorrisApiKey;
    }

    public boolean isRoleAddingCommandEnabled(){ return("true").equals(addRoleCommandStatus); }

    public String getBotOwnerUserId() {
        return botOwnerUserId;
    }

    public String getBotLogsChannelId() {
        return botLogsChannelId;
    }

    public String getSupportServerId() {
        return supportServerId;
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
                for(PropertyEnum property : PropertyEnum.values()){
                    propertiesValueForServer.put(property.getPropertyName(), (String) serverProperties.get(property.getPropertyName()));
                }
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
            catApiKey = properties.getProperty("catApiKey");
            dogApiKey = properties.getProperty("dogApiKey");
            chuckNorrisApiKey = properties.getProperty("chuckNorrisApiKey");
            botOwnerUserId = properties.getProperty("botOwnerUserId");
            botLogsChannelId = properties.getProperty("botLogsChannelId");
            supportServerId = properties.getProperty("supportServerId");
        } catch (IOException e) {
            logger.log(Level.ERROR, "Something went wrong", e);
        }
    }
}
