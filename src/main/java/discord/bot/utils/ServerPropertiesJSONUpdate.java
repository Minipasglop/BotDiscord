package discord.bot.utils;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class ServerPropertiesJSONUpdate {

    private String autoRole;
    private String userEventEnabled;
    private String userEventChannel;

    public ServerPropertiesJSONUpdate(String serverID) {
        Map<String,String> propertiesForServer = ServerPropertiesManager.getInstance().getPropertiesFromServer(serverID);
        try {
            //Opening properties file
            File configFile = new File("jackson-guild-"+serverID+".properties");
            if(configFile.createNewFile()){
                System.out.println("File created for server id " + serverID);
            }
            FileInputStream fileInput = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
            initProperties(propertiesForServer);
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
    private void initProperties( Map<String,String> propertiesForServer){
        autoRole = propertiesForServer.get("autoRole");
        userEventEnabled = propertiesForServer.get("userEventEnabled");
        userEventChannel = propertiesForServer.get("userEventChannel");
        if(autoRole == null){
            autoRole = "null";
        }if(userEventChannel == null){
            userEventChannel = "null";
        }if(userEventEnabled == null){
            userEventEnabled = "false";
        }
    }
}
