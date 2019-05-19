package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.GuildMusicManager;
import discord.bot.utils.audio.GuildMusicManagerSupervisor;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.save.ServerPropertiesManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SoundVolumeCommand extends ICommand {
    private final String HELP = "Set the volume (must be between 0 and 100). \nUsage :  `!" + this.commandName + " 20`";
    private final String VOLUME_MODIFIED = "The volume has been modified.";
    private final String COMMAND_FAILED = "Failed modifying the volume. Please make sure you set it between 0 and 100";

    public SoundVolumeCommand(String commandName){
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return args.length != 0 && !args[0].equals("help");
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        try{
            if(Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[0]) > 100){
                throw new Exception("Out of bounds.");
            }else {
                GuildMusicManager musicManager = GuildMusicManagerSupervisor.getInstance().getGuildMusicManager(event.getGuild().getIdLong());
                musicManager.player.setVolume(Integer.parseInt(args[0]));
                ServerPropertiesManager.getInstance().setPropertyForServer(event.getGuild().getId(), "volume", args[0]);
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),VOLUME_MODIFIED);
            }
        }catch (Exception e){
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),COMMAND_FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
