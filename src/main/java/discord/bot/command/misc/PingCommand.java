package discord.bot.command.misc;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PingCommand extends ICommand {

    String HELP = "Command for testing bot ping. \nUsage : `!" + this.commandName + "`";

    public PingCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return (args.length == 0 || !args[0].equals("help")) && args.length == 0;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),"The bot ping is : " + event.getJDA().getPing() + " ms");
    }

    @Override
    public String help() {
        return HELP;
    }

}
