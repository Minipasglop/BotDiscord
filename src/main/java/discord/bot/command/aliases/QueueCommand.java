package discord.bot.command.aliases;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.AudioServerManager;
import discord.bot.utils.Track;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class QueueCommand implements ICommand {
    private final String HELP = "Display the current playlist status played. \nUsage : `!queue`";
    private final String PLAYLIST_STATUS = "Playlist current status : ";
    private final String NO_MORE_SOUND = "No more sound to be played.";
    private final String NO_SOUND_PLAYING = "No sound is currently being played.";
    private final String COMMAND_FAILED = "Failed displaying the playlist.";
    private final String EMPTY_QUEUE = "Playlist is empty.";

    private Map<String,AudioServerManager> audioServerManagers;

    public QueueCommand(Map<String,AudioServerManager> audioServerManagers){
        this.audioServerManagers =  audioServerManagers;
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return (args.length == 0 || !args[0].equals("help")) && args.length == 0;
    }

    private String getFormattedTrackName(Track trackToFormat){
        return trackToFormat.getTitle() + " - " + trackToFormat.getChannelTitle() + "\n";
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        try{
            AudioServerManager currAudioServerManager = audioServerManagers.get(event.getGuild().getId());
            String trackTitleList = EMPTY_QUEUE;
            String currentTrack = NO_SOUND_PLAYING + "\n̔̏";
            EmbedBuilder builder = new EmbedBuilder();
            if(currAudioServerManager.getTrackAmount() > 0){
                List<Track> trackList = currAudioServerManager.getTrackList();
                builder.setThumbnail(trackList.get(0).getThumbnailUrl());
                currentTrack = getFormattedTrackName(trackList.get(0)) + "̔̏";
                trackTitleList = "";
                for(int i = 1; i < trackList.size(); i++){
                    trackTitleList += getFormattedTrackName(trackList.get(i));
                }
            }if(currAudioServerManager.getTrackAmount() == 1){
                trackTitleList = NO_MORE_SOUND;
            }else {
                builder.setThumbnail(BotGlobalManager.getJda().getSelfUser().getAvatarUrl());
            }
            builder.setAuthor(PLAYLIST_STATUS);
            builder.setColor(Color.ORANGE);
            builder.addField("Current sound :loud_sound: ", currentTrack, false);
            builder.addField("Loop :repeat_one: ", (currAudioServerManager.isTrackLooping() ? "Enabled. \n̔̏" : "Disabled. \n̔̏"), false );
            builder.addField("Sound to come :bulb:", trackTitleList, false);
            event.getTextChannel().sendMessage(builder.build()).queue();
        }catch (Exception e){
            e.printStackTrace();
            event.getTextChannel().sendMessage(COMMAND_FAILED).queue();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        if(!success) {
            event.getTextChannel().sendMessage(help()).queue();
        }
    }
}
