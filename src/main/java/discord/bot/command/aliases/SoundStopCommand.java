package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.GuildMusicManager;
import discord.bot.utils.audio.GuildMusicManagerSupervisor;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SoundStopCommand extends ICommand {
    private final String HELP = "Stop the sound playing and disconnect the bot from the voice channel. \nUsage : `!" + this.commandName + "`";
    private final String SOUND_STOPPED = "Sound has been stopped.";
    private final String COMMAND_FAILED = "Failed executing this command. Please make sure a track is being played.";

    public SoundStopCommand( String commandName){
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
            musicManager.scheduler.stop();
            event.getGuild().getAudioManager().closeAudioConnection();
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
