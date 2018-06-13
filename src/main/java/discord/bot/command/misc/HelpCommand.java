package discord.bot.command.misc;

import discord.bot.command.ICommand;
import discord.bot.utils.CommandHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Map;

public class HelpCommand implements ICommand {

    private String HELP = "The command `help` displays the commands available at the moment. \nUsage: `!help`";
    private String MESSAGE_HEADER = "The commands available at the moment are listed below. All commands must be prefixed with a `!`. \nTo obtain more information on a command, just type `!command help`\n\n";
    private String MESSAGE_FOOTER ="\nIf you need help, please mind joining the support server : https://discord.gg/MUaWKcu\nThank's for using *Jackson* :heart: :smirk: ";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0 && args[0].equals("help") || args.length != 0) {
            return false;
        } else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String commandList = "**Commands**\n";
        Map<String, ICommand> entry = CommandHandler.getInstance().getCommands();
        Object[] commandArray = entry.keySet().toArray();
        for(int i = 0; i < commandArray.length; i++) {
            String command = commandArray[i].toString();
            commandList += (i%3 == 0 && i!=0 ? "\n":"") + "`" + command + "`" + "\t";
        }
        String messageToSend = MESSAGE_HEADER + commandList + MESSAGE_FOOTER;
        event.getAuthor().openPrivateChannel().queue((channel) -> channel.sendMessage(messageToSend).queue());
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