package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.AudioServerManager;
import discord.bot.utils.ServerPropertiesManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Map;

public class SoundLoopCommand extends ICommand {
    private final String HELP = "Enables / Disables the repetition of the current track. \nUsage : `!" + this.commandName + "`";
    private final String TRACK_LOOP_ENABLED = "Audio player will loop on current played track.";
    private final String TRACK_LOOP_DISABLED = "Audio player will no longer loop on the current played track.";
    private final String COMMAND_FAILED = "Failed looping the sound. Please make sure a track is being played";

    private Map<String,AudioServerManager> audioServerManagers;

    public SoundLoopCommand(Map<String,AudioServerManager> audioServerManagers, String commandName){
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
            if(currAudioServerManager.isTrackLooping() || ("true").equals(ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(), "loop"))){
                ServerPropertiesManager.getInstance().setPropertyForServer(event.getGuild().getId(), "loop", "false");
                event.getTextChannel().sendMessage(TRACK_LOOP_DISABLED).queue();
            }else {
                ServerPropertiesManager.getInstance().setPropertyForServer(event.getGuild().getId(), "loop", "true");
                event.getTextChannel().sendMessage(TRACK_LOOP_ENABLED).queue();
            }
            currAudioServerManager.reverseTrackLoop();
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
