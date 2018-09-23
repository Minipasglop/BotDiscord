package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.AudioServerManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Map;

public class SoundShuffleCommand extends ICommand {
    private final String HELP = "Shuffles the playlist order. \nUsage : `!" + this.commandName + "`";
    private final String PLAYLIST_SHUFFLED = "Playlist has been successfully shuffled.";
    private final String NO_TRACK_TO_SHUFFLE = "Playlist don't have enough tracks to be shuffled :thinking:";
    private final String COMMAND_FAILED = "Failed shuffleing the playlist. Please make sure tracks are queued.";

    private Map<String,AudioServerManager> audioServerManagers;

    public SoundShuffleCommand(Map<String,AudioServerManager> audioServerManagers, String commandName){
        super(commandName);
        this.audioServerManagers =  audioServerManagers;
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return (args.length == 0 || !args[0].equals("help")) && args.length == 0;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        try{
            AudioServerManager currAudioServerManager = audioServerManagers.get(event.getGuild().getId());
            if(currAudioServerManager.getTrackAmount() > 1){
                currAudioServerManager.shufflePlaylist();
                event.getTextChannel().sendMessage(PLAYLIST_SHUFFLED).queue();
            }else {
                event.getTextChannel().sendMessage(NO_TRACK_TO_SHUFFLE).queue();
            }
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
