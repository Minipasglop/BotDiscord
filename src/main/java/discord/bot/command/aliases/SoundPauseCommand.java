package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.GuildMusicManager;
import discord.bot.utils.audio.GuildMusicManagerSupervisor;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SoundPauseCommand extends ICommand {
    private final String HELP = "Pauses / Resumes the current track. \nUsage : `!" + this.commandName + "`";
    private final String PAUSED = "Current track has been paused.";
    private final String RESUMED = "Current track has been resumed.";
    private final String COMMAND_FAILED = "Failed pausing the sound. Please make sure a track is currently being played.";
    private static Logger logger = Logger.getLogger(SoundPauseCommand.class);

    public SoundPauseCommand(String commandName){
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
            if(musicManager.player.isPaused()){
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),RESUMED);
                musicManager.player.setPaused(false);
            }else {
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),PAUSED);
                musicManager.player.setPaused(true);
            }
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
