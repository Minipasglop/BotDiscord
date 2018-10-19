package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.AudioServerManager;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Map;

public class SoundPauseCommand extends ICommand {
    private final String HELP = "Pauses / Resumes the current track. \nUsage : `!" + this.commandName + "`";
    private final String PAUSED = "Current track has been paused.";
    private final String RESUMED = "Current track has been resumed.";
    private final String COMMAND_FAILED = "Failed pausing the sound. Please make sure a track is currently being played.";
    private static Logger logger = Logger.getLogger(SoundPauseCommand.class);

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
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),RESUMED);
                currAudioServerManager.setTrackPaused(false);
            }else {
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),PAUSED);
                currAudioServerManager.setTrackPaused(true);
            }
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
