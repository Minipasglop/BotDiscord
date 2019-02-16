package discord.bot.utils.save;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServerPropertiesInjector {

    private final String FILE_PREFIX = "jackson-guild-";
    private final String FILE_EXTENSION = ".properties";
    private static Logger logger = Logger.getLogger(ServerPropertiesInjector.class);

    public Map<String,String> getPropertiesFromFile(String serverID){
        Map<String, String> serverProperties = new HashMap<>();
        File configFile = new File(FILE_PREFIX + serverID + FILE_EXTENSION);
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
            Enumeration<String> enums = (Enumeration<String>) properties.propertyNames();
            while (enums.hasMoreElements()) {
                String key = enums.nextElement();
                String value = properties.getProperty(key);
                serverProperties.put(key,value);
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, "Properties injector failed for server : " + serverID, e);
        }
        return serverProperties;
    }
}
