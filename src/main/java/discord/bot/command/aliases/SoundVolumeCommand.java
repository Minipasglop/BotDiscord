package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.AudioServerManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Map;

public class SoundVolumeCommand implements ICommand {
    private final String HELP = "Set the volume (must be between 0 and 200). \nUsage : `!vol 20`";
    private final String VOLUME_MODIFIED = "The volume has been modified.";
    private final String COMMAND_FAILED = "Failed modifying the volume. Please make sure you set it between 0 and 200";

    private Map<String,AudioServerManager> audioServerManagers;

    public SoundVolumeCommand(Map<String,AudioServerManager> audioServerManagers){
        this.audioServerManagers =  audioServerManagers;
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return args.length != 0 && !args[0].equals("help");
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        try{
            if(Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[0]) > 200){
                throw new Exception("Out of bounds.");
            }else {
                audioServerManagers.get(event.getGuild().getId()).setVolume(Integer.parseInt(args[0]));
                event.getTextChannel().sendMessage(VOLUME_MODIFIED).queue();
            }
        }catch (Exception e){
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
