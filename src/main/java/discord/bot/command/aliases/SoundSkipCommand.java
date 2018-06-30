package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.AudioServerManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Map;

public class SoundSkipCommand implements ICommand {
    private final String HELP = "Skip the current track played. \nUsage : `!skip`";
    private final String SOUND_SKIPPED_NEXT = "Track has been skipped. Next to be played : ";
    private final String SOUND_SKIPPED_END = "Track has been skipped. No more in queue, `disconnecting.`";
    private final String COMMAND_FAILED = "Failed skipping the track. Please make sure a track is being played.";
    private final String NOTHING_TO_SKIP = "No track to skip.";

    private Map<String,AudioServerManager> audioServerManagers;

    public SoundSkipCommand(Map<String,AudioServerManager> audioServerManagers){
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
                event.getTextChannel().sendMessage(NOTHING_TO_SKIP).queue();
            }
            else if(!currAudioServerManager.skipTrack()){
                currAudioServerManager.getAudioLoadResultHandler().getGuildAudioManager().setSendingHandler(null);
                currAudioServerManager.getAudioLoadResultHandler().getGuildAudioManager().closeAudioConnection();
                event.getTextChannel().sendMessage(SOUND_SKIPPED_END).queue();
            }else{
                event.getTextChannel().sendMessage(SOUND_SKIPPED_NEXT + currAudioServerManager.getNextTrackURL()).queue();
            }
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
