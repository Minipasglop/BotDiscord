package discord.bot.command.bot.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.save.PropertyEnum;
import discord.bot.utils.save.ServerPropertiesManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SetPrefixCommand extends ICommand {

    private final String HELP = "This command set the bot's command prefix. \nUsage: `!" + this.commandName + " prefix`";
    private final String COMMAND_SUCCESS = "Successfully updated prefix.";

    public SetPrefixCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0 && args[0].equals("help") || args.length < 1) {
            return false;
        } else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        ServerPropertiesManager.getInstance().setPropertyForServer(event.getGuild().getId(), PropertyEnum.PREFIX.getPropertyName(), args[0]);
        MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),COMMAND_SUCCESS);
    }

    @Override
    public String help() {
        return HELP;
    }

}
