package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.ServerPropertiesManager;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SetUserEventChannelCommand implements ICommand {

    private final String HELP = "Sets the channel where you whant the join / leave message to be send. \nUsage : `!setUserEventChannel channelName`";
    private final String COMMAND_SUCCESS = "Successfully set the greetings message channel destination.\nMake sure you have `!setUserEventStatus true` to activate the feature.";
    private final String COMMAND_FAILED = "An unexpected error occured. Please make sure Jackson has administrations role on the server.";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0 && args[0].equals("help") || args.length < 1) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!event.getGuild().getTextChannelsByName(args[args.length - 1],true).isEmpty()) {
            ServerPropertiesManager.getInstance().setPropertyForServer(event.getGuild().getId(), "userEventChannel", args[args.length - 1]);
            event.getTextChannel().sendMessage(COMMAND_SUCCESS).queue();
        } else {
            event.getMessage().delete().queue();
            PrivateChannel chanToTalk = event.getAuthor().openPrivateChannel().complete();
            chanToTalk.sendMessage(COMMAND_FAILED).queue();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        if (!success) {
            event.getTextChannel().sendMessage(help()).queue();
        }
    }
}