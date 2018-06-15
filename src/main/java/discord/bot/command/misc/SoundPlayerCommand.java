package discord.bot.command.misc;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.AudioServerManager;
import discord.bot.utils.CustomAudioLoadResultHandler;
import discord.bot.utils.YoutubeApi;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class SoundPlayerCommand implements ICommand {

    private final String HELP = "Plays a sound from the Youtube Video found with the query parameters. \nUsage : `!sound query parameters`";
    private final String JOIN_VOCAL_CHANNEL = "Please join a vocal channel.";

    private final YoutubeApi youtubeApi = BotGlobalManager.getYoutubeApi();
    private Map<String,AudioServerManager> audioServerManagers = initGlobalManager();

    private Map<String,AudioServerManager> initGlobalManager(){
        Map<String, AudioServerManager> audioGlobalManagerMap = new HashMap<>();
        for(int i = 0; i < BotGlobalManager.getServers().size(); i++){
            audioGlobalManagerMap.put(BotGlobalManager.getServers().get(i).getId(),new AudioServerManager(new CustomAudioLoadResultHandler()));
        }
        return audioGlobalManagerMap;
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return args.length != 0 && !args[0].equals("help");
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        VoiceChannel targetChannel;
        AudioManager guildAudioManager = event.getGuild().getAudioManager();
        audioServerManagers.get(event.getGuild().getId()).getAudioLoadResultHandler().setChanToWrite(event.getTextChannel());
        audioServerManagers.get(event.getGuild().getId()).getAudioLoadResultHandler().setGuildAudioManager(guildAudioManager);
        //TODO interaction avec l'utilisateur : Contenu de la playlist, skip etc...
        switch (args[0]) {
            case "skip":
                if(!audioServerManagers.get(event.getGuild().getId()).skipTrack()){
                    guildAudioManager.setSendingHandler(null);
                    guildAudioManager.closeAudioConnection();
                }
                break;
            case "stop":
                try {
                    audioServerManagers.get(event.getGuild().getId()).emptyPlaylist();
                    guildAudioManager.setSendingHandler(null);
                    guildAudioManager.closeAudioConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "volume":
                audioServerManagers.get(event.getGuild().getId()).setVolume(Integer.parseInt(args[args.length-1]));
                break;
            default:
                targetChannel = event.getMember().getVoiceState().getChannel();
                if (targetChannel == null) {
                    event.getTextChannel().sendMessage(JOIN_VOCAL_CHANNEL).queue();
                } else {
                    String youtubeQuery = "";
                    for(int i = 0; i  < args.length; ++i){
                        youtubeQuery += args[i] + " ";
                    }
                    String youtubeSearch = youtubeApi.searchVideo(youtubeQuery);
                    if (!("").equalsIgnoreCase(youtubeSearch)) {
                        String videoURL = "https://youtu.be/" + youtubeSearch;
                        audioServerManagers.get(event.getGuild().getId()).getAudioLoadResultHandler().setTargetVoicelChannel(targetChannel);
                        audioServerManagers.get(event.getGuild().getId()).loadTrack(videoURL);
                    }
                }
                break;
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