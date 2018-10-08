package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.AudioServerManager;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Map;

public class SoundStopCommand extends ICommand {
    private final String HELP = "Stop the sound playing and disconnect the bot from the voice channel. \nUsage : `!" + this.commandName + "`";
    private final String SOUND_STOPPED = "Sound has been stopped.";
    private final String COMMAND_FAILED = "Failed executing this command. Please make sure a track is being played.";

    private Map<String,AudioServerManager> audioServerManagers;

    public SoundStopCommand(Map<String,AudioServerManager> audioServerManagers, String commandName){
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
            currAudioServerManager.emptyPlaylist();
            currAudioServerManager.getAudioLoadResultHandler().getGuildAudioManager().setSendingHandler(null);
            currAudioServerManager.getAudioLoadResultHandler().getGuildAudioManager().closeAudioConnection();
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),SOUND_STOPPED);
        }catch (Exception e){
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),COMMAND_FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
