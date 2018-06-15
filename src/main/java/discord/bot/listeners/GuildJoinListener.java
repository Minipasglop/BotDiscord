package discord.bot.listeners;

import discord.bot.utils.ServerPropertiesManager;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class GuildJoinListener extends ListenerAdapter {


    //TODO, gestion du cas où on quitte un serveur, supprimer le fichier ?
    @Override
    public void onGuildJoin(GuildJoinEvent event){
        Map<String,String> propertiesForJoinedServer = new HashMap<>();
        for(int i = 0; i < ServerPropertiesManager.getInstance().getPropertiesList().size(); i++){
            propertiesForJoinedServer.put(ServerPropertiesManager.getInstance().getPropertiesList().get(i),null);
        }
        ServerPropertiesManager.getInstance().setPropertiesForServer(event.getGuild().getId(),propertiesForJoinedServer);
    }
}