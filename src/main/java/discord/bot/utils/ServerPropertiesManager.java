package discord.bot.utils;

import discord.bot.BotGlobalManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerPropertiesManager {

    private Map<String,Map<String,String>> globalProperties;
    private List<String> propertiesList;
    private ServerPropertiesInjector injector;

    private static ServerPropertiesManager instance;

    /* Pour ajouter une propriété : Il faut l'ajouter dans cette property list
        La gérer au niveau du ServerpropertiesJSONUpdate
        L'impacter dans la méthode initializeSavedProperties du PropertiesLoader
     */

    private void createPropertiesList(){
        propertiesList = new ArrayList<>();
        propertiesList.add("autoRole");
        propertiesList.add("userEventChannel");
        propertiesList.add("userEventEnabled");
        propertiesList.add("volume");
        propertiesList.add("loop");
    }

    public static ServerPropertiesManager getInstance(){
        if(instance == null){
            instance = new ServerPropertiesManager();
        }
        return instance;
    }

    public void setPropertyForServer(String serverId,String property, String value){
        Map<String,String> buffMap = new HashMap<>();
        if(globalProperties.get(serverId) != null){
            buffMap = globalProperties.get(serverId);
        }else {
            globalProperties.put(serverId,new HashMap<>());
        }
        buffMap.put(property,value);
        globalProperties.put(serverId,buffMap);
    }

    private ServerPropertiesManager(){
        globalProperties = new HashMap<>();
        injector = new ServerPropertiesInjector();
        System.out.println( BotGlobalManager.getServers().size());
        for(int i = 0; i < BotGlobalManager.getServers().size(); i++){
            String currServerId = BotGlobalManager.getServers().get(i).getId();
            globalProperties.put(currServerId, injector.getPropertiesFromFile(currServerId));
        }
        createPropertiesList();
    }

    public List<String> getPropertiesList(){ return propertiesList; }

    public void setPropertiesForServer(String serverId,Map<String,String> properties){
        globalProperties.put(serverId,properties);
    }

    public String getPropertyOrBlankFromServer(String serverId,String property){
        try{
            String value = globalProperties.get(serverId).get(property);
            if( value == null) return "";
            else return value;
        }catch(NullPointerException e){
            return "";
        }
    }
}
