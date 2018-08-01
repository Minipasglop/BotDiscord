package discord.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import discord.bot.listeners.GuildJoinListener;
import discord.bot.listeners.MessageListener;
import discord.bot.listeners.UserMovementListener;
import discord.bot.utils.CommandHandler;
import discord.bot.utils.PropertiesLoader;
import discord.bot.utils.SaveThread;
import discord.bot.utils.YoutubeApi;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class BotGlobalManager {
    private static List<JDA> shards;
    private static PropertiesLoader config = new PropertiesLoader();
    private static AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
    private static YoutubeApi youtubeApi = new YoutubeApi();
    private final int SHARD_AMMOUNT = 10;

    BotGlobalManager() {
        try {
            shards = new ArrayList<>();
            JDABuilder shardBuilder = new JDABuilder(AccountType.BOT).setGame(Game.of(Game.GameType.WATCHING,"!help")).setToken(config.getBotToken()).setBulkDeleteSplittingEnabled(false);
            shardBuilder.addEventListener(new MessageListener());
            shardBuilder.addEventListener(new UserMovementListener());
            shardBuilder.addEventListener(new GuildJoinListener());
            for(int i = 0; i < SHARD_AMMOUNT; i++){
                shards.add(shardBuilder.useSharding(i, SHARD_AMMOUNT)
                        .buildAsync());
            }
            audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
            audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
            config.initializeSavedProperties();
            SaveThread saveThread = new SaveThread();
            saveThread.start();
        } catch (LoginException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue veuillez verifier le token ou votre connection internet");
        }
    }//Constructeur de la JDA permettant de faire fonctionner le bot et le couper en tapant stop dans la console

    public static void main(String[] args) {
        new BotGlobalManager();
    }//Fonction main

    public static YoutubeApi getYoutubeApi(){ return youtubeApi; }

    public static PropertiesLoader getConfig() {
        return config;
    }

    public static AudioPlayerManager getAudioPlayerManager() {return audioPlayerManager;}

    public static List<Guild> getServers() {
        List<Guild> serverCount = new ArrayList<>();
        for(int i = 0; i < shards.size(); i++){
            serverCount.addAll(shards.get(i).getGuilds());
        }
        return serverCount;
    }

}
