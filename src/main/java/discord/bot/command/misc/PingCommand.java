package discord.bot.command.misc;

import discord.bot.command.ICommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PingCommand implements ICommand {

    String HELP = "Command for testing bot ping. \nUsage : `!ping`";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length != 0 && args[0].equals("help") || args.length != 0) {return false;}
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage("The bot ping is : " + event.getJDA().getPing() + " ms").queue();
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
