package discord.bot.utils.save;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerPropertiesJSONUpdate {

    private static Logger logger = Logger.getLogger(ServerPropertiesJSONUpdate.class);

    public ServerPropertiesJSONUpdate(String serverID) {
        try {
            //Opening properties file
            File configFile = new File("jackson-guild-" + serverID + ".properties");
            if(configFile.createNewFile()){
                logger.log(Level.INFO, "Fichier créé pour le serveur : " + serverID);
            }
            FileInputStream fileInput = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
            FileOutputStream fileOutput = new FileOutputStream(configFile);
            for(PropertyEnum property : PropertyEnum.values()){
                properties.setProperty(property.getPropertyName(),ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(serverID,property.getPropertyName()));
            }
            properties.store(fileOutput, null);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Something went wrong during properties saving", e);
        }
    }

}
