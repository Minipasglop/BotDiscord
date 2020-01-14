package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.GuildMusicManager;
import discord.bot.utils.audio.GuildMusicManagerSupervisor;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.save.PropertyEnum;
import discord.bot.utils.save.ServerPropertiesManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SoundLoopCommand extends ICommand {
    private final String HELP = "Enables / Disables the repetition of the current track. \nUsage : `!" + this.commandName + "`";
    private final String TRACK_LOOP_ENABLED = "Audio player will loop on current played track.";
    private final String TRACK_LOOP_DISABLED = "Audio player will no longer loop on the current played track.";
    private final String COMMAND_FAILED = "Failed looping the sound. Please make sure a track is being played";
    private static Logger logger = Logger.getLogger(SoundLoopCommand.class);

    public SoundLoopCommand(String commandName){
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return (args.length == 0 || !args[0].equals("help")) && args.length == 0;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        try{
            GuildMusicManager musicManager = GuildMusicManagerSupervisor.getInstance().getGuildMusicManager(event.getGuild().getIdLong());
            if(("true").equals(ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(), PropertyEnum.LOOP.getPropertyName()))){
                musicManager.scheduler.setLooping(true);
                ServerPropertiesManager.getInstance().setPropertyForServer(event.getGuild().getId(), "loop", "false");
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),TRACK_LOOP_DISABLED);
            }else {
                ServerPropertiesManager.getInstance().setPropertyForServer(event.getGuild().getId(), "loop", "true");
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),TRACK_LOOP_ENABLED);
            }
            musicManager.scheduler.reverseTrackLoop();
        }catch (Exception e){
            logger.log(Level.ERROR, event.getMessage(), e);
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),COMMAND_FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}