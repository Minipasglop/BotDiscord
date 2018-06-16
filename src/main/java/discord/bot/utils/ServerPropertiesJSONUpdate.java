package discord.bot.utils;

import discord.bot.BotGlobalManager;

import java.io.*;
import java.util.Properties;

public class ServerPropertiesJSONUpdate {

    private String autoRole;
    private String userEventEnabled;
    private String userEventChannel;

    public ServerPropertiesJSONUpdate(String serverID) {
        try {
            //Opening properties file
            File configFile = new File("jackson-guild-"+serverID+".properties");
            if(configFile.createNewFile()){
                System.out.println("Fichier créé pour le serveur : " + BotGlobalManager.getJda().getGuildById(serverID).getName());
            }
            FileInputStream fileInput = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
            initProperties(serverID);
            FileOutputStream fileOutput = new FileOutputStream(configFile);
            properties.setProperty("autoRole", autoRole);
            properties.setProperty("userEventEnabled", userEventEnabled);
            properties.setProperty("userEventChannel", userEventChannel);

            properties.store(fileOutput, null);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initProperties(String serverId){
        autoRole = ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(serverId,"autoRole");
        userEventEnabled = ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(serverId,"userEventEnabled");
        userEventChannel = ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(serverId,"userEventChannel");
    }
}
