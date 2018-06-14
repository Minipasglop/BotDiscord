package discord.bot.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class ServerPropertiesJSONUpdate {

    private String autoRole;
    private String userEventEnabled;
    private String userEventChannel;

    public ServerPropertiesJSONUpdate(String serverID) {
        Map<String,String> propertiesForServer = ServerPropertiesManager.getInstance().getPropertiesFromServer(serverID);
        try {
            autoRole = propertiesForServer.get("autoRole");
            userEventEnabled = propertiesForServer.get("userEventEnabled");
            userEventChannel = propertiesForServer.get("userEventChannel");
            //Opening properties file
            File configFile = new File("jackson-guild-"+serverID+".properties");
            FileInputStream fileInput = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();

            FileOutputStream fileOutput = new FileOutputStream(configFile);
            properties.setProperty("autoRole", autoRole);
            properties.setProperty("userEventEnabled", userEventEnabled);
            properties.setProperty("userEventChannel", userEventChannel);

            properties.store(fileOutput, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
