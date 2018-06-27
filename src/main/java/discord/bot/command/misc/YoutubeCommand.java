package discord.bot.command.misc;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.YoutubeApi;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Map;

public class YoutubeCommand implements ICommand {

    private final String HELP = "Looks for a video in YouTube. \nUsage : `!yt query parameters`";

    private final String NO_RESULT = "No result found for the requested video. Sorry bro :zipper_mouth: ";

    private final YoutubeApi youtubeApi = BotGlobalManager.getYoutubeApi();


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return args.length != 0 && !args[0].equals("help");
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String youtubeQuery = "";
        for(int i = 0; i  < args.length; ++i){
            youtubeQuery += args[i] + " ";
        }
        Map<String,String> youtubeSearch = youtubeApi.searchVideo(youtubeQuery);
        if(youtubeSearch != null){
            String videoURL = "https://youtu.be/" + youtubeSearch.get("videoUrl");
            event.getTextChannel().sendMessage(videoURL).queue();
        }else {
            event.getTextChannel().sendMessage(NO_RESULT).queue();
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
