package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.AudioServerManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Map;

public class SoundPauseCommand extends ICommand {
    private final String HELP = "Pauses / Resumes the current track. \nUsage : `!" + this.commandName + "`";
    private final String PAUSED = "Current track has been paused.";
    private final String RESUMED = "Current track has been resumed.";
    private final String COMMAND_FAILED = "Failed pausing the sound. Please make sure a track is currently being played.";

    private Map<String,AudioServerManager> audioServerManagers;

    public SoundPauseCommand(Map<String,AudioServerManager> audioServerManagers, String commandName){
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
            if(currAudioServerManager.isTrackPaused()){
                event.getTextChannel().sendMessage(RESUMED).queue();
                currAudioServerManager.setTrackPaused(false);
            }else {
                event.getTextChannel().sendMessage(PAUSED).queue();
                currAudioServerManager.setTrackPaused(true);
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
