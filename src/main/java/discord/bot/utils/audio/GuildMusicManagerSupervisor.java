package discord.bot.utils.audio;

import discord.bot.BotGlobalManager;

import java.util.HashMap;
import java.util.Map;

public class GuildMusicManagerSupervisor {

    private Map<Long, GuildMusicManager> musicManagers;
    private static GuildMusicManagerSupervisor instance;

    public static GuildMusicManagerSupervisor getInstance(){
        if(instance == null){
            instance = new GuildMusicManagerSupervisor();
        }
        return instance;
    }

    private GuildMusicManagerSupervisor() {
        this.musicManagers = new HashMap<>();
    }

    public GuildMusicManager getGuildMusicManager(final Long id){
        if(this.musicManagers.get(id) == null){
            this.musicManagers.put(id, new GuildMusicManager(BotGlobalManager.getAudioPlayerManager()));
        }
        return this.musicManagers.get(id);
    }
}
