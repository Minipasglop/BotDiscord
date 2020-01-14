
package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.GuildMusicManager;
import discord.bot.utils.audio.GuildMusicManagerSupervisor;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class SoundSkipCommand extends ICommand {
    private final String HELP = "Skip the current track played. \nUsage : `!" + this.commandName + "`";
    private final String SOUND_SKIPPED_END = "Track has been skipped. No more in queue, `disconnecting.`";
    private final String COMMAND_FAILED = "Failed skipping the track. Please make sure a track is being played.";
    private final String NOTHING_TO_SKIP = "No track to skip.";
    private static Logger logger = Logger.getLogger(SoundSkipCommand.class);

    public SoundSkipCommand(String commandName){
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
            if(musicManager.scheduler.getTrackAmount() == 0){
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),NOTHING_TO_SKIP);
                return;
            }
            musicManager.scheduler.nextTrack();
            if(musicManager.player.isPaused()){
                event.getGuild().getAudioManager().closeAudioConnection();
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),SOUND_SKIPPED_END);
            }
        }catch (Exception e){
            logger.log(Level.ERROR,event.getMessage(), e);
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),COMMAND_FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
