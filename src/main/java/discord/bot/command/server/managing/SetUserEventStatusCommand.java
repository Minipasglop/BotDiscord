package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.ServerPropertiesManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SetUserEventStatusCommand implements ICommand {

    private final String HELP = "Toggle the user join / leave message. \nUsage : `!setUserEventStatus status (true|false)`";
    private final String COMMAND_SUCCESS_ON = "Successfully toggled on the greetings message.";
    private final String COMMAND_SUCCESS_OFF = "Successfully toggled off the greetings message";
    private final String COMMAND_FAILED = "An unexpected error occured. Please make sure you have administration rights.";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0 && args[0].equals("help") || args.length < 1) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            ServerPropertiesManager.getInstance().setPropertyForServer(event.getGuild().getId(), "userEventEnabled", args[args.length - 1]);
            if(("true").equals(args[args.length -1])) event.getTextChannel().sendMessage(COMMAND_SUCCESS_ON).queue();
            else if(("false").equals(args[args.length -1])) event.getTextChannel().sendMessage(COMMAND_SUCCESS_OFF).queue();
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