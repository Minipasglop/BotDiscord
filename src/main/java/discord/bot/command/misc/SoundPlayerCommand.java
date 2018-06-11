package discord.bot.command.misc;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.AudioGlobalManager;
import discord.bot.utils.CustomAudioLoadResultHandler;
import discord.bot.utils.YoutubeApi;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class SoundPlayerCommand implements ICommand {

    private final String HELP = "Joue le son souhaité dans le salon vocal courant de l'utilisateur. \nUsage : `!sound <nomDuSon>`";

    private final YoutubeApi youtubeApi = BotGlobalManager.getYoutubeApi();
    private CustomAudioLoadResultHandler myAudioLoadResultHandler = new CustomAudioLoadResultHandler();

    private AudioGlobalManager audioGlobalManager = new AudioGlobalManager(myAudioLoadResultHandler);


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return args.length != 0 && !args[0].equals("help");
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        VoiceChannel targetChannel;
        AudioManager guildAudioManager = event.getGuild().getAudioManager();
        myAudioLoadResultHandler.setChanToWrite(event.getTextChannel());
        myAudioLoadResultHandler.setGuildAudioManager(guildAudioManager);
        //TODO interaction avec l'utilisateur : Contenu de la playlist, skip etc...
        switch (args[0]) {
            case "skip":
                if(!audioGlobalManager.skipTrack()){
                    guildAudioManager.setSendingHandler(null);
                    guildAudioManager.closeAudioConnection();
                }
                break;
            case "stop":
                try {
                    audioGlobalManager.emptyPlaylist();
                    guildAudioManager.setSendingHandler(null);
                    guildAudioManager.closeAudioConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "volume":
                audioGlobalManager.setVolume(Integer.parseInt(args[args.length-1]));
                break;
            default:
                targetChannel = event.getMember().getVoiceState().getChannel();
                if (targetChannel == null) {
                    event.getTextChannel().sendMessage("Veuillez rejoindre un salon vocal.").queue();
                } else {
                    String youtubeQuery = "";
                    for(int i = 0; i  < args.length; ++i){
                        youtubeQuery += args[i] + " ";
                    }
                    String youtubeSearch = youtubeApi.searchVideo(youtubeQuery);
                    System.out.println(youtubeSearch);
                    if (!("").equalsIgnoreCase(youtubeSearch)) {
                        String videoURL = "https://youtu.be/" + youtubeSearch;
                        myAudioLoadResultHandler.setTargetVoicelChannel(targetChannel);
                        audioGlobalManager.loadTrack(videoURL);
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