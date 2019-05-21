package discord.bot.command.misc;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.ChuckNorrisApiCall;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ChuckNorrisCommand extends ICommand {

    private final String HELP = "Get a sick chuck norris fact. \nUsage : `!" + this.commandName + "`";
    private final String FAILED = "Failed to load a chuck norris fact. Hope he won't load you :sweat: ";

    public ChuckNorrisCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0) {
            return false;
        } else return true;    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String chuckNorrisFact = ChuckNorrisApiCall.getInstance().getRandomFact();
        if(chuckNorrisFact != null && !chuckNorrisFact.isEmpty()){
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),chuckNorrisFact);
        }else {
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
