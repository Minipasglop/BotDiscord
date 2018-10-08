package discord.bot.command.misc;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.audio.Track;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.misc.YoutubeApi;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class YoutubeCommand extends ICommand {

    private final String HELP = "Looks for a video in YouTube. \nUsage : `!" + this.commandName + " requested song name`";

    private final String NO_RESULT = "No result found for the requested video. Sorry bro :zipper_mouth: ";

    private final YoutubeApi youtubeApi = BotGlobalManager.getYoutubeApi();

    public YoutubeCommand(String commandName) {
        super(commandName);
    }


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
        Track youtubeSearch = youtubeApi.searchVideo(youtubeQuery);
        if(youtubeSearch != null){
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),youtubeSearch.getVideoUrl());
        }else {
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),NO_RESULT);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
