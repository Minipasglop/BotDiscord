package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.AudioServerManager;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Map;

public class SoundSkipCommand extends ICommand {
    private final String HELP = "Skip the current track played. \nUsage : `!" + this.commandName + "`";
    private final String SOUND_SKIPPED_END = "Track has been skipped. No more in queue, `disconnecting.`";
    private final String COMMAND_FAILED = "Failed skipping the track. Please make sure a track is being played.";
    private final String NOTHING_TO_SKIP = "No track to skip.";
    private static Logger logger = Logger.getLogger(SoundSkipCommand.class);


    private Map<String,AudioServerManager> audioServerManagers;

    public SoundSkipCommand(Map<String,AudioServerManager> audioServerManagers, String commandName){
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
            if(currAudioServerManager.getTrackAmount() == 0){
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),NOTHING_TO_SKIP);
            }
            else if(!currAudioServerManager.skipTrack()){
                currAudioServerManager.getAudioLoadResultHandler().getGuildAudioManager().setSendingHandler(null);
                currAudioServerManager.getAudioLoadResultHandler().getGuildAudioManager().closeAudioConnection();
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),SOUND_SKIPPED_END);
            }
        }catch (Exception e){
            logger.log(Level.ERROR, e.getMessage());
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),COMMAND_FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
