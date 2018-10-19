package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.AudioServerManager;
import discord.bot.utils.audio.Track;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class SoundQueueCommand extends ICommand {
    private final String HELP = "Display the current playlist status played. \nUsage : `!" + this.commandName + "`";
    private final String PLAYLIST_STATUS = "Playlist current status : ";
    private final String NO_MORE_SOUND = "No more track to be played.";
    private final String NO_SOUND_PLAYING = "No sound is currently being played.";
    private final String COMMAND_FAILED = "Failed displaying the playlist.";
    private final String EMPTY_QUEUE = "Playlist is empty.";
    private static Logger logger = Logger.getLogger(SoundQueueCommand.class);


    private Map<String,AudioServerManager> audioServerManagers;

    public SoundQueueCommand(Map<String,AudioServerManager> audioServerManagers, String commandName){
        super(commandName);
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
                    trackTitleList += i + " - " + getFormattedTrackName(trackList.get(i));
                }
            }else {
                builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
            }
            if(currAudioServerManager.getTrackAmount() == 1){
                trackTitleList = NO_MORE_SOUND;
            }
            builder.setAuthor(PLAYLIST_STATUS);
            builder.setColor(Color.ORANGE);
            builder.addField("Current track :loud_sound:", currentTrack, false);
            builder.addField("Loop :repeat_one:", (currAudioServerManager.isTrackLooping() ? "Enabled. \n̔̏" : "Disabled. \n̔̏"), false );
            builder.addField("Queued tracks :bulb:", trackTitleList, false);
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),builder.build());
        }catch (Exception e){
            logger.log(Level.ERROR, "Something went wrong", e);
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),COMMAND_FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
