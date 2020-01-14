package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.save.ServerPropertiesManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SetUserEventChannelCommand extends ICommand {

    private final String HELP = "Sets the channel where you whant the join / leave message to be send. \nUsage : `!"+ this.commandName +" channelName`";
    private final String COMMAND_SUCCESS = "Successfully set the greetings message channel destination.\nMake sure you have `!greetingsmessage on` to activate the feature.";
    private final String COMMAND_FAILED = "An unexpected error occured. Please make sure JacksonBot has administrations role on the server.";

    public SetUserEventChannelCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0 && args[0].equals("help") || args.length < 1) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!event.getGuild().getTextChannelsByName(args[args.length - 1],true).isEmpty()) {
            ServerPropertiesManager.getInstance().setPropertyForServer(event.getGuild().getId(), "userEventChannel", args[args.length - 1]);
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), COMMAND_SUCCESS);
        } else {
            event.getMessage().delete().queue();
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(), COMMAND_FAILED, event.getTextChannel(), COMMAND_FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }
}