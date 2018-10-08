package discord.bot.command.misc;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.audio.AudioServerManager;
import discord.bot.utils.audio.CustomAudioLoadResultHandler;
import discord.bot.utils.audio.Track;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.misc.YoutubeApi;
import discord.bot.utils.save.ServerPropertiesManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayerCommand extends ICommand {

    private final String HELP = "Plays a track from Youtube using the name. \nUsage : `!" + this.commandName + " songName`";
    private final String JOIN_VOCAL_CHANNEL = "Please join a vocal channel.";
    private final String SOUND_QUEUED = "Sound has been queued";

    private YoutubeApi youtubeApi;
    private Map<String, AudioServerManager> audioServerManagers;
    public SoundPlayerCommand(String commandName){
        super(commandName);
        youtubeApi = BotGlobalManager.getYoutubeApi();
        audioServerManagers = new HashMap<>();
    }

    public Map<String,AudioServerManager> getAudioServerManagers() { return this.audioServerManagers; }

    private void initServerManagers(){
        for(int i = 0; i < BotGlobalManager.getServers().size(); i++){
            if(audioServerManagers.get(BotGlobalManager.getServers().get(i).getId()) == null)
                audioServerManagers.put(BotGlobalManager.getServers().get(i).getId(),new AudioServerManager(new CustomAudioLoadResultHandler()));
        }
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(audioServerManagers.isEmpty() || audioServerManagers.size() != BotGlobalManager.getServers().size()){
            initServerManagers();
        }
        return args.length != 0 && !args[0].equals("help");
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        int volume = 50;
        VoiceChannel targetChannel = event.getMember().getVoiceState().getChannel();
        AudioManager guildAudioManager = event.getGuild().getAudioManager();
        AudioServerManager currAudioServerManager = audioServerManagers.get(event.getGuild().getId());
        currAudioServerManager.getAudioLoadResultHandler().setChanToWrite(event.getTextChannel());
        currAudioServerManager.getAudioLoadResultHandler().setGuildAudioManager(guildAudioManager);
        if (targetChannel == null) {
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),JOIN_VOCAL_CHANNEL);
        } else {
            if(!("").equals(ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(),"volume")) && ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(),"volume") != null){
                volume = Integer.parseInt(ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(),"volume"));
            }
            String youtubeQuery = "";
            for(int i = 0; i  < args.length; ++i){
                youtubeQuery += args[i] + " ";
            }
            System.out.println(event.getAuthor().getName() + " searched : " + youtubeQuery + " on server : " + event.getGuild().getName());
            Track youtubeSearch = youtubeApi.searchVideo(youtubeQuery);
            if (youtubeSearch != null) {
                currAudioServerManager.getAudioLoadResultHandler().setTargetVoicelChannel(targetChannel);
                currAudioServerManager.loadTrack(youtubeSearch);
                currAudioServerManager.setVolume(volume);
                if(("true").equals(ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(), "loop")) && !currAudioServerManager.isTrackLooping()){
                    currAudioServerManager.reverseTrackLoop();
                }
                EmbedBuilder builder = new EmbedBuilder();
                builder.setAuthor(SOUND_QUEUED);
                builder.setColor(Color.ORANGE);
                builder.setThumbnail(youtubeSearch.getThumbnailUrl());
                builder.addField("Video Title :movie_camera: ", youtubeSearch.getTitle() + " - " + youtubeSearch.getChannelTitle() + "\n̔̏", false);
                builder.addField("Playlist status :bulb:", String.valueOf(currAudioServerManager.getTrackAmount()) + " track" + (currAudioServerManager.getTrackAmount() == 1 ? "" : "s") + " listed.", false);
                if(event.getTextChannel().canTalk()){
                    MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),builder.build());
                }
            }
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}